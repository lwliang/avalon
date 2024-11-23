/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.share;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import com.avalon.erp.addon.im.service.ImService;
import com.avalon.erp.addon.pet.model.enums.NotifyTypeEnum;
import com.avalon.erp.addon.pet.service.user.PetNotifyService;
import com.avalon.erp.addon.pet.service.user.PetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetShareLikeService extends AbstractService {
    private final PetNotifyService petNotifyService;
    private final PetUserService petUserService;
    private final PetShareService petShareService;

    public PetShareLikeService(PetNotifyService petNotifyService,
                               PetUserService petUserService,
                               @Lazy PetShareService petShareService) {
        this.petNotifyService = petNotifyService;
        this.petUserService = petUserService;
        this.petShareService = petShareService;
    }

    @Override
    public String getServiceName() {
        return "pet.share.like";
    }

    public final Field shareId = Fields.createMany2one("分享id", "pet.share");
    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field thumbUp = Fields.createBoolean("点赞"); // true 表示之前点赞过，false 点赞之后取消了，没有记录 则表示没有点赞过

    /**
     * 点赞 取消点赞
     *
     * @param shareId
     * @param userId
     * @param thumbUp 0 取消点赞 1 点赞
     */
    public void like(Integer shareId, Integer userId, Integer thumbUp) {
        Record select = select(Condition.equalCondition("shareId", shareId)
                        .andCondition(Condition.equalCondition("userId", userId)),
                getPrimaryKeyName());
        RecordRow recordRow = null;
        if (thumbUp == 1) {
            // 点赞
            if (select.isEmpty()) {
                recordRow = new RecordRow();
                recordRow.put("shareId", shareId);
                recordRow.put("userId", userId);
                recordRow.put("thumbUp", true);
                insert(recordRow);
            } else {
                recordRow = select.get(0);
                recordRow.put("thumbUp", true);
                update(recordRow);
            }
            Record shares = petShareService.select(Condition.equalCondition("id", shareId),
                    "comment", "userId");
            if (!shares.isEmpty()) {
                Integer commentUserId = shares.get(0).getInteger("userId");
                String title = shares.get(0).getString("comment");

                RecordRow content = RecordRow.build();
                content.put("likeId", recordRow.getInteger("id"));
                content.put("likeUserId", userId);
                content.put("likeUserName", petUserService.getUserNickName(userId));
                content.put("likeTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                content.put("fromType", "pet.share");
                content.put("fromSourceId", shareId);

                petNotifyService.sendEventMessage(title, "pet.share", shareId,
                        userId, commentUserId, NotifyTypeEnum.Like, content);
            }
        } else {
            // 取消点赞
            if (!select.isEmpty()) {
                recordRow = select.get(0);
                recordRow.put("thumbUp", false);
                update(recordRow);
            }
        }
    }
}
