/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */

package com.avalon.erp.sys.addon.common.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrencyService extends AbstractService {
    @Override
    public String getServiceName() {
        return "common.currency";
    }

    @Override
    public String getLabel() {
        return "币别";
    }

    @Override
    public Field getNameField() {
        return name;
    }

    public final Field name = Fields.createString("币别名称", true, 50);
    public final Field code = Fields.createString("币别代码", true, 10);
    public final Field symbol = Fields.createString("货币符号", false, 10);
    public final Field precision = Fields.createInteger("小数位数", 0, 10);
    public final Field isActive = Fields.createBoolean("是否启用", false, true);
    public final Field isDefault = Fields.createBoolean("是否默认币别", false, false);
    public final Field remark = Fields.createText("备注");
} 