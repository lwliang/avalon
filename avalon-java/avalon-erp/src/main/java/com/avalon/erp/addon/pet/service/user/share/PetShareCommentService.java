/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.share;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.ObjectUtils;
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
public class PetShareCommentService extends AbstractService {
    private final PetShareCommentLikeService petShareCommentLikeService;
    private final PetShareService petShareService;
    private final PetUserService petUserService;
    private final PetNotifyService petNotifyService;

    public PetShareCommentService(PetShareCommentLikeService petShareCommentLikeService,
                                  @Lazy PetShareService petShareService,
                                  PetUserService petUserService,
                                  PetNotifyService petNotifyService) {
        this.petShareCommentLikeService = petShareCommentLikeService;
        this.petShareService = petShareService;
        this.petUserService = petUserService;
        this.petNotifyService = petNotifyService;
    }

    @Override
    public Field getNameField() {
        return comment;
    }

    @Override
    public String getServiceName() {
        return "pet.share.comment";
    }

    public final Field shareId = Fields.createMany2one("分享id", "pet.share");
    public final Field comment = Fields.createText("评论", true);
    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field image = Fields.createImage("图片");
    public final Field relayShareCommentId = Fields.createMany2one("被回复评论", "pet.share.comment");
    public final Field relayUserId = Fields.createMany2one("回复用户", "pet.user");
    public final Field rootId = Fields.createMany2one("根评论", "pet.share.comment");
    public final Field thumbUp = Fields.createInteger("点赞"); // 点赞数
    public final Field city = Fields.createString("城市");
    public final Field sourceIp = Fields.createString("ip");


    /**
     * 评论点赞
     *
     * @param shareCommentId 评论id
     * @param userId         用户id
     * @param like           0 取消点赞 1 点赞
     */
    public void shareCommentLike(Integer shareCommentId, Integer userId, Integer like) {
        petShareCommentLikeService.shareCommentLike(shareCommentId, userId, like);
        Integer count = petShareCommentLikeService.selectCount(Condition.equalCondition("commentId", shareCommentId)
                .andEqualCondition("thumbUp", true));
        RecordRow row = RecordRow.build();
        row.put(this.thumbUp, count);
        row.put("id", shareCommentId);
        update(row);
    }


    /**
     * 增加评论
     *
     * @param shareId 分享id
     * @param userId  用户id
     * @param comment 评论
     * @param image   图片
     * @return 评论
     */
    public RecordRow addComment(Integer shareId,
                                Integer userId,
                                String comment,
                                String image,
                                Integer relayShareCommentId,
                                String sourceIp,
                                String city) {
        RecordRow row = RecordRow.build();
        row.put(this.shareId, shareId);
        row.put(this.userId, userId);
        row.put(this.comment, comment);
        row.put(this.image, image);
        row.put(this.city, city);
        row.put(this.sourceIp, sourceIp);
        Record shares = petShareService.select(Condition.equalCondition("id", shareId),
                "comment", "userId");

        if (ObjectUtils.isNotNull(relayShareCommentId)) { // 回复评论
            Record select = select(Condition.equalCondition("id", relayShareCommentId),
                    "userId", "rootId");
            RecordRow relayRow = select.get(0);
            row.put(this.relayUserId, relayRow.getInteger("userId"));
            if (relayRow.isNotNull(rootId)) {
                row.put(this.rootId, relayRow.getInteger("rootId"));
            } else {
                row.put(this.rootId, relayShareCommentId);
            }
        }
        PrimaryKey key = insert(row);

        if (ObjectUtils.isNotNull(relayShareCommentId)) {
            if (!shares.isEmpty()) {
                Integer commentUserId = shares.get(0).getInteger("userId");
                String title = shares.get(0).getString("comment");
                RecordRow content = RecordRow.build();
                content.put("relayId", key.getInteger());
                content.put("relay", comment);
                content.put("relayUserId", userId);
                content.put("relayUserName", petUserService.getUserNickName(userId));
                content.put("relayTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                Record select = select(Condition.equalCondition("id", relayShareCommentId), "comment");
                if (!select.isEmpty()) {
                    content.put("comment", select.get(0).getString("comment"));
                } else {
                    content.put("comment", "已删除");
                }
                content.put("fromType", "pet.share");
                content.put("fromSourceId", shareId);
                content.put("parentId", relayShareCommentId);

                petNotifyService.sendEventMessage(title, "pet.share", shareId,
                        userId, commentUserId, NotifyTypeEnum.Relay, content);
            }
        } else {  // 对分享评论
            if (!shares.isEmpty()) {
                Integer commentUserId = shares.get(0).getInteger("userId");
                String title = shares.get(0).getString("comment");

                RecordRow content = RecordRow.build();
                content.put("commentId", key.getInteger());
                content.put("comment", comment);
                content.put("commentUserId", userId);
                content.put("commentUserName", petUserService.getUserNickName(userId));
                content.put("commentTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                content.put("fromType", "pet.share");
                content.put("fromSourceId", shareId);
                petNotifyService.sendEventMessage(title, "pet.share", shareId,
                        userId, commentUserId, NotifyTypeEnum.Comment, content);
            }
        }
        return row;
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    public void deleteComment(Integer commentId) {
        delete(Condition.equalCondition("id", commentId));
        delete(Condition.equalCondition(rootId, commentId));
        petShareCommentLikeService.delete(Condition.equalCondition("commentId", commentId));
    }

    public Record getShareComment(Integer shareId,
                                  Integer pageNum,
                                  Integer pageSize,
                                  Integer userId) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Condition condition = Condition.equalCondition("shareId", shareId);
        condition = condition.andEqualCondition(rootId, null);
        PageInfo createTimeDesc = selectPage(pageParam,
                "createTime desc",
                condition,
                "id",
                "shareId",
                "userId.avatar",
                "userId.id",
                "userId.nickName",
                "comment",
                "image",
                thumbUp.getName(),
                "createTime",
                city.getName());

        getChildCount(createTimeDesc.getData(), userId);

        return createTimeDesc.getData();
    }

    private void getChildCount(Record record, Integer userId) {
        if (record.isEmpty()) return;

        for (RecordRow recordRow : record) {
            Integer id = recordRow.getInteger("id");
            Integer count = selectCount(Condition.equalCondition("rootId", id));
            recordRow.put("childCount", count);
            recordRow.put("isThumbUp", petShareCommentLikeService.getCommentUserLike(id, userId));
        }
    }


    public Record getChildShareComment(Integer rootId,
                                       Integer pageNum,
                                       Integer pageSize,
                                       Integer userId) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        PageInfo pageInfo = selectPage(pageParam,
                "createTime desc",
                Condition.equalCondition("rootId", rootId),
                "id",
                "shareId",
                "userId.id",
                "userId.avatar",
                "userId.nickName",
                "comment",
                "image",
                "thumbUp",
                "relayUserId.id",
                "relayUserId.name",
                "createTime",
                "city");
        getChildCount(pageInfo.getData(), userId);
        return pageInfo.getData();
    }
}
