package com.avalon.erp.sys.addon.finance.service.report;

import org.springframework.stereotype.Service;

import com.avalon.core.condition.Condition;
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
import com.avalon.erp.sys.addon.finance.model.enums.AccountTypeEnum;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FinanceProfitReportService extends AbstractReportService {
    @Override
    public String getServiceName() {
        return "finance.profit.report";
    }

    @Override
    public String getLabel() {
        return "利润表";
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
        String startPeriodStr = recordRow.getString(startPeriod);
        String endPeriodStr = recordRow.getString(endPeriod);
        Integer accountSetIdValue = null;
        if (recordRow.containsKey(accountSetId)) {
            accountSetIdValue = recordRow.getInteger(accountSetId);
        }

        // 创建报表表格
        ReportTable reportTable = new ReportTable();

        // 设置报表列 - 利润表格式
        List<ReportTableColumn> columns = new ArrayList<>();
        columns.add(createColumn("itemName", "项目"));
        columns.add(createColumn("itemCode", "项目编码"));
        columns.add(createColumn("currentPeriodAmount", "本期金额"));
        reportTable.setColumns(columns);

        if (ObjectUtils.isNull(accountSetIdValue)) {
            return reportTable;
        }

        // 构建利润表数据
        Record profitData = buildProfitData(accountSetIdValue, startPeriodStr, endPeriodStr);

        // 设置报表数据
        reportTable.setData(profitData);

        return reportTable;
    }

    /**
     * 构建利润表数据
     *
     * @param accountSetId   账套ID
     * @param startPeriodStr 开始期间
     * @param endPeriodStr   结束期间
     * @return 利润表数据
     */
    private Record buildProfitData(Integer accountSetId, String startPeriodStr, String endPeriodStr) {
        Record result = Record.build();

        // 1. 获取期间内的凭证行数据
        Record currentPeriodData = getVoucherLineDataInPeriod(accountSetId, startPeriodStr, endPeriodStr);

        // 2. 构建利润表项目
        buildProfitItems(result, currentPeriodData);

        log.info("利润表数据构建完成，项目数量: {}", result.size());

        return result;
    }

        /**
     * 构建利润表项目
     *
     * @param result            结果记录
     * @param currentPeriodData 本期数据
     */
    private void buildProfitItems(Record result, Record currentPeriodData) {
        // 获取所有会计科目
        Record allAccounts = getAllAccounts();
        
        // 按科目类型分组
        Record revenueAccounts = getAccountsByType(allAccounts, AccountTypeEnum.revenue);
        Record expenseAccounts = getAccountsByType(allAccounts, AccountTypeEnum.expense);
        
        // 添加收入类项目
        addRevenueItems(result, revenueAccounts, currentPeriodData);
        
        // 添加费用类项目
        addExpenseItems(result, expenseAccounts, currentPeriodData);
        
        // 计算营业利润
        calculateOperatingProfit(result, currentPeriodData);
        
        // 计算利润总额
        calculateTotalProfit(result, currentPeriodData);
        
        // 计算净利润
        calculateNetProfit(result, currentPeriodData);
    }

        /**
     * 添加利润表项目
     *
     * @param result            结果记录
     * @param itemName          项目名称
     * @param accountCode       科目编码
     * @param currentPeriodData 本期数据
     * @param accountType       科目类型
     */
    private void addProfitItem(Record result, String itemName, String accountCode, 
                              Record currentPeriodData, AccountTypeEnum accountType) {
        RecordRow itemRow = RecordRow.build();
        itemRow.put("itemName", itemName);
        itemRow.put("itemCode", accountCode);
        
        // 计算本期金额
        BigDecimal currentAmount = calculateAccountAmount(currentPeriodData, accountCode, accountType);
        itemRow.put("currentPeriodAmount", currentAmount);
        
        result.add(itemRow);
    }

        /**
     * 计算营业利润
     */
    private void calculateOperatingProfit(Record result, Record currentPeriodData) {
        RecordRow itemRow = RecordRow.build();
        itemRow.put("itemName", "营业利润");
        itemRow.put("itemCode", "OP");
        
        // 本期营业利润 = 营业收入 - 营业成本 - 税金及附加 - 销售费用 - 管理费用 - 研发费用 - 财务费用 + 其他收益 + 投资收益
        BigDecimal currentOperatingProfit = calculateOperatingProfitAmount(currentPeriodData);
        itemRow.put("currentPeriodAmount", currentOperatingProfit);
        
        result.add(itemRow);
    }

    /**
     * 计算利润总额
     */
    private void calculateTotalProfit(Record result, Record currentPeriodData) {
        RecordRow itemRow = RecordRow.build();
        itemRow.put("itemName", "利润总额");
        itemRow.put("itemCode", "TP");
        
        // 本期利润总额 = 营业利润 + 营业外收入 - 营业外支出
        BigDecimal currentTotalProfit = calculateTotalProfitAmount(currentPeriodData);
        itemRow.put("currentPeriodAmount", currentTotalProfit);
        
        result.add(itemRow);
    }

    /**
     * 计算净利润
     */
    private void calculateNetProfit(Record result, Record currentPeriodData) {
        RecordRow itemRow = RecordRow.build();
        itemRow.put("itemName", "净利润");
        itemRow.put("itemCode", "NP");
        
        // 本期净利润 = 利润总额 - 所得税费用
        BigDecimal currentNetProfit = calculateNetProfitAmount(currentPeriodData);
        itemRow.put("currentPeriodAmount", currentNetProfit);
        
        result.add(itemRow);
    }

    /**
     * 计算营业利润金额
     */
    private BigDecimal calculateOperatingProfitAmount(Record periodData) {
        // 获取所有会计科目
        Record allAccounts = getAllAccounts();
        Record revenueAccounts = getAccountsByType(allAccounts, AccountTypeEnum.revenue);
        Record expenseAccounts = getAccountsByType(allAccounts, AccountTypeEnum.expense);
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        
        // 计算收入总额
        for (RecordRow account : revenueAccounts) {
            String accountCode = account.getString("code");
            BigDecimal amount = calculateAccountAmount(periodData, accountCode, AccountTypeEnum.revenue);
            totalRevenue = totalRevenue.add(amount);
        }
        
        // 计算费用总额
        for (RecordRow account : expenseAccounts) {
            String accountCode = account.getString("code");
            BigDecimal amount = calculateAccountAmount(periodData, accountCode, AccountTypeEnum.expense);
            totalExpense = totalExpense.add(amount);
        }
        
        return totalRevenue.subtract(totalExpense);
    }

    /**
     * 计算利润总额金额
     */
    private BigDecimal calculateTotalProfitAmount(Record periodData) {
        BigDecimal operatingProfit = calculateOperatingProfitAmount(periodData);
        
        // 获取所有会计科目
        Record allAccounts = getAllAccounts();
        Record revenueAccounts = getAccountsByType(allAccounts, AccountTypeEnum.revenue);
        Record expenseAccounts = getAccountsByType(allAccounts, AccountTypeEnum.expense);
        
        BigDecimal nonOperatingIncome = BigDecimal.ZERO;
        BigDecimal nonOperatingExpense = BigDecimal.ZERO;
        
        // 计算营业外收入（非主营业务收入）
        for (RecordRow account : revenueAccounts) {
            String accountCode = account.getString("code");
            if (isNonOperatingRevenue(accountCode)) {
                BigDecimal amount = calculateAccountAmount(periodData, accountCode, AccountTypeEnum.revenue);
                nonOperatingIncome = nonOperatingIncome.add(amount);
            }
        }
        
        // 计算营业外支出（非主营业务费用）
        for (RecordRow account : expenseAccounts) {
            String accountCode = account.getString("code");
            if (isNonOperatingExpense(accountCode)) {
                BigDecimal amount = calculateAccountAmount(periodData, accountCode, AccountTypeEnum.expense);
                nonOperatingExpense = nonOperatingExpense.add(amount);
            }
        }

        return operatingProfit.add(nonOperatingIncome).subtract(nonOperatingExpense);
    }

    /**
     * 计算净利润金额
     */
    private BigDecimal calculateNetProfitAmount(Record periodData) {
        BigDecimal totalProfit = calculateTotalProfitAmount(periodData);
        
        // 获取所有会计科目
        Record allAccounts = getAllAccounts();
        Record expenseAccounts = getAccountsByType(allAccounts, AccountTypeEnum.expense);
        
        BigDecimal incomeTax = BigDecimal.ZERO;
        
        // 计算所得税费用
        for (RecordRow account : expenseAccounts) {
            String accountCode = account.getString("code");
            if (isIncomeTaxExpense(accountCode)) {
                BigDecimal amount = calculateAccountAmount(periodData, accountCode, AccountTypeEnum.expense);
                incomeTax = incomeTax.add(amount);
            }
        }

        return totalProfit.subtract(incomeTax);
    }

    /**
     * 计算指定科目的金额
     *
     * @param periodData   期间数据
     * @param accountCode  科目编码
     * @param accountType  科目类型
     * @return 金额
     */
    private BigDecimal calculateAccountAmount(Record periodData, String accountCode, AccountTypeEnum accountType) {
        BigDecimal amount = BigDecimal.ZERO;

        for (RecordRow row : periodData) {
            String rowAccountCode = row.getString("accountCode");
            BigDecimal debitAmount = row.getBigDecimal("debitAmount");
            BigDecimal creditAmount = row.getBigDecimal("creditAmount");

            if (accountCode != null && accountCode.equals(rowAccountCode)) {
                if (accountType == AccountTypeEnum.revenue) {
                    // 收入类科目：贷方金额 - 借方金额
                    if (creditAmount != null) {
                        amount = amount.add(creditAmount);
                    }
                    if (debitAmount != null) {
                        amount = amount.subtract(debitAmount);
                    }
                } else if (accountType == AccountTypeEnum.expense) {
                    // 费用类科目：借方金额 - 贷方金额
                    if (debitAmount != null) {
                        amount = amount.add(debitAmount);
                    }
                    if (creditAmount != null) {
                        amount = amount.subtract(creditAmount);
                    }
                }
            }
        }

        return amount;
    }

    /**
     * 计算指定科目类型的金额
     *
     * @param periodData        期间数据
     * @param accountCodePrefix 科目编码前缀
     * @param accountType       科目类型
     * @return 金额
     */
    private BigDecimal calculateAccountTypeAmount(Record periodData, String accountCodePrefix,
            AccountTypeEnum accountType) {
        BigDecimal amount = BigDecimal.ZERO;

        for (RecordRow row : periodData) {
            String accountCode = row.getString("accountCode");
            if (accountCode != null && accountCode.startsWith(accountCodePrefix)) {
                BigDecimal debitAmount = row.getBigDecimal("debitAmount");
                BigDecimal creditAmount = row.getBigDecimal("creditAmount");

                if (accountType == AccountTypeEnum.revenue) {
                    // 收入类：贷方 - 借方
                    if (creditAmount != null) {
                        amount = amount.add(creditAmount);
                    }
                    if (debitAmount != null) {
                        amount = amount.subtract(debitAmount);
                    }
                } else if (accountType == AccountTypeEnum.expense) {
                    // 费用类：借方 - 贷方
                    if (debitAmount != null) {
                        amount = amount.add(debitAmount);
                    }
                    if (creditAmount != null) {
                        amount = amount.subtract(creditAmount);
                    }
                }
            }
        }

        return amount;
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

        // 按会计科目合并数据
        Record mergedData = Record.build();

        voucherLines.forEach(row -> {
            String accountCode = row.getRecordRow("accountId").getString("code");
            String accountName = row.getRecordRow("accountId").getString("name");
            BigDecimal debitAmount = row.getBigDecimal("debitAmount");
            BigDecimal creditAmount = row.getBigDecimal("creditAmount");

            // 查找是否已存在该科目的记录
            RecordRow existingRow = null;
            for (RecordRow existing : mergedData) {
                if (accountCode.equals(existing.getString("accountCode"))) {
                    existingRow = existing;
                    break;
                }
            }

            if (existingRow == null) {
                // 创建新记录
                existingRow = RecordRow.build();
                existingRow.put("accountCode", accountCode);
                existingRow.put("accountName", accountName);
                existingRow.put("debitAmount", BigDecimal.ZERO);
                existingRow.put("creditAmount", BigDecimal.ZERO);
                mergedData.add(existingRow);
            }

            // 累加金额
            BigDecimal currentDebit = existingRow.getBigDecimal("debitAmount");
            BigDecimal currentCredit = existingRow.getBigDecimal("creditAmount");

            if (debitAmount != null) {
                existingRow.put("debitAmount", currentDebit.add(debitAmount));
            }
            if (creditAmount != null) {
                existingRow.put("creditAmount", currentCredit.add(creditAmount));
            }
        });

        log.info("获取期间内凭证行数据成功,账套ID: {}, 期间: {} - {}, 原始记录数: {}, 合并后记录数: {}",
                accountSetId, startPeriodStr, endPeriodStr, voucherLines.size(), mergedData.size());

        return mergedData;
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
     * 添加收入类项目
     *
     * @param result            结果记录
     * @param revenueAccounts   收入类科目
     * @param currentPeriodData 本期数据
     */
    private void addRevenueItems(Record result, Record revenueAccounts, Record currentPeriodData) {
        for (RecordRow account : revenueAccounts) {
            String accountCode = account.getString("code");
            String accountName = account.getString("name");
            
            // 根据科目编码确定项目名称
            String itemName = getRevenueItemName(accountCode, accountName);
            
            addProfitItem(result, itemName, accountCode, currentPeriodData, AccountTypeEnum.revenue);
        }
    }

    /**
     * 添加费用类项目
     *
     * @param result            结果记录
     * @param expenseAccounts   费用类科目
     * @param currentPeriodData 本期数据
     */
    private void addExpenseItems(Record result, Record expenseAccounts, Record currentPeriodData) {
        for (RecordRow account : expenseAccounts) {
            String accountCode = account.getString("code");
            String accountName = account.getString("name");
            
            // 根据科目编码确定项目名称
            String itemName = getExpenseItemName(accountCode, accountName);
            
            addProfitItem(result, itemName, accountCode, currentPeriodData, AccountTypeEnum.expense);
        }
    }

    /**
     * 根据科目编码获取收入项目名称
     *
     * @param accountCode 科目编码
     * @param accountName 科目名称
     * @return 项目名称
     */
    private String getRevenueItemName(String accountCode, String accountName) {
        if (accountCode.startsWith("6001")) {
            return "主营业务收入";
        } else if (accountCode.startsWith("6051")) {
            return "其他业务收入";
        } else if (accountCode.startsWith("6111")) {
            return "投资收益";
        } else if (accountCode.startsWith("6301")) {
            return "营业外收入";
        } else if (accountCode.startsWith("6401")) {
            return "其他收益";
        } else {
            return accountName;
        }
    }

    /**
     * 根据科目编码获取费用项目名称
     *
     * @param accountCode 科目编码
     * @param accountName 科目名称
     * @return 项目名称
     */
    private String getExpenseItemName(String accountCode, String accountName) {
        if (accountCode.startsWith("6401")) {
            return "主营业务成本";
        } else if (accountCode.startsWith("6402")) {
            return "其他业务成本";
        } else if (accountCode.startsWith("6601")) {
            return "销售费用";
        } else if (accountCode.startsWith("6602")) {
            return "管理费用";
        } else if (accountCode.startsWith("6603")) {
            return "研发费用";
        } else if (accountCode.startsWith("6604")) {
            return "财务费用";
        } else if (accountCode.startsWith("6801")) {
            return "税金及附加";
        } else if (accountCode.startsWith("6711")) {
            return "营业外支出";
        } else {
            return accountName;
        }
    }

    /**
     * 判断是否为营业外收入
     *
     * @param accountCode 科目编码
     * @return 是否为营业外收入
     */
    private boolean isNonOperatingRevenue(String accountCode) {
        return accountCode.startsWith("6301"); // 营业外收入
    }

    /**
     * 判断是否为营业外支出
     *
     * @param accountCode 科目编码
     * @return 是否为营业外支出
     */
    private boolean isNonOperatingExpense(String accountCode) {
        return accountCode.startsWith("6711"); // 营业外支出
    }

    /**
     * 判断是否为所得税费用
     *
     * @param accountCode 科目编码
     * @return 是否为所得税费用
     */
    private boolean isIncomeTaxExpense(String accountCode) {
        return accountCode.startsWith("6801"); // 所得税费用
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