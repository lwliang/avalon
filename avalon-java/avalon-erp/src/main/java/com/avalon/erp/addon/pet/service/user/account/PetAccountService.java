/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.account;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PetAccountService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.account";
    }

    @Override
    public String getLabel() {
        return "宠物记账";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("记账项目");
    }

    public final Field userId = Fields.createMany2one("用户", "pet.user");
    public final Field amount = Fields.createBigDecimal("金额");
    public final Field remark = Fields.createString("备注");
    public final Field accountDate = Fields.createDate("记账日期");

    public final Field petIds = Fields.createMany2many("宠物", "pet.user.pet");


    public RecordRow getSum(Integer userId, Integer year, Integer month) {
        StringBuilder sql = new StringBuilder("select sum(amount) as amount from pet_account where");
        sql.append(" user_id = ").append(userId);
        if (ObjectUtils.isNotNull(year) && ObjectUtils.isNotNull(month)) {
            sql.append(" and ").append("to_char(\"account_date\",'yyyy-mm')");
            sql.append(" = ").append("'").append(year).append("-")
                    .append(paddingZero(month)).append("'");
        } else if (ObjectUtils.isNotNull(year)) {
            sql.append(" and ").append("to_char(\"account_date\",'yyyy')");
            sql.append(" = ").append("'").append(year).append("'");
        }

        BigDecimal amount = getJdbcTemplate().executeScalar(sql, BigDecimal.class);
        RecordRow row = RecordRow.build();
        if (ObjectUtils.isNull(amount)) {
            amount = BigDecimal.ZERO;
        }
        row.put("amount", String.format("%.2f", amount));
        return row;
    }

    private String paddingZero(Integer num) {
        if (num < 10) {
            return "0" + num;
        }
        return num.toString();
    }
}
