/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.pet;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetPetVaccinationService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.pet.vaccination";
    }

    @Override
    public String getLabel() {
        return "宠物接种疫苗记录";
    }

    public final Field petId = Fields.createMany2one("宠物", "pet.user.pet");
    public final Field vaccinationDate = Fields.createDate("接种日期");
    public final Field vaccinationName = Fields.createString("接种疫苗");
}
