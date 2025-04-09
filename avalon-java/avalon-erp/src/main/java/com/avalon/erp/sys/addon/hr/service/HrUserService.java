package com.avalon.erp.sys.addon.hr.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/04 21:38
 */
@Service
@Slf4j
public class HrUserService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.user";
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    @Override
    public String getInherit() {
        return "base.user";
    }

    public Field staffId = Fields.createMany2one("员工", "hr.staff");
}
