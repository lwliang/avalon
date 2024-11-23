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
public class PetBigTypeService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.big.type";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("大类名", true);
    }

    protected Field img = Fields.createImage("图片");

    public Field smallTypeIds = Fields.createOne2many("小类",
            "pet.small.type",
            "petBigTypeId");

    public Field sequence = Fields.createInteger("排序");
}
