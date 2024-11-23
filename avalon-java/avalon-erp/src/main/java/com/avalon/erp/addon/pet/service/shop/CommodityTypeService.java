/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommodityTypeService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.type";
    }

    @Override
    public String getLabel() {
        return "商品类型"; // 表示商品可以由狗，猫等适合
    }

    public final Field img = Fields.createImage("图片");

    public final Field sequence = Fields.createInteger("排序");
}
