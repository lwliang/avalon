/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.finance;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.TransientService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FinanceModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "finance";
    }

    @Override
    public String getLabel() {
        return "财务";
    }

    @Override
    public String getDescription() {
        return "财务模块 包含基础资料、会计科目、账簿管理等功能";
    }

    @Override
    public String getIcon() {
        return "resource/finance.png";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String[] depends() {
        return new String[]{
                "common",
                "hr"
        };
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/record/finance.period.type.xml",
                "resource/record/finance.account.xml",
                "resource/record/finance.account.set.xml",
                "resource/record/finance.opening.balance.xml",
                "resource/record/finance.auxiliary.type.xml",

                "resource/view/finance.period.type.views.xml",
                "resource/view/finance.account.set.views.xml",
                "resource/view/finance.account.views.xml",
                "resource/view/finance.opening.balance.views.xml",
                "resource/view/finance.auxiliary.type.views.xml",
                "resource/view/finance.auxiliary.item.views.xml",
                "resource/view/finance.voucher.views.xml",
                "resource/view/finance.voucher.line.views.xml",

                "resource/view/report/finance.asset.balance.report.views.xml",
                "resource/view/report/finance.account.summary.report.views.xml",
                "resource/view/report/finance.profit.report.views.xml",
                "resource/view/report/finance.cash.flow.report.views.xml",
                "resource/view/report/finance.expense.detail.report.views.xml",

                "resource/menu.xml"
        };
    }
} 