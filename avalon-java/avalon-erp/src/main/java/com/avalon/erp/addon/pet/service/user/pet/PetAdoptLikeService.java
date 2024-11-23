/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.pet;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.erp.addon.pet.model.enums.NotifyTypeEnum;
import com.avalon.erp.addon.pet.service.user.PetNotifyService;
import com.avalon.erp.addon.pet.service.user.PetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetAdoptLikeService extends AbstractService {
    private final PetUserService petUserService;
    private final PetAdoptService petAdoptService;
    private final PetNotifyService petNotifyService;

    public PetAdoptLikeService(PetUserService petUserService,
                               @Lazy PetAdoptService petAdoptService,
                               PetNotifyService petNotifyService) {
        this.petUserService = petUserService;
        this.petAdoptService = petAdoptService;
        this.petNotifyService = petNotifyService;
    }

    @Override
    public String getServiceName() {
        return "pet.adopt.like";
    }

    @Override
    public String getLabel() {
        return "宠物领养点赞";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field petAdoptId = Fields.createMany2one("宠物领养id", "pet.adopt");
    public final Field petUserId = Fields.createMany2one("点赞人", "pet.user");
    public final Field isLike = Fields.createBoolean("点赞");

    /**
     * 取消点赞
     *
     * @param petAdoptId
     * @param petUserId
     */
    public void unlike(Integer petAdoptId, Integer petUserId) {
        Record select = select(this.petAdoptId.eq(petAdoptId).andCondition(this.petUserId.eq(petUserId)), "id");
        if (!select.isEmpty()) {
            RecordRow recordRow = select.get(0);
            recordRow.put(this.petAdoptId, petAdoptId);
            recordRow.put(this.petUserId, petUserId);
            recordRow.put(this.isLike, false);
            update(recordRow);
        }
    }

    /**
     * 点赞
     *
     * @param petAdoptId
     * @param petUserId
     */
    public void like(Integer petAdoptId, Integer petUserId) {
        Record select = select(this.petAdoptId.eq(petAdoptId).andCondition(this.petUserId.eq(petUserId)), "id");
        RecordRow recordRow;
        if (select.isEmpty()) {
            recordRow = new RecordRow();
            recordRow.put(this.petAdoptId, petAdoptId);
            recordRow.put(this.petUserId, petUserId);
            recordRow.put(this.isLike, true);
            insert(recordRow);
        } else {
            recordRow = select.get(0);
            recordRow.put(this.isLike, true);
            update(recordRow);
        }

        Record adopts = petAdoptService.select(Condition.equalCondition("id", petAdoptId),
                "selfDescription", "publishId");
        if (!adopts.isEmpty()) {
            Integer commentUserId = adopts.get(0).getInteger("publishId");
            String title = adopts.get(0).getString("selfDescription");

            RecordRow content = RecordRow.build();
            content.put("likeId", recordRow.getInteger("id"));
            content.put("likeUserId", petUserId);
            content.put("likeUserName", petUserService.getUserNickName(petUserId));
            content.put("likeTime", DateTimeUtils.getCurrentDateTimeString());
            content.put("userId", commentUserId);
            content.put("userName", petUserService.getUserNickName(commentUserId));
            content.put("fromType", "pet.adopt");
            content.put("fromSourceId", petAdoptId);

            petNotifyService.sendEventMessage(title, "pet.adopt", petAdoptId,
                    petUserId, commentUserId, NotifyTypeEnum.Like, content);
        }
    }
}
