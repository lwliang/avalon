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
import com.avalon.erp.addon.pet.model.enums.NotifyTypeEnum;
import com.avalon.erp.addon.pet.service.user.PetNotifyService;
import com.avalon.erp.addon.pet.service.user.PetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetShareCommentLikeService extends AbstractService {
    private final PetShareService petShareService;
    private final PetUserService petUserService;
    private final PetNotifyService petNotifyService;

    public PetShareCommentLikeService(@Lazy PetShareService petShareService,
                                      PetUserService petUserService,
                                      PetNotifyService petNotifyService) {
        this.petShareService = petShareService;
        this.petUserService = petUserService;
        this.petNotifyService = petNotifyService;
    }

    @Override
    public String getServiceName() {
        return "pet.share.comment.like";
    }

    public final Field commentId = Fields.createMany2one("评论id", "pet.share.comment");
    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field thumbUp = Fields.createBoolean("点赞"); // true 表示之前点赞过，false 点赞之后取消了，没有记录 则表示没有点赞过

    /**
     * 评论点赞
     *
     * @param shareCommentId 评论id
     * @param userId         用户id
     * @param like           0 取消点赞 1 点赞
     */
    public void shareCommentLike(Integer shareCommentId, Integer userId, Integer like) {
        if (like.equals(1)) {
            Record select = select(
                    commentId.eq(shareCommentId).andEqualCondition(this.userId, userId),
                    getPrimaryKeyName());
            RecordRow recordRow = null;
            if (select.isEmpty()) {
                recordRow = RecordRow.build();
                recordRow.put(commentId, shareCommentId);
                recordRow.put(this.userId, userId);
                recordRow.put(thumbUp, true);
                insert(recordRow);
            } else {
                recordRow = select.get(0);
                recordRow.put(thumbUp, true);
                update(recordRow);
            }
            Record shares = select(Condition.equalCondition("commentId.id", shareCommentId),
                    "id", "commentId.id", "commentId.shareId", "commentId.shareId.id", "commentId.shareId.comment"
                    , "commentId.comment", "commentId.userId");
            if (!shares.isEmpty()) {
                Integer commentUserId = shares.get(0).getRecordRow("commentId").getInteger("userId");
                Integer shareId = shares.get(0).getRecordRow("commentId").getRecordRow("shareId").getInteger("id");
                String comment = shares.get(0).getRecordRow("commentId").getRecordRow("shareId").getString("comment");
                RecordRow content = RecordRow.build();
                content.put("likeId", recordRow.getInteger("id"));
                content.put("likeUserId", userId);
                content.put("likeUserName", petUserService.getUserNickName(userId));
                content.put("likeTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                content.put("comment", shares.get(0).getRecordRow("commentId").getString("comment"));
                content.put("fromType", "pet.share");
                content.put("fromSourceId", shareId);
                content.put("commentId", shareCommentId);


                petNotifyService.sendEventMessage(comment, "pet.share", shareId,
                        userId, commentUserId, NotifyTypeEnum.Like, content);
            }
        } else {
            //取消点赞
            Record select = select(commentId.eq(shareCommentId).andEqualCondition(this.userId, userId),
                    getPrimaryKeyName());
            RecordRow recordRow = null;
            if (!select.isEmpty()) {
                recordRow = select.get(0);
                recordRow.put(thumbUp, false);
                update(recordRow);
            }
        }
    }

    public Integer getCommentUserLike(Integer shareCommentId, Integer userId) {
        Record select = select(commentId.eq(shareCommentId).andEqualCondition(this.userId, userId),
                "id", "thumbUp");
        if (select.isEmpty()) {
            return 0;
        } else {
            return select.get(0).getBoolean(thumbUp) ? 1 : 0;
        }
    }
}
