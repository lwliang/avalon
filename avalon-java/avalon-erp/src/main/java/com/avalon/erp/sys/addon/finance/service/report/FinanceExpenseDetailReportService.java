package com.avalon.erp.sys.addon.finance.service.report;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.report.table.ReportTable;
import com.avalon.core.model.report.table.ReportTableColumn;
import com.avalon.core.service.AbstractReportService;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.condition.Condition;
import com.avalon.erp.sys.addon.finance.model.enums.AccountTypeEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用明细表报表服务
 *
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */
@Service
@Slf4j
public class FinanceExpenseDetailReportService extends AbstractReportService {

    @Override
    public String getServiceName() {
        return "finance.expense.detail.report";
    }

    @Override
    public String getLabel() {
        return "费用明细表";
    }

    // 查询条件字段
    public final Field startPeriod = Fields.createDate("开始期间");
    public final Field endPeriod = Fields.createDate("结束期间");
    public final Field accountSetId = Fields.createMany2one("账套", "finance.account.set");
    public final Field expenseAccountId = Fields.createMany2one("费用科目", "finance.account");

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        RecordRow recordRow = super.create(defaultRow);
        RecordRow defaultAccountSet = (RecordRow) invokeMethod("finance.account.set", "getDefaultAccountSet");
        if (ObjectUtils.isNotNull(defaultAccountSet)) {
            recordRow.put("accountSetId", defaultAccountSet);
        }
        return recordRow;
    }

    @Override
    public ReportTable getReportTable(RecordRow recordRow) {
        ReportTable reportTable = new ReportTable();

        // 获取查询条件
        Integer accountSetId = recordRow.getInteger("accountSetId");
        Integer expenseAccountId = null;
        if(recordRow.isNotNull("expenseAccountId")) {
            expenseAccountId = recordRow.getInteger("expenseAccountId");
        }
        String startPeriodStr = recordRow.getString("startPeriod");
        String endPeriodStr = recordRow.getString("endPeriod");

        if (accountSetId == null || startPeriodStr == null || endPeriodStr == null) {
            log.warn("费用明细表查询条件不完整");
            return reportTable;
        }

        // 设置报表列 - 费用明细表格式
        List<ReportTableColumn> columns = new ArrayList<>();
        columns.add(createColumn("voucherDate", "凭证日期"));
        columns.add(createColumn("accountCode", "科目编码"));
        columns.add(createColumn("accountName", "科目名称"));
        columns.add(createColumn("summary", "摘要"));
        columns.add(createColumn("debitAmount", "借方金额"));
        columns.add(createColumn("creditAmount", "贷方金额"));
        columns.add(createColumn("netAmount", "净额"));
        reportTable.setColumns(columns);

        // 构建费用明细表数据
        Record data = buildExpenseDetailData(accountSetId, expenseAccountId, startPeriodStr, endPeriodStr);
        reportTable.setData(data);

        log.info("费用明细表构建完成，数据行数: {}", data.size());
        return reportTable;
    }

    /**
     * 构建费用明细表数据
     *
     * @param accountSetId     账套ID
     * @param expenseAccountId 费用科目ID
     * @param startPeriodStr   开始期间
     * @param endPeriodStr     结束期间
     * @return 费用明细表数据
     */
    private Record buildExpenseDetailData(Integer accountSetId, Integer expenseAccountId, 
                                        String startPeriodStr, String endPeriodStr) {
        Record result = Record.build();

        // 1. 获取期间内的凭证行数据
        Record periodData = getVoucherLineDataInPeriod(accountSetId, expenseAccountId, startPeriodStr, endPeriodStr);

        // 2. 构建费用明细项目
        buildExpenseDetailItems(result, periodData);

        log.info("费用明细表数据构建完成，项目数量: {}", result.size());

        return result;
    }

    /**
     * 构建费用明细表项目
     *
     * @param result     结果记录
     * @param periodData 期间数据
     */
    private void buildExpenseDetailItems(Record result, Record periodData) {
        for (RecordRow line : periodData) {
            addExpenseDetailItem(result, line);
        }
    }

    /**
     * 添加费用明细表项目
     *
     * @param result 结果记录
     * @param line   凭证行数据
     */
    private void addExpenseDetailItem(Record result, RecordRow line) {
        RecordRow itemRow = RecordRow.build();
        
        // 基本信息
        itemRow.put("voucherDate", line.getString("voucherDate"));
        itemRow.put("accountCode", line.getString("accountCode"));
        itemRow.put("accountName", line.getString("accountName"));
        itemRow.put("summary", line.getString("summary"));
        
        // 金额信息
        BigDecimal debitAmount = line.getBigDecimal("debitAmount");
        BigDecimal creditAmount = line.getBigDecimal("creditAmount");
        itemRow.put("debitAmount", debitAmount != null ? debitAmount : BigDecimal.ZERO);
        itemRow.put("creditAmount", creditAmount != null ? creditAmount : BigDecimal.ZERO);
        
        // 计算净额
        BigDecimal netAmount = (debitAmount != null ? debitAmount : BigDecimal.ZERO)
                .subtract(creditAmount != null ? creditAmount : BigDecimal.ZERO);
        itemRow.put("netAmount", netAmount);
        
        result.add(itemRow);
    }

    /**
     * 获取期间内的凭证行数据
     *
     * @param accountSetId     账套ID
     * @param expenseAccountId 费用科目ID
     * @param startPeriodStr   开始期间
     * @param endPeriodStr     结束期间
     * @return 期间内凭证行数据
     */
    private Record getVoucherLineDataInPeriod(Integer accountSetId, Integer expenseAccountId, 
                                            String startPeriodStr, String endPeriodStr) {
        AbstractService voucherLineService = getServiceBean("finance.voucher.line");

        // 构建查询条件
        Condition condition = Condition.andCondition(
                Condition.equalCondition("voucherId.accountSetId", accountSetId),
                Condition.andCondition(
                        Condition.greaterEqualCondition("voucherId.date", "'" + startPeriodStr + "'"),
                        Condition.lessEqualCondition("voucherId.date", "'" + endPeriodStr + "'")));

        // 如果指定了费用科目，添加科目条件
        if (expenseAccountId != null) {
            condition = Condition.andCondition(condition, Condition.equalCondition("accountId", expenseAccountId));
        } else {
            // 如果没有指定科目，只查询费用类科目
            condition = Condition.andCondition(condition, Condition.equalCondition("accountId.type", "expense"));
        }

        // 查询凭证行数据
        Record voucherLines = voucherLineService.select(condition,
                "id", "voucherId.date", "accountId", "accountId.code", 
                "accountId.name", "summary", "debitAmount", "creditAmount");

        // 转换数据格式
        Record mergedData = Record.build();
        for (RecordRow line : voucherLines) {
            RecordRow dataRow = RecordRow.build();
            dataRow.put("voucherDate", line.getString("voucherId.date"));
            dataRow.put("accountCode", line.getString("accountId.code"));
            dataRow.put("accountName", line.getString("accountId.name"));
            dataRow.put("summary", line.getString("summary"));
            dataRow.put("debitAmount", line.getBigDecimal("debitAmount"));
            dataRow.put("creditAmount", line.getBigDecimal("creditAmount"));
            mergedData.add(dataRow);
        }

        return mergedData;
    }

    /**
     * 创建报表列
     *
     * @param name  列名
     * @param label 列标签
     * @return 报表列对象
     */
    private ReportTableColumn createColumn(String name, String label) {
        ReportTableColumn column = new ReportTableColumn();
        column.setName(name);
        column.setLabel(label);
        return column;
    }
} 