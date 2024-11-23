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
public class PetAdoptImageService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.adopt.image";
    }

    @Override
    public String getLabel() {
        return "轮播图片";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field adoptId = Fields.createMany2one("领养id", "pet.adopt");
    public final Field image = Fields.createImage("图片");
    public final Field sequence = Fields.createInteger("序号");
}
