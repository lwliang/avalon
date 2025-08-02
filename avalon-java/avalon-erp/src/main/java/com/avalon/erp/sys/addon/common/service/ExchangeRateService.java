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
public class ExchangeRateService extends AbstractService {
    @Override
    public String getServiceName() {
        return "common.exchange.rate";
    }

    @Override
    public String getLabel() {
        return "汇率";
    }

    @Override
    public Field getNameField() {
        return fromCurrencyId;
    }
    public final Field fromCurrencyId = Fields.createMany2one("源币别", "common.currency", true);
    public final Field toCurrencyId = Fields.createMany2one("目标币别", "common.currency", true);
    public final Field rate = Fields.createBigDecimal("汇率", true, 10, 6);
    public final Field effectiveDate = Fields.createDate("生效日期", true);
    public final Field isActive = Fields.createBoolean("是否启用", false, true);
    public final Field remark = Fields.createText("备注");
} 