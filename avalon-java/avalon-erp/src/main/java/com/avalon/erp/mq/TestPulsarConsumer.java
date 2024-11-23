/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.mq;

import com.avalon.erp.sys.addon.base.service.UserService;
import com.avalon.core.model.Record;
import com.avalon.core.mq.PulsarConsumer;
import com.avalon.core.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestPulsarConsumer extends PulsarConsumer<MessageContent> {
    @Override
    public String getTopic() {
        return "test-topic";
    }

    @Override
    public String getSubscription() {
        return "test-subscription_" + SystemUtil.getMACAddress().hashCode();
    }

    @Autowired
    private UserService userService;

    @Override
    protected void dealMessage(MessageContent msg) throws Exception {
        super.dealMessage(msg);
    }
}
