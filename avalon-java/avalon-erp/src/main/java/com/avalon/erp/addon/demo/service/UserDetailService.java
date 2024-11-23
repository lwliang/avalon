/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.demo.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "demo.user.detail";
    }

    private final Field userId = Fields.createMany2one("用户","base.user");

    private final Field age = Fields.createInteger("年龄");
    private final Field weight = Fields.createFloat("体重");

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    @Override
    protected void checkInsert(RecordRow recordRow) throws AvalonException {
        super.checkInsert(recordRow);
    }
}
