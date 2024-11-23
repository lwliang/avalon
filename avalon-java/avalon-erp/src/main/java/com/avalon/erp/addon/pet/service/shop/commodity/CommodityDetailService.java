/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.commodity;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommodityDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.detail";
    }

    @Override
    public String getLabel() {
        return "商品详情";
    }

    protected final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    protected final Field url = Fields.createImage("图片详情");
    protected final Field sequence = Fields.createInteger("顺序");
}
