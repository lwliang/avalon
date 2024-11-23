/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.pet;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.addon.pet.model.enums.DewormKindEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetDewormService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.pet.deworm";
    }

    @Override
    public String getLabel() {
        return "宠物驱虫记录";
    }

    public final Field petId = Fields.createMany2one("宠物", "pet.user.pet");
    public final Field dewormDate = Fields.createDate("驱虫时间");
    public final Field dewormKind = Fields.createSelection("驱虫方式", DewormKindEnum.class);
}
