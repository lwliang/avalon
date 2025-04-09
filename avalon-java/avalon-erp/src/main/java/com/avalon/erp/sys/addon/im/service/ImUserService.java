package com.avalon.erp.sys.addon.im.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImUserService extends AbstractService {
    @Autowired
    private ImService imService;

    @Override
    public String getServiceName() {
        return "base.user";
    }

    @Override
    public String getInherit() {
        return "base.user";
    }

    public Field imUserId = Fields.createInteger("imId");

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);
        Integer imId = imService.registerIm("avalon", getContext().getBaseName(),
                recordRow.getInteger("id"));
        if (ObjectUtils.isEmpty(imId)) {
            log.error("注册im user id =null 用户报错,{}", recordRow.getInteger("id"));
        }
        RecordRow imRow = RecordRow.build();
        imRow.put("id", recordRow.getInteger("id"));
        imRow.put(imUserId, imId);
        update(imRow);
    }
}
