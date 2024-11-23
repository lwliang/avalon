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

@Slf4j
@Service
public class PetSmallTypeService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.small.type";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("小类名", true);
    }

    public final Field petBigTypeId = Fields.createMany2one("大类", "pet.big.type");
    public final Field img = Fields.createImage("图片");
}
