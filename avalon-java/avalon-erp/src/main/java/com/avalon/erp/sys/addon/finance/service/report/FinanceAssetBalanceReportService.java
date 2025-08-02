package com.avalon.erp.sys.addon.finance.service.report;

import org.springframework.stereotype.Service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.report.echart.EchartOption;
import com.avalon.core.model.report.table.ReportTable;
import com.avalon.core.model.report.table.ReportTableColumn;
import com.avalon.core.service.AbstractReportService;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.sys.addon.finance.model.enums.AccountTypeEnum;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class FinanceAssetBalanceReportService extends AbstractReportService {
    @Override
    public String getServiceName() {
        return "finance.asset.balance.report";
    }

    @Override
    public String getLabel() {
        return "资产负债表";
    }

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

        // 设置报表列 - 包含借贷方向的资产负债表格式
        List<ReportTableColumn> columns = new ArrayList<>();
        columns.add(createColumn("accountCode", "科目编码"));
        columns.add(createColumn("accountName", "科目名称"));
        columns.add(createColumn("accountType", "科目类型"));
        columns.add(createColumn("openingDebitAmount", "期初借方"));
        columns.add(createColumn("openingCreditAmount", "期初贷方"));
        columns.add(createColumn("openingBalance", "期初余额"));
        columns.add(createColumn("currentDebitAmount", "本期借方"));
        columns.add(createColumn("currentCreditAmount", "本期贷方"));
        columns.add(createColumn("currentPeriodBalance", "本期发生额"));
        columns.add(createColumn("endingDebitAmount", "期末借方"));
        columns.add(createColumn("endingCreditAmount", "期末贷方"));
        columns.add(createColumn("endingBalance", "期末余额"));
        reportTable.setColumns(columns);

        if (ObjectUtils.isNull(accountSetIdValue)) {
            return reportTable;
        }
        // 构建资产负债表数据
        Record balanceSheetData = buildBalanceSheetData(accountSetIdValue, startPeriodStr, endPeriodStr);

        // 设置报表数据
        reportTable.setData(balanceSheetData);

        return reportTable;
    }

    /**
     * 构建资产负债表数据
     *
     * @param accountSetId   账套ID
     * @param startPeriodStr 开始期间
     * @param endPeriodStr   结束期间
     * @return 资产负债表数据
     */
    private Record buildBalanceSheetData(Integer accountSetId, String startPeriodStr, String endPeriodStr) {
        Record result = Record.build();

        try {
            // 1. 获取所有会计科目（包含type字段）
            Record allAccounts = getAllAccounts(accountSetId);

            // 2. 获取期初余额数据
            Record openingBalanceData = getOpeningBalanceData(accountSetId);

            // 3. 获取开始期间之前的凭证行数据（计算期初余额）
            Record beforeStartPeriodData = getVoucherLineDataBeforePeriod(accountSetId, startPeriodStr);

            // 4. 获取期间内的凭证行数据（计算本期发生额）
            Record currentPeriodData = getVoucherLineDataInPeriod(accountSetId, startPeriodStr, endPeriodStr);

            // 5. 构建资产负债表数据
            allAccounts.forEach(accountRow -> {
                String accountCode = accountRow.getString("code");
                String accountName = accountRow.getString("name");
                String accountType = accountRow.getString("type");

                // 创建科目记录
                RecordRow balanceRow = RecordRow.build();
                balanceRow.put("accountCode", accountCode);
                balanceRow.put("accountName", accountName);
                balanceRow.put("accountType", getAccountTypeEnum(accountType).getName());

                // 计算期初余额（包含借贷方向）
                BalanceInfo openingBalanceInfo = calculateOpeningBalanceWithDirection(accountCode, accountType, openingBalanceData, beforeStartPeriodData);
                balanceRow.put("openingDebitAmount", openingBalanceInfo.getDebitAmount());
                balanceRow.put("openingCreditAmount", openingBalanceInfo.getCreditAmount());
                balanceRow.put("openingBalance", openingBalanceInfo.getBalance());

                // 计算本期发生额（包含借贷方向）
                BalanceInfo currentPeriodInfo = calculateCurrentPeriodBalanceWithDirection(accountCode, accountType, currentPeriodData);
                balanceRow.put("currentDebitAmount", currentPeriodInfo.getDebitAmount());
                balanceRow.put("currentCreditAmount", currentPeriodInfo.getCreditAmount());
                balanceRow.put("currentPeriodBalance", currentPeriodInfo.getBalance());

                // 计算期末余额（包含借贷方向）
                BalanceInfo endingBalanceInfo = calculateEndingBalanceWithDirection(accountCode, accountType, openingBalanceInfo, currentPeriodInfo);
                balanceRow.put("endingDebitAmount", endingBalanceInfo.getDebitAmount());
                balanceRow.put("endingCreditAmount", endingBalanceInfo.getCreditAmount());
                balanceRow.put("endingBalance", endingBalanceInfo.getBalance());

                result.add(balanceRow);
            });

            log.info("资产负债表数据构建完成，科目数量: {}", result.size());

        } catch (Exception e) {
            log.error("构建资产负债表数据失败: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 余额信息类
     */
    private static class BalanceInfo {
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        private BigDecimal balance;

        public BalanceInfo(BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal balance) {
            this.debitAmount = debitAmount != null ? debitAmount : BigDecimal.ZERO;
            this.creditAmount = creditAmount != null ? creditAmount : BigDecimal.ZERO;
            this.balance = balance != null ? balance : BigDecimal.ZERO;
        }

        public BigDecimal getDebitAmount() {
            return debitAmount;
        }

        public BigDecimal getCreditAmount() {
            return creditAmount;
        }

        public BigDecimal getBalance() {
            return balance;
        }
    }

    /**
     * 计算期初余额（包含借贷方向，根据会计规范计算）
     *
     * @param accountCode           科目编码
     * @param accountType           科目类型
     * @param openingBalanceData    期初余额数据
     * @param beforeStartPeriodData 开始期间之前的凭证数据
     * @return 期初余额信息
     */
    private BalanceInfo calculateOpeningBalanceWithDirection(String accountCode, String accountType, Record openingBalanceData, Record beforeStartPeriodData) {
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;

        // 1. 从期初余额表获取基础借贷金额
        for (RecordRow row : openingBalanceData) {
            if (accountCode.equals(row.getRecordRow("accountId").getString("code"))) {
                BigDecimal baseDebitAmount = row.getBigDecimal("debitAmount");
                BigDecimal baseCreditAmount = row.getBigDecimal("creditAmount");

                if (baseDebitAmount != null) {
                    debitAmount = debitAmount.add(baseDebitAmount);
                }
                if (baseCreditAmount != null) {
                    creditAmount = creditAmount.add(baseCreditAmount);
                }
                break;
            }
        }

        // 2. 加上开始期间之前的凭证发生额
        for (RecordRow row : beforeStartPeriodData) {
            if (accountCode.equals(row.getString("accountCode"))) {
                BigDecimal rowDebitAmount = row.getBigDecimal("debitAmount");
                BigDecimal rowCreditAmount = row.getBigDecimal("creditAmount");

                if (rowDebitAmount != null) {
                    debitAmount = debitAmount.add(rowDebitAmount);
                }
                if (rowCreditAmount != null) {
                    creditAmount = creditAmount.add(rowCreditAmount);
                }
            }
        }

        // 3. 根据会计规范计算余额
        balance = calculateBalanceByAccountType(getAccountTypeEnum(accountType), debitAmount, creditAmount);

        return new BalanceInfo(debitAmount, creditAmount, balance);
    }

    /**
     * 根据会计规范计算余额
     *
     * @param accountType  科目类型枚举
     * @param debitAmount  借方金额
     * @param creditAmount 贷方金额
     * @return 计算后的余额
     */
    private BigDecimal calculateBalanceByAccountType(AccountTypeEnum accountType, BigDecimal debitAmount, BigDecimal creditAmount) {
        BigDecimal balance = BigDecimal.ZERO;

        switch (accountType) {
            case asset:
                // 资产类：借方 - 贷方
                balance = debitAmount.subtract(creditAmount);
                break;

            case liability:
            case equity:
            case revenue:
                // 负债、权益、收入类：贷方 - 借方
                balance = creditAmount.subtract(debitAmount);
                break;

            case expense:
                // 费用类：借方 - 贷方
                balance = debitAmount.subtract(creditAmount);
                break;
        }

        return balance;
    }

    /**
     * 安全地将字符串转换为AccountTypeEnum
     *
     * @param accountType 科目类型字符串
     * @return AccountTypeEnum枚举值
     */
    private AccountTypeEnum getAccountTypeEnum(String accountType) {
        if (accountType == null) {
            return AccountTypeEnum.asset; // 默认返回资产类
        }

        try {
            return AccountTypeEnum.valueOf(accountType.toLowerCase());
        } catch (IllegalArgumentException e) {
            log.warn("无法识别的科目类型: {}, 默认使用资产类", accountType);
            return AccountTypeEnum.asset; // 如果转换失败，默认返回资产类
        }
    }

    /**
     * 计算本期发生额（包含借贷方向，根据会计规范计算）
     *
     * @param accountCode       科目编码
     * @param accountType       科目类型
     * @param currentPeriodData 本期凭证数据
     * @return 本期发生额信息
     */
    private BalanceInfo calculateCurrentPeriodBalanceWithDirection(String accountCode, String accountType, Record currentPeriodData) {
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;

        for (RecordRow row : currentPeriodData) {
            if (accountCode.equals(row.getString("accountCode"))) {
                BigDecimal rowDebitAmount = row.getBigDecimal("debitAmount");
                BigDecimal rowCreditAmount = row.getBigDecimal("creditAmount");

                if (rowDebitAmount != null) {
                    debitAmount = debitAmount.add(rowDebitAmount);
                }
                if (rowCreditAmount != null) {
                    creditAmount = creditAmount.add(rowCreditAmount);
                }
            }
        }

        // 根据会计规范计算余额
        balance = calculateBalanceByAccountType(getAccountTypeEnum(accountType), debitAmount, creditAmount);

        return new BalanceInfo(debitAmount, creditAmount, balance);
    }

    /**
     * 计算期末余额（包含借贷方向，根据科目类型确定显示方向）
     *
     * @param accountCode        科目编码
     * @param accountType        科目类型
     * @param openingBalanceInfo 期初余额信息
     * @param currentPeriodInfo  本期发生额信息
     * @return 期末余额信息
     */
    private BalanceInfo calculateEndingBalanceWithDirection(String accountCode, String accountType,
                                                            BalanceInfo openingBalanceInfo, BalanceInfo currentPeriodInfo) {
        // 1. 累加借贷金额
        BigDecimal totalDebitAmount = openingBalanceInfo.getDebitAmount().add(currentPeriodInfo.getDebitAmount());
        BigDecimal totalCreditAmount = openingBalanceInfo.getCreditAmount().add(currentPeriodInfo.getCreditAmount());

        // 2. 根据科目类型计算期末余额
        BigDecimal totalBalance = calculateBalanceByAccountType(getAccountTypeEnum(accountType), totalDebitAmount, totalCreditAmount);

        // 3. 根据科目类型确定最终显示方向
        BigDecimal finalDebitAmount = BigDecimal.ZERO;
        BigDecimal finalCreditAmount = BigDecimal.ZERO;
        BigDecimal finalBalance = totalBalance;

        AccountTypeEnum type = getAccountTypeEnum(accountType);
        switch (type) {
            case asset:
            case expense:
                // 资产、费用类：正常余额在借方
                if (totalBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    finalDebitAmount = totalBalance;
                    finalCreditAmount = BigDecimal.ZERO;
                } else {
                    finalDebitAmount = BigDecimal.ZERO;
                    finalCreditAmount = totalBalance.abs();
                }
                break;

            case liability:
            case equity:
            case revenue:
                // 负债、权益、收入类：正常余额在贷方
                if (totalBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    finalDebitAmount = BigDecimal.ZERO;
                    finalCreditAmount = totalBalance;
                } else {
                    finalDebitAmount = totalBalance.abs();
                    finalCreditAmount = BigDecimal.ZERO;
                }
                break;
        }

        return new BalanceInfo(finalDebitAmount, finalCreditAmount, finalBalance);
    }

    /**
     * 获取所有会计科目
     *
     * @param accountSetId 账套ID
     * @return 会计科目记录
     */
    private Record getAllAccounts(Integer accountSetId) {
        try {
            AbstractService accountService = getServiceBean("finance.account");
            return accountService.select(null, "code", "name", "type", "direction");
        } catch (Exception e) {
            log.error("获取会计科目失败: {}", e.getMessage());
            return new Record();
        }
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
                        Condition.lessEqualCondition("voucherId.date", "'" + endPeriodStr + "'")
                )
        );

        // 查询凭证行数据
        Record voucherLines = voucherLineService.select(condition,
                "id","accountId", "accountId.code", "accountId.name", "debitAmount", "creditAmount");

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
     * 获取finance.opening.balance的借方和贷方金额
     *
     * @param accountSetId 账套ID
     * @return 期初余额记录（包含借贷金额）
     */
    public Record getOpeningBalanceData(Integer accountSetId) {
        try {
            // 获取期初余额服务
            AbstractService openingBalanceService = getServiceBean("finance.opening.balance");

            // 构建查询条件
            Condition condition = Condition.equalCondition("accountSetId", accountSetId);

            // 查询期初余额数据（获取借方和贷方金额）
            Record record = openingBalanceService.select(condition,
                    "id", "accountId", "accountId.id", "accountId.code", "accountId.name", "debitAmount", "creditAmount");

            log.info("获取期初余额数据成功,账套ID: {}, 记录数: {}",
                    accountSetId, record.size());

            return record;
        } catch (Exception e) {
            log.error("获取期初余额数据失败, 账套ID: {}, 错误: {}", accountSetId, e.getMessage());
            return new Record();
        }
    }

    /**
     * 获取指定期间之前的会计凭证行数据，按会计科目合并
     *
     * @param accountSetId   账套ID
     * @param startPeriodStr 开始期间字符串
     * @return 合并后的凭证行数据记录
     */
    public Record getVoucherLineDataBeforePeriod(Integer accountSetId, String startPeriodStr) {
        try {
            // 获取会计凭证行服务
            AbstractService voucherLineService = getServiceBean("finance.voucher.line");

            // 构建查询条件：账套ID相等且凭证日期小于开始期间
            Condition condition = Condition.andCondition(
                    Condition.equalCondition("voucherId.accountSetId", accountSetId),
                    Condition.lessCondition("voucherId.date", "'" + startPeriodStr + "'")
            );

            // 查询凭证行数据
            Record voucherLines = voucherLineService.select(condition,
                    "id","accountId", "accountId.code", "accountId.name", "debitAmount", "creditAmount");

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

            return mergedData;
        } catch (Exception e) {
            log.error("获取凭证行数据失败, 账套ID: {}, 期间: {}", accountSetId, startPeriodStr, e);
            return new Record();
        }
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
