/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.mq;


import com.avalon.core.config.PulsarConfig;
import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * 测试 消费者
 */


@Slf4j
public abstract class PulsarConsumer<T> implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private PulsarConfig pulsarConfig;

//    public abstract String getServiceName();

    public abstract String getTopic();

    public abstract String getSubscription();

    public PulsarClient getClient() {
        return client;
    }

    private PulsarClient client = null;

    public Consumer<T> getConsumer() {
        return consumer;
    }

    private Consumer<T> consumer = null;

    @Autowired
    private Context context;

    @PostConstruct
    public void initPulsar() throws Exception {
        if (ObjectUtils.isNull(pulsarConfig.getEnable()) || !pulsarConfig.getEnable()) return;
        try {
            //构造Pulsar client
            client = PulsarClient.builder()
                    .serviceUrl(pulsarConfig.getUrl())
                    .enableTransaction(true)
                    .build();
            Type genericSuperclass = this.getClass().getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type genericType = actualTypeArguments[0];
            Class schema = (Class) genericType;
            //创建consumer
            consumer = client.newConsumer(JSONSchema.of(schema))
                    .topic(getTopic())
                    .subscriptionName(getSubscription())
                    .subscriptionType(SubscriptionType.Shared)//指定消费模式，包含：Exclusive，Failover，Shared，Key_Shared。默认Exclusive模式
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)//指定从哪里开始消费还有Latest，valueof可选，默认Latest
                    .negativeAckRedeliveryDelay(60, TimeUnit.SECONDS)//指定消费失败后延迟多久broker重新发送消息给consumer，默认60s
                    .subscribe();

        } catch (Exception e) {
            log.error("Pulsar初始化异常：", e);
            throw e;
        }
    }

    /**
     * 消息处理，子类默认第一行调用，可以保证数据库配置正确
     *
     * @param msg
     * @throws Exception
     */
    protected void dealMessage(T msg) throws Exception {
        if (!context.getAvalonApplicationContext().isRunning()) {
            throw new Exception("avalon application context 正在初始化, 无法处理消息");
        }
        context.init(context.getApplicationConfig().getDataSource().getDatabase());
        context.setUserId(1);
    }

    private void start() {
        //消费消息
        while (true) {
            Message<T> message = null;
            try {
                message = consumer.receive();
                dealMessage(message.getValue());
                consumer.acknowledge(message);
            } catch (Exception e) {
                if (ObjectUtils.isNotNull(message)) {
                    consumer.negativeAcknowledge(message);
                }
                log.error(e.getMessage(), e);
            }
        }
    }

    public void close() {
        try {
            consumer.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar 消费者失败：", e);
        }
        try {
            client.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar 消费者连接失败：", e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            if (!pulsarConfig.getEnable()) return;
            close();
        } else if (event instanceof ApplicationReadyEvent) {
            if (!pulsarConfig.getEnable()) return;
            // 开始消费
            new Thread(() -> {/**/
                try {
                    start();
                } catch (Exception e) {
                    log.error("消费Pulsar数据异常，停止Pulsar连接：", e);
                    close();
                }
            }).start();
        }
    }
}
