/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.account;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetAccountItemService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.account.item";
    }

    @Override
    public String getLabel() {
        return "宠物记账项目";
    }

    public final Field userId = Fields.createMany2one("用户", "pet.user");
}
