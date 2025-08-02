/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */

package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.core.model.RecordRow;
import com.avalon.core.exception.AvalonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinanceOpeningBalanceService extends AbstractService {
    @Override
    public String getServiceName() {
        return "finance.opening.balance";
    }

    @Override
    public String getLabel() {
        return "期初余额";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("期初余额", true, 100);
    }

    // 期初余额基本信息字段
    public final Field accountSetId = Fields.createMany2one("账套", "finance.account.set", true);
    public final Field accountId = Fields.createMany2one("会计科目", "finance.account", true);
    public final Field periodStartDate = Fields.createDate("期间开始日期", true);
    public final Field debitAmount = Fields.createBigDecimal("借方金额", false, 15, 2);
    public final Field creditAmount = Fields.createBigDecimal("贷方金额", false, 15, 2);
    public final Field description = Fields.createText("备注");

    @Override
    protected void checkBeforeInsert(RecordRow recordRow) throws AvalonException {
        super.checkBeforeInsert(recordRow);
    }

    @Override
    protected void checkBeforeUpdate(RecordRow recordRow) throws AvalonException {
        super.checkBeforeUpdate(recordRow);
    }


}