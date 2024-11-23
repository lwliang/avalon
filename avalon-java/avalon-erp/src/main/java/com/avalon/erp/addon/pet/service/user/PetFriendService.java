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
public class PetFriendService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.friend";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field friendId = Fields.createMany2one("好友id", "pet.user");
}
