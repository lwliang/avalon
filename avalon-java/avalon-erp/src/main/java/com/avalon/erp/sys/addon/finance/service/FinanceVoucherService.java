package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.avalon.core.model.RecordRow;

@Slf4j
@Service
public class FinanceVoucherService extends AbstractService {
    @Override
    public String getServiceName() { return "finance.voucher"; }
    @Override
    public String getLabel() { return "记账凭证"; }
    @Override
    public Field getNameField() { return code; }

    public final Field code = Fields.createString("凭证编号", true, 30);
    public final Field date = Fields.createDate("凭证日期", true);
    public final Field summary = Fields.createString("摘要", true, 200);
    public final Field accountSetId = Fields.createMany2one("账套", "finance.account.set", true);
    public final Field creatorId = Fields.createMany2one("制单人", "hr.staff", true);
    public final Field auditorId = Fields.createMany2one("审核人", "hr.staff", false);
    public final Field status = Fields.createSelection("状态", new com.avalon.core.model.SelectionHashMap() {{
        put("draft", "草稿");
        put("audited", "已审核");
        put("posted", "已记账");
    }}, "draft");
    public final Field description = Fields.createText("备注");
    public final Field totalDebitAmount = Fields.createBigDecimal("借方金额总和", false, 15, 2);
    public final Field totalCreditAmount = Fields.createBigDecimal("贷方金额总和", false, 15, 2);
    public final Field lines = Fields.createOne2many("凭证明细", "finance.voucher.line", "voucherId");
    

    @Override
    protected void checkAfterInsert(RecordRow row) {
        super.checkAfterInsert(row);
    }
    @Override
    protected void checkAfterUpdate(RecordRow row) {
        super.checkAfterUpdate(row);
    }
} 