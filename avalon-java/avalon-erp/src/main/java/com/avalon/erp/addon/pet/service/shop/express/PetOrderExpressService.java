/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.express;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PetOrderExpressService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.order.express";
    }

    @Override
    public String getLabel() {
        return "订单物流";
    }

    protected final Field expressNo = Fields.createString("快递单号");
    protected final Field expressCompany = Fields.createString("快递公司");
    protected final Field orderId = Fields.createMany2one("订单", "pet.order");
    protected final Field userId = Fields.createMany2one("用户", "pet.user");
    protected final Field currentStatus = Fields.createString("当前状态");
}
