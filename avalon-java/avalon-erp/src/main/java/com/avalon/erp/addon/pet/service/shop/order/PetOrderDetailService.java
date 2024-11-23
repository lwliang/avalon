/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.order;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetOrderDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.order.detail";
    }

    @Override
    public String getLabel() {
        return "订单详情,记录订单中的商品信息";
    }

    protected final Field orderId = Fields.createMany2one("订单", "pet.order");
    protected final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    protected final Field count = Fields.createInteger("数量");
    protected final Field price = Fields.createBigDecimal("价格");
    protected final Field total = Fields.createBigDecimal("总价");
}
