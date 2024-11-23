/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.pet;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
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
public class PetAdoptMessageService extends AbstractService {
    private final PetUserService petUserService;
    private final PetAdoptService petAdoptService;
    private final PetNotifyService petNotifyService;

    public PetAdoptMessageService(PetUserService petUserService,
                                  @Lazy PetAdoptService petAdoptService,
                                  PetNotifyService petNotifyService) {
        this.petUserService = petUserService;
        this.petAdoptService = petAdoptService;
        this.petNotifyService = petNotifyService;
    }

    @Override
    public String getServiceName() {
        return "pet.adopt.message";
    }

    @Override
    public Field getNameField() {
        return message;
    }

    @Override
    public String getLabel() {
        return "宠物留言";
    }

    public final Field petAdoptId = Fields.createMany2one("宠物领养id",
            "pet.adopt");
    public final Field message = Fields.createString("留言");
    public final Field petUserId = Fields.createMany2one("留言人", "pet.user");
    public final Field parentId = Fields.createMany2one("上级评论", "pet.adopt.message");
    public final Field rootId = Fields.createMany2one("根评论", "pet.adopt.message");

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (recordRow.containsKey(parentId)) {
            Record select = select(Condition.equalCondition("id", recordRow.getInteger(parentId)), "rootId");
            if (!select.isEmpty()) {
                if (select.get(0).isNotNull(rootId)) {
                    recordRow.put(rootId, select.get(0).getInteger(rootId));
                } else {
                    recordRow.put(rootId, recordRow.getInteger(parentId));
                }
            }
        }
        return super.insert(recordRow);
    }

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);

        if (recordRow.containsKey(parentId)) {
            Record adopts = petAdoptService.select(Condition.equalCondition("id",
                            recordRow.getInteger(petAdoptId)),
                    "selfDescription", "publishId");
            if (!adopts.isEmpty()) {
                Integer commentUserId = adopts.get(0).getInteger("publishId");
                String title = adopts.get(0).getString("selfDescription");
                RecordRow content = RecordRow.build();
                content.put("relayId", recordRow.getInteger("id"));
                content.put("relay", recordRow.getString("message"));
                content.put("relayUserId", recordRow.getInteger(petUserId));
                content.put("relayUserName", petUserService.getUserNickName(recordRow.getInteger(petUserId)));
                content.put("relayTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                Record select = select(Condition.equalCondition("id", recordRow.getInteger(parentId)), "message");
                if (!select.isEmpty()) {
                    content.put("comment", select.get(0).getString("message"));
                } else {
                    content.put("comment", "已删除");
                }
                content.put("fromType", "pet.adopt");
                content.put("fromSourceId", recordRow.getInteger(petAdoptId));
                content.put("parentId", recordRow.getInteger(parentId));

                petNotifyService.sendEventMessage(title, "pet.adopt", recordRow.getInteger(petAdoptId),
                        recordRow.getInteger(petUserId), commentUserId, NotifyTypeEnum.Relay, content);
            }
        } else { // 评论
            RecordRow message = RecordRow.build();
            Record adopts = petAdoptService.select(Condition.equalCondition("id", recordRow.getInteger(petAdoptId)),
                    "selfDescription", "publishId");

            if (!adopts.isEmpty()) {
                Integer commentUserId = adopts.get(0).getInteger("publishId");
                String title = adopts.get(0).getString("selfDescription");
                RecordRow content = RecordRow.build();
                content.put("commentId", recordRow.getInteger("id"));
                content.put("comment", recordRow.getString("message"));
                content.put("commentUserId", recordRow.getInteger(petUserId));
                content.put("commentUserName", petUserService.getUserNickName(recordRow.getInteger(petUserId)));
                content.put("commentTime", DateTimeUtils.getCurrentDateTimeString());
                content.put("userId", commentUserId);
                content.put("userName", petUserService.getUserNickName(commentUserId));
                content.put("fromType", "pet.adopt");
                content.put("fromSourceId", recordRow.getInteger(petAdoptId));

                petNotifyService.sendEventMessage(title, "pet.adopt", recordRow.getInteger(petAdoptId),
                        recordRow.getInteger(petUserId), commentUserId, NotifyTypeEnum.Comment, content);
            }
        }
    }

    /**
     * 获取第一级评论分页数据
     *
     * @param petAdoptId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws AvalonException
     */
    public Record selectRootPage(Integer petAdoptId, int pageNum, int pageSize) throws AvalonException {
        PageParam pageParam = new PageParam(pageNum, pageSize);

        PageInfo pageInfo = selectPage(pageParam, "createTime desc",
                Condition.equalCondition(this.petAdoptId, petAdoptId).andEqualCondition(parentId, null),
                "id", "createTime", "message", "petUserId", "petUserId.id", "petUserId.nickName", "petUserId.avatar");

        Record data = pageInfo.getData();

        for (RecordRow datum : data) {
            Integer count = selectCount(Condition.equalCondition(rootId, datum.getInteger("id")));
            datum.put("childCount", count);
        }
        return data;
    }

    /**
     * 获取第一级评论分页数据
     *
     * @param petAdoptId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws AvalonException
     */
    public Record selectChildPage(Integer petAdoptId, Integer rootId, int pageNum, int pageSize) throws AvalonException {
        PageParam pageParam = new PageParam(pageNum, pageSize);

        PageInfo pageInfo = selectPage(pageParam, "createTime asc",
                Condition.equalCondition(this.petAdoptId, petAdoptId).andEqualCondition(this.rootId, rootId),
                "id", "message", "petUserId", "petUserId.id", "petUserId.nickName", "createTime", "petUserId.avatar",
                "parentId.id", "parentId.petUserId.id", "parentId.petUserId.nickName", "parentId.petUserId.avatar");

        return pageInfo.getData();
    }

    /**
     * 获取评论总数
     *
     * @param petAdoptId
     * @return
     * @throws AvalonException
     */
    public int selectMessageCount(Integer petAdoptId) throws AvalonException {
        return selectCount(Condition.equalCondition(this.petAdoptId, petAdoptId));
    }


}
