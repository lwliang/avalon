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

@Slf4j
@Service
public class CommodityImageService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.image";
    }

    @Override
    public String getLabel() {
        return "商品图片";
    }

    protected final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    protected final Field url = Fields.createImage("图片");
    protected final Field urlThumb = Fields.createImage("缩略图");
    protected final Field sequence = Fields.createInteger("顺序");
}
