package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 辅助核算类型服务
 * 
 * @author lwlianghehe@gmail.com
 * @date 2024/12/06
 */
@Slf4j
@Service
public class FinanceAuxiliaryTypeService extends AbstractService {
    
    @Override
    public String getServiceName() {
        return "finance.auxiliary.type";
    }

    @Override
    public String getLabel() {
        return "辅助核算类型";
    }

    @Override
    public Field getNameField() {
        return name;
    }

    /**
     * 辅助核算类型编码
     */
    public final Field code = Fields.createString("类型编码", true, 20);
    
    /**
     * 辅助核算类型名称
     */
    public final Field name = Fields.createString("类型名称", true, 100);
    
    /**
     * 辅助核算类型描述
     */
    public final Field description = Fields.createText("类型描述");
} 