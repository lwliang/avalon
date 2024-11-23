/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PetFollowService extends AbstractService {
    private final PetFanService petFanService;

    public PetFollowService(PetFanService petFanService) {
        this.petFanService = petFanService;
    }

    @Override
    public String getServiceName() {
        return "pet.follow";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field followId = Fields.createMany2one("关注id", "pet.user");

    /**
     * 关注
     *
     * @param userId   当前用户
     * @param followId 被关注用户
     */
    public void addFollow(Integer userId, Integer followId) {
        Integer count = selectCount(this.userId.eq(userId).andEqualCondition(this.followId, followId));

        if (count == 0) {
            RecordRow row = RecordRow.build();
            row.put(this.userId, userId);
            row.put(this.followId, followId);
            insert(row);
            petFanService.addFan(followId, userId); // userId 是 followId 的粉丝
        }
    }

    /**
     * 取消关注
     *
     * @param userId   当前用户
     * @param followId 被关注
     */
    public void deleteFollow(Integer userId, Integer followId) {
        delete(this.userId.eq(userId).andEqualCondition(this.followId, followId));
        petFanService.deleteFan(followId, userId);
    }
}
