/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */

package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinancePeriodTypeService extends AbstractService {
    @Override
    public String getServiceName() {
        return "finance.period.type";
    }

    @Override
    public String getLabel() {
        return "会计期间类型";
    }

    @Override
    public Field getNameField() {
        return periodTypeName;
    }

    // 会计期间类型基本信息字段
    public final Field periodTypeName = Fields.createString("期间类型名称", true, 50);
    public final Field periodTypeCode = Fields.createString("期间类型编码", true, 20);
    public final Field startDate = Fields.createDate("会计期间开始日期", true);
    public final Field isActive = Fields.createBoolean("是否启用", false, true);
    public final Field isDefault = Fields.createBoolean("是否默认类型", false, false);
    public final Field description = Fields.createText("备注");
} 