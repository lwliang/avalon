package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 辅助核算项目服务
 * 
 * @author lwlianghehe@gmail.com
 * @date 2024/12/06
 */
@Slf4j
@Service
public class FinanceAuxiliaryItemService extends AbstractService {
    
    @Override
    public String getServiceName() {
        return "finance.auxiliary.item";
    }

    @Override
    public String getLabel() {
        return "辅助核算项目";
    }


    @Override
    protected Field createNameField() {
        return Fields.createString("项目名称", true, 100);
    }

    /**
     * 辅助核算类型
     */
    public final Field auxiliaryTypeId = Fields.createMany2one("辅助核算类型", "finance.auxiliary.type", true);
    
    /**
     * 项目编码
     */
    public final Field code = Fields.createString("项目编码", true, 20);
    
    
    /**
     * 项目描述
     */
    public final Field description = Fields.createText("项目描述");
} 