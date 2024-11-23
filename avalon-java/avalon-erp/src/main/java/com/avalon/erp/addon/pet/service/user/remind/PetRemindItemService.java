/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.remind;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetRemindItemService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.remind.item";
    }

    @Override
    public String getLabel() {
        return "提醒事项,比如铲屎,喂奶等等";
    }

    public final Field userId = Fields.createMany2one("宠物主", "pet.user");
}
