package com.avalon.erp.sys.addon.email.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.DynamicMailUtil;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/06/21 19:29
 */
@Service
@Slf4j
public class EmailMessageService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "email.message";
    }

    @Override
    public String getLabel() {
        return "邮件消息";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("邮件主题");
    }

    public Field body = Fields.createHtml("邮件内容");
    public Field userId = Fields.createMany2one("作者", "base.user");
    public Field emailFrom = Fields.createString("发件人");
    public Field emailTo = Fields.createString("收件人");
    public Field emailCC = Fields.createString("抄送");
    public Field replyTo = Fields.createString("回复");
    public Field emailTime = Fields.createDateTime("发送时间");
    public Field serviceId = Fields.createMany2one("关联业务", "base.service");
    public Field serviceResId = Fields.createInteger("关联业务资源ID");
    public Field uid = Fields.createBigIntegerField("邮件uid");

    @Override
    public PageInfo selectPage(PageParam pageParam, String order, Condition condition, String... fields) throws AvalonException {
        if (ObjectUtils.isNotNull(condition)) { // 只获取当前用户的邮件
            condition = condition.andEqualCondition("userId", getContext().getUserId());
        } else {
            condition = Condition.equalCondition("userId", getContext().getUserId());
        }
        return super.selectPage(pageParam, order, condition, fields);
    }

    @Override
    protected void checkBeforeInsert(RecordRow recordRow) throws AvalonException {
        super.checkBeforeInsert(recordRow);
        AbstractService emailAccountService = getServiceBean("email.account");
        Record services = emailAccountService.select(Condition.equalCondition("userId", getContext().getUserId()),
                "id", "name", "password", "emailServerId.sendHost", "emailServerId.sendPort", "emailServerId.protocol");
        if (!services.isEmpty()) {
            recordRow.put("emailFrom", services.get(0).getString("name"));
            RecordRow emailAccount = services.get(0).getRecordRow("emailServerId");
            try {
                DynamicMailUtil.sendMail(emailAccount.getString("sendHost"),
                        emailAccount.getInteger("sendPort"),
                        services.get(0).getString("name"),
                        services.get(0).getString("password"),
                        emailAccount.getString("protocol").equals("ssl"),
                        recordRow.getString("emailTo"),
                        recordRow.getString("name"),
                        recordRow.getString("body"));
            } catch (Exception e) {
                throw new AvalonException(e.getMessage(), e);
            }
        }
        recordRow.put("emailTime", DateTimeUtils.getCurrentDateTime());
        recordRow.put("userId", getContext().getUserId());

    }

    @Override
    public void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);

    }
}
