/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.commodity;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LimitedDiscountService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.limited.discount";
    }

    @Override
    public String getLabel() {
        return "限时折扣";
    }

    protected final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    protected final Field discount = Fields.createBigDecimal("折扣金额");
    protected final Field startTime = Fields.createDateTime("开始时间");
    protected final Field endTime = Fields.createDateTime("结束时间");

    @Override
    public PrimaryKey insertTableInfo(PrimaryKey moduleId) {
        return super.insertTableInfo(moduleId);
    }
}
