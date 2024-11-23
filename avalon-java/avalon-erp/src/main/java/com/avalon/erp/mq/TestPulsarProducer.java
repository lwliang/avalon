/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.mq;

import com.avalon.core.mq.PulsarProducer;
import com.avalon.core.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestPulsarProducer extends PulsarProducer<MessageContent> {
    @Override
    public String getTopic() {
        return "test-topic";
    }

    @Override
    public String getProducerName() {
        return "test-producer_" + SystemUtil.getMACAddress().hashCode();
    }
}
