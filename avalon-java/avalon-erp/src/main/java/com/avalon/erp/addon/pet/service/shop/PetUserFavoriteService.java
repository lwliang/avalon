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
public class PetUserFavoriteService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.favorite";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户", "pet.user");
    public final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
}
