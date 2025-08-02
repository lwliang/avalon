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
 * 现金流量表报表服务
 *
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */
@Service
@Slf4j
public class FinanceCashFlowReportService extends AbstractReportService {

    @Override
    public String getServiceName() {
        return "finance.cash.flow.report";
    }

    @Override
    public String getLabel() {
        return "现金流量表";
    }

    // 查询条件字段
    public final Field startPeriod = Fields.createDate("开始期间");
    public final Field endPeriod = Fields.createDate("结束期间");
    public final Field accountSetId = Fields.createMany2one("账套", "finance.account.set");

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
        String startPeriodStr = recordRow.getString("startPeriod");
        String endPeriodStr = recordRow.getString("endPeriod");

        if (accountSetId == null || startPeriodStr == null || endPeriodStr == null) {
            log.warn("现金流量表查询条件不完整");
            return reportTable;
        }

        // 设置报表列 - 现金流量表格式
        List<ReportTableColumn> columns = new ArrayList<>();
        columns.add(createColumn("accountCode", "科目编码"));
        columns.add(createColumn("accountName", "科目名称"));
        columns.add(createColumn("accountType", "科目类型"));
        columns.add(createColumn("debitAmount", "借方金额"));
        columns.add(createColumn("creditAmount", "贷方金额"));
        columns.add(createColumn("netAmount", "净额"));
        reportTable.setColumns(columns);

        // 构建现金流量表数据
        Record data = buildCashFlowData(accountSetId, startPeriodStr, endPeriodStr);
        reportTable.setData(data);

        log.info("现金流量表构建完成，数据行数: {}", data.size());
        return reportTable;
    }

    /**
     * 构建现金流量表数据
     *
     * @param accountSetId   账套ID
     * @param startPeriodStr 开始期间
     * @param endPeriodStr   结束期间
     * @return 现金流量表数据
     */
    private Record buildCashFlowData(Integer accountSetId, String startPeriodStr, String endPeriodStr) {
        Record result = Record.build();

        // 1. 获取期间内的凭证行数据
        Record periodData = getVoucherLineDataInPeriod(accountSetId, startPeriodStr, endPeriodStr);

        // 2. 获取所有会计科目
        Record allAccounts = getAllAccounts();

        // 3. 按科目类型分组并计算金额
        buildCashFlowItems(result, allAccounts, periodData);

        log.info("现金流量表数据构建完成，项目数量: {}", result.size());

        return result;
    }

    /**
     * 构建现金流量表项目
     *
     * @param result      结果记录
     * @param allAccounts 所有会计科目
     * @param periodData  期间数据
     */
    private void buildCashFlowItems(Record result, Record allAccounts, Record periodData) {
        // 按科目类型分组
        Record assetAccounts = getAccountsByType(allAccounts, AccountTypeEnum.asset);
        Record liabilityAccounts = getAccountsByType(allAccounts, AccountTypeEnum.liability);
        Record equityAccounts = getAccountsByType(allAccounts, AccountTypeEnum.equity);
        Record revenueAccounts = getAccountsByType(allAccounts, AccountTypeEnum.revenue);
        Record expenseAccounts = getAccountsByType(allAccounts, AccountTypeEnum.expense);

        // 添加资产类科目
        addAccountTypeItems(result, assetAccounts, periodData, "资产类");

        // 添加负债类科目
        addAccountTypeItems(result, liabilityAccounts, periodData, "负债类");

        // 添加所有者权益类科目
        addAccountTypeItems(result, equityAccounts, periodData, "所有者权益类");

        // 添加收入类科目
        addAccountTypeItems(result, revenueAccounts, periodData, "收入类");

        // 添加费用类科目
        addAccountTypeItems(result, expenseAccounts, periodData, "费用类");
    }

    /**
     * 添加指定类型的科目项目
     *
     * @param result      结果记录
     * @param accounts    科目列表
     * @param periodData  期间数据
     * @param accountType 科目类型名称
     */
    private void addAccountTypeItems(Record result, Record accounts, Record periodData, String accountType) {
        for (RecordRow account : accounts) {
            String accountCode = account.getString("code");
            String accountName = account.getString("name");

            addCashFlowItem(result, accountCode, accountName, accountType, periodData);
        }
    }

    /**
     * 添加现金流量表项目
     *
     * @param result      结果记录
     * @param accountCode 科目编码
     * @param accountName 科目名称
     * @param accountType 科目类型
     * @param periodData  期间数据
     */
    private void addCashFlowItem(Record result, String accountCode, String accountName,
            String accountType, Record periodData) {
        RecordRow itemRow = RecordRow.build();
        itemRow.put("accountCode", accountCode);
        itemRow.put("accountName", accountName);
        itemRow.put("accountType", accountType);

        // 计算借方金额
        BigDecimal debitAmount = calculateAccountDebitAmount(periodData, accountCode);
        itemRow.put("debitAmount", debitAmount);

        // 计算贷方金额
        BigDecimal creditAmount = calculateAccountCreditAmount(periodData, accountCode);
        itemRow.put("creditAmount", creditAmount);

        // 计算净额
        BigDecimal netAmount = debitAmount.subtract(creditAmount);
        itemRow.put("netAmount", netAmount);

        result.add(itemRow);
    }

    /**
     * 计算指定科目的借方金额
     *
     * @param periodData  期间数据
     * @param accountCode 科目编码
     * @return 借方金额
     */
    private BigDecimal calculateAccountDebitAmount(Record periodData, String accountCode) {
        BigDecimal amount = BigDecimal.ZERO;

        for (RecordRow row : periodData) {
            String rowAccountCode = row.getString("accountCode");
            BigDecimal debitAmount = row.getBigDecimal("debitAmount");

            if (accountCode != null && accountCode.equals(rowAccountCode) && debitAmount != null) {
                amount = amount.add(debitAmount);
            }
        }

        return amount;
    }

    /**
     * 计算指定科目的贷方金额
     *
     * @param periodData  期间数据
     * @param accountCode 科目编码
     * @return 贷方金额
     */
    private BigDecimal calculateAccountCreditAmount(Record periodData, String accountCode) {
        BigDecimal amount = BigDecimal.ZERO;

        for (RecordRow row : periodData) {
            String rowAccountCode = row.getString("accountCode");
            BigDecimal creditAmount = row.getBigDecimal("creditAmount");

            if (accountCode != null && accountCode.equals(rowAccountCode) && creditAmount != null) {
                amount = amount.add(creditAmount);
            }
        }

        return amount;
    }

    /**
     * 获取所有会计科目
     *
     * @return 会计科目记录
     */
    private Record getAllAccounts() {
        try {
            AbstractService accountService = getServiceBean("finance.account");
            return accountService.select(null, "code", "name", "type", "direction", "isActive");
        } catch (Exception e) {
            log.error("获取会计科目失败: {}", e.getMessage());
            return new Record();
        }
    }

    /**
     * 根据科目类型获取会计科目
     *
     * @param allAccounts 所有会计科目
     * @param accountType 科目类型
     * @return 指定类型的会计科目
     */
    private Record getAccountsByType(Record allAccounts, AccountTypeEnum accountType) {
        Record result = Record.build();
        for (RecordRow row : allAccounts) {
            String type = row.getString("type");
            Boolean isActive = row.getBoolean("isActive");
            if (accountType.name().equals(type) && Boolean.TRUE.equals(isActive)) {
                result.add(row);
            }
        }
        return result;
    }

    /**
     * 获取期间内的凭证行数据
     *
     * @param accountSetId   账套ID
     * @param startPeriodStr 开始期间
     * @param endPeriodStr   结束期间
     * @return 期间内凭证行数据
     */
    private Record getVoucherLineDataInPeriod(Integer accountSetId, String startPeriodStr, String endPeriodStr) {
        AbstractService voucherLineService = getServiceBean("finance.voucher.line");

        // 构建查询条件：账套ID相等且凭证日期在期间内
        Condition condition = Condition.andCondition(
                Condition.equalCondition("voucherId.accountSetId", accountSetId),
                Condition.andCondition(
                        Condition.greaterEqualCondition("voucherId.date", "'" + startPeriodStr + "'"),
                        Condition.lessEqualCondition("voucherId.date", "'" + endPeriodStr + "'")));

        // 查询凭证行数据
        Record voucherLines = voucherLineService.select(condition,
                "id", "accountId", "accountId.code", "accountId.name", "debitAmount", "creditAmount");
        // 转换数据格式
        Record mergedData = Record.build();
        for (RecordRow line : voucherLines) {
            RecordRow dataRow = RecordRow.build();
            dataRow.put("accountCode", line.getString("accountId.code"));
            dataRow.put("accountName", line.getString("accountId.name"));
            dataRow.put("accountType", line.getString("accountId.type"));
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