/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.finance.model.enums.AccountSetTypeEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinanceAccountSetService extends AbstractService {
    @Override
    public String getServiceName() {
        return "finance.account.set";
    }

    @Override
    public String getLabel() {
        return "账套";
    }

    @Override
    public Field getNameField() {
        return name;
    }

    // 账套基本信息字段
    public final Field code = Fields.createString("账套编码", true, 20);
    public final Field name = Fields.createString("账套名称", true, 100);
    public final Field type = Fields.createSelection("账套类型", AccountSetTypeEnum.class,
            AccountSetTypeEnum.COMPANY);
    public final Field companyId = Fields.createMany2one("关联公司", "hr.org", true);
    public final Field startPeriod = Fields.createDate("启用期间", true);
    public final Field endPeriod = Fields.createDate("结束期间", false);
    public final Field currencyId = Fields.createMany2one("本位币", "common.currency", true);
    public final Field periodTypeId = Fields.createMany2one("会计期间类型", "finance.period.type", true);
    public final Field taxRate = Fields.createBigDecimal("税率", false, 5, 2);
    public final Field isEnabled = Fields.createBoolean("是否启用", false, true);
    public final Field isDefault = Fields.createBoolean("是否默认账套", false, false);
    public final Field description = Fields.createText("备注", false);

    public RecordRow getDefaultAccountSet() {
        Condition condition = Condition.equalCondition(isDefault, true);
        Record results = select(condition, "id", "name");
        if(!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}