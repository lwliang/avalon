package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinanceVoucherLineService extends AbstractService {
    @Override
    public String getServiceName() { return "finance.voucher.line"; }
    @Override
    public String getLabel() { return "记账凭证明细"; }
    @Override
    public Field getNameField() { return summary; }

    public final Field voucherId = Fields.createMany2one("所属凭证", "finance.voucher", true);
    public final Field accountId = Fields.createMany2one("会计科目", "finance.account", true);
    public final Field summary = Fields.createString("分录摘要", true, 200);
    public final Field debitAmount = Fields.createBigDecimal("借方金额", true, 15, 2);
    public final Field creditAmount = Fields.createBigDecimal("贷方金额", true, 15, 2);
    public final Field auxiliaryItemId = Fields.createMany2one("辅助核算项目", "finance.auxiliary.item", false);
    public final Field description = Fields.createText("备注");
} 