/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.share;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetSharePetService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.share.pet";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field shareId = Fields.createMany2one("分享id", "pet.share");
    public final Field petId = Fields.createMany2one("宠物id", "pet.user.pet");
}
