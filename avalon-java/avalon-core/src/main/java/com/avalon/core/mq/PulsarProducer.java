/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.mq;

import com.avalon.core.config.PulsarConfig;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.SnowflakeIdWorker3rd;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class PulsarProducer<T> implements ApplicationListener<ApplicationEvent> {

    public abstract String getTopic();

    public abstract String getProducerName();

    @Autowired
    private PulsarConfig pulsarConfig;

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }

    private PulsarClient pulsarClient;

    public Producer<T> getProducer() {
        return producer;
    }

    private Producer<T> producer;

    @PostConstruct
    public void initPulsarClient() throws PulsarClientException {
        if (ObjectUtils.isNull(pulsarConfig.getEnable()) || !pulsarConfig.getEnable()) return;
        pulsarClient = PulsarClient.builder().enableTransaction(true).serviceUrl(pulsarConfig.getUrl()).build();
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type genericType = actualTypeArguments[0];
        Class schema = (Class) genericType;
        //创建producer
        producer = pulsarClient.newProducer(JSONSchema.of(schema))
                .topic(getTopic())
                .producerName(getProducerName())
                .enableBatching(true)//是否开启批量处理消息，默认true,需要注意的是enableBatching只在异步发送sendAsync生效，同步发送send失效。因此建议生产环境若想使用批处理，则需使用异步发送，或者多线程同步发送
                .compressionType(CompressionType.LZ4)//消息压缩（四种压缩方式：LZ4，ZLIB，ZSTD，SNAPPY），consumer端不用做改动就能消费，开启后大约可以降低3/4带宽消耗和存储（官方测试）
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS) //设置将对发送的消息进行批处理的时间段,10ms；可以理解为若该时间段内批处理成功，则一个batch中的消息数量不会被该参数所影响。
                .sendTimeout(0, TimeUnit.SECONDS)//设置发送超时0s；如果在sendTimeout过期之前服务器没有确认消息，则会发生错误。默认30s，设置为0代表无限制，建议配置为0
                .batchingMaxMessages(1000)//批处理中允许的最大消息数。默认1000
                .maxPendingMessages(1000)//设置等待接受来自broker确认消息的队列的最大大小，默认1000
                .blockIfQueueFull(true)//设置当消息队列中等待的消息已满时，Producer.send 和 Producer.sendAsync 是否应该block阻塞。默认为false，达到maxPendingMessages后send操作会报错，设置为true后，send操作阻塞但是不报错。建议设置为true
                .roundRobinRouterBatchingPartitionSwitchFrequency(10)//向不同partition分发消息的切换频率，默认10ms，可根据batch情况灵活调整
                .batcherBuilder(BatcherBuilder.DEFAULT)//key_Shared模式要用KEY_BASED,才能保证同一个key的message在一个batch里
                .create();

    }

    /**
     * 异步发送
     *
     * @param key  消息的键（字符串类型）。它是消息键或分区键的简称。
     * @param data 数据
     */
    public void sendAsync(String key, T data) {
        CompletableFuture<MessageId> future = producer.newMessage()
                .key(key)
                .value(data).sendAsync();//异步发送

        future.handle((v, ex) -> {
            if (ex == null) {
                log.info("Message persisted2: {}", data);
            } else {
                log.error("发送Pulsar消息失败msg:【{}】 ", data, ex);
            }
            return null;
        });
    }

    /**
     * 异步发送
     *
     * @param data  数据
     * @param delay 延迟的时间，单位秒
     */
    public void sendAsyncDeliverAfter(T data, Long delay) {
        int key = SnowflakeIdWorker3rd.nextUId();
        CompletableFuture<MessageId> future = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).sendAsync();//异步发送

        future.handle((v, ex) -> {
            if (ex == null) {
                log.info("Message persisted2: {}", data);
            } else {
                log.error("发送Pulsar消息失败msg:【{}】 ", data, ex);
            }
            return null;
        });
    }


    /**
     * 异步发送
     *
     * @param data 数据
     */
    public void sendAsync(T data) {
        int key = SnowflakeIdWorker3rd.nextUId();
        CompletableFuture<MessageId> future = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).sendAsync();//异步发送

        future.handle((v, ex) -> {
            if (ex == null) {
                log.info("Message persisted2: {}", data);
            } else {
                log.error("发送Pulsar消息失败msg:【{}】 ", data, ex);
            }
            return null;
        });
    }

    /**
     * 异步发送
     *
     * @param data     数据
     * @param sendTime 在指定时间发送消息
     */
    public void sendAsyncDeliverAt(T data, Date sendTime) {
        int key = SnowflakeIdWorker3rd.nextUId();
        CompletableFuture<MessageId> future = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).deliverAt(sendTime.getTime()).sendAsync();//异步发送

        future.handle((v, ex) -> {
            if (ex == null) {
                log.info("Message persisted2: {}", data);
            } else {
                log.error("发送Pulsar消息失败msg:【{}】 ", data, ex);
            }
            return null;
        });
    }


    /**
     * 同步发送
     *
     * @param key  消息的键（字符串类型）。它是消息键或分区键的简称。
     * @param data 数据
     * @throws PulsarClientException
     */
    public void send(String key, T data) throws PulsarClientException {
        MessageId messageId = producer.newMessage()
                .key(key)
                .value(data).send();//同步发送
    }

    /**
     * 同步延迟发送
     *
     * @param data  消息
     * @param delay 延迟的时间，单位秒
     * @throws PulsarClientException
     */
    public void sendDeliverAfter(T data, long delay) throws PulsarClientException {
        int key = SnowflakeIdWorker3rd.nextUId();
        MessageId messageId = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).deliverAfter(delay, TimeUnit.SECONDS).send();//同步发送
    }

    /**
     * 同步延迟发送
     *
     * @param data     消息
     * @param sendTime 在指定时间发送消息
     * @throws PulsarClientException
     */
    public void sendDeliverAt(T data, Date sendTime) throws PulsarClientException {
        int key = SnowflakeIdWorker3rd.nextUId();
        MessageId messageId = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).deliverAt(sendTime.getTime()).send();//同步发送
    }


    /**
     * 同步发送
     *
     * @param data 数据
     * @throws PulsarClientException
     */
    public void send(T data) throws PulsarClientException {
        int key = SnowflakeIdWorker3rd.nextUId();
        MessageId messageId = producer.newMessage()
                .key(String.valueOf(key))
                .value(data).send();//同步发送
    }


    public void close() {
        try {
            producer.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar 生产者 失败：", e);
        }
        try {
            pulsarClient.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar 生产者 连接失败：", e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStoppedEvent) {
            if (!pulsarConfig.getEnable()) return;
            close();
        }
    }

    /**
     * 开始事务消息
     *
     * @return
     */
    public Transaction beginTransaction() {
        try {
            return pulsarClient.newTransaction().withTransactionTimeout(1, TimeUnit.SECONDS).build().get();
        } catch (InterruptedException | ExecutionException | PulsarClientException e) {
            throw new AvalonException("开始事务消息失败" + e.getMessage());
        }
    }

    /**
     * 发送事务消息
     *
     * @param tran
     * @param msg
     */
    public void sendTransaction(Transaction tran, T msg) {
        try {
            producer.newMessage(tran).value(msg).send();
        } catch (PulsarClientException e) {
            throw new AvalonException("发送事务消息失败" + e.getMessage());
        }
    }

    /**
     * 提交事务
     *
     * @param tran
     */
    public void commitTransaction(Transaction tran) {
        tran.commit();
    }

    /**
     * 终止事务
     *
     * @param tran
     */
    public void abortTransaction(Transaction tran) {
        tran.abort();
    }

}
