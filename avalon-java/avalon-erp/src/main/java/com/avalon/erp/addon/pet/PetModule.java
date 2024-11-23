/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PetModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "pet";
    }

    @Override
    public String getLabel() {
        return "宠物";
    }

    @Override
    public String getDescription() {
        return "宠物模块";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }


    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/pet.big.type.views.xml",
                "resource/view/pet.small.type.views.xml",
                "resource/view/pet.user.views.xml",
                "resource/view/pet.user.pet.views.xml",
                "resource/view/pet.address.views.xml",
                "resource/view/pet.address.views.xml",
                "resource/view/pet.pet.deworm.views.xml",
                "resource/view/pet.pet.vaccination.views.xml",
                "resource/view/pet.vaccination.views.xml",
                "resource/view/pet.friend.views.xml",
                "resource/view/pet.fan.views.xml",
                "resource/view/pet.follow.views.xml",
                "resource/view/pet.share.views.xml",
                "resource/view/pet.share.image.views.xml",
                "resource/view/pet.share.pet.views.xml",
                "resource/view/pet.share.comment.views.xml",
                "resource/view/pet.remind.views.xml",
                "resource/view/pet.account.views.xml",
                "resource/view/pet.remind.item.views.xml",
                "resource/view/pet.account.item.views.xml",
                "resource/view/pet.adopt.views.xml",
                "resource/view/pet.adopt.image.views.xml",
                "resource/view/pet.adopt.message.views.xml",
                "resource/view/menus.xml"
        };
    }
}
