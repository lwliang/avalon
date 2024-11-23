/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetVaccinationService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.vaccination";
    }

    @Override
    public String getLabel() {
        return "宠物疫苗";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("疫苗");
    }

    public final Field introduce = Fields.createHtml("介绍");
    public final Field sequence = Fields.createInteger("序号");
    public final Field petCategoryIds = Fields.createMany2many("适用宠物",
            "pet.big.type");
}
