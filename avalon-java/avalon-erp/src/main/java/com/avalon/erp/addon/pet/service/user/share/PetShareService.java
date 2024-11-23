/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.share;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.addon.pet.model.enums.CheckStateEnum;
import com.avalon.erp.addon.pet.model.enums.ShareContentTypeEnum;
import com.avalon.erp.addon.pet.model.enums.SharePrivacyTypeEnum;
import com.avalon.erp.addon.pet.model.enums.ShareTypeEnum;
import com.avalon.erp.addon.pet.service.user.PetFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetShareService extends AbstractService {
    public PetShareService(PetFollowService petFollowService, PetShareCommentService petShareCommentService, PetShareLikeService petShareLikeService) {
        this.petFollowService = petFollowService;
        this.petShareCommentService = petShareCommentService;
        this.petShareLikeService = petShareLikeService;
    }

    @Override
    public String getServiceName() {
        return "pet.share";
    }

    @Override
    public Field getNameField() {
        return comment;
    }

    private final PetFollowService petFollowService;
    private final PetShareCommentService petShareCommentService;
    private final PetShareLikeService petShareLikeService;

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field shareType = Fields.createSelection("分享类型", ShareTypeEnum.class);
    public final Field petIds = Fields.createOne2many("pet.share.pet", "shareId");
    public final Field comment = Fields.createText("内容", true);
    public final Field video = Fields.createString("视频");
    public final Field shareCommentType = Fields.createSelection("分享内容类型", ShareContentTypeEnum.class);
    public final Field images = Fields.createOne2many("pet.share.image", "shareId");
    public final Field sharePrivacyType = Fields.createSelection("隐私类型", SharePrivacyTypeEnum.class);
    public final Field state = Fields.createSelection("审核状态", CheckStateEnum.class);


    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (!recordRow.containsKey(state)) {
            if (recordRow.containsKey(images)) {
                recordRow.put(state, CheckStateEnum.checking);
            } else {
                recordRow.put(state, CheckStateEnum.success);
            }
        }
        return super.insert(recordRow);
    }

    private final String[] fields = new String[]{
            "id", "userId.id", "userId.avatar", "userId.sex", "userId.nickName",
            "shareType", "comment", "video", "shareCommentType", "state",
            "createTime", "images.id", "images.image"};

    /**
     * 获取推荐动态
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    public Record getRecommendShare(Integer pageNum, Integer pageSize, Integer userId, String state, String conditionText) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Condition condition = Condition.equalCondition("sharePrivacyType", SharePrivacyTypeEnum.disClose);
        if (StringUtils.isNotEmpty(state)) {
            condition = condition.andEqualCondition("state", state);
        }
        if (StringUtils.isNotEmpty(conditionText)) {
            condition = condition.andLikeCondition(comment, conditionText);
        }
        PageInfo createTimeDesc = selectPage(pageParam,
                "createTime desc", condition, fields);

        return getLikeCount(getCommentCount(getFollow(createTimeDesc.getData(), userId)), userId);
    }

    /**
     * 获取求助动态
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    public Record getHelpShare(Integer pageNum, Integer pageSize, Integer userId, String state) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Condition condition = Condition.equalCondition("shareType", ShareTypeEnum.help);
        condition = condition.andEqualCondition("sharePrivacyType", SharePrivacyTypeEnum.disClose);
        if (StringUtils.isNotEmpty(state)) {
            condition = condition.andEqualCondition("state", state);
        }
        PageInfo createTimeDesc = selectPage(pageParam, "createTime desc", condition, fields);

        return getLikeCount(getCommentCount(getFollow(createTimeDesc.getData(), userId)), userId);
    }

    /**
     * 获取关注动态
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    public Record getFollowShare(Integer pageNum, Integer pageSize, Integer userId, String state) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Record select = petFollowService.select(Condition.equalCondition("userId", userId), "followId");
        if (select.isEmpty()) {
            return Record.build();
        }

        Condition condition = Condition.inCondition("userId", select.getValues("followId"));
        if (StringUtils.isNotEmpty(state)) {
            condition = condition.andEqualCondition("state", state);
        }
        PageInfo createTimeDesc = selectPage(pageParam, "createTime desc", condition, fields);

        return getLikeCount(getCommentCount(getFollow(createTimeDesc.getData(), userId)), userId);
    }

    public RecordRow getShareDetailById(Integer shareId, Integer userId) {
        Record res = select(Condition.equalCondition(getPrimaryKeyName(), shareId), fields);
        Record likeCount = getLikeCount(getCommentCount(getFollow(res, userId)), userId);

        if (res.isEmpty()) {
            throw new AvalonException("无数据,检查输入参数");
        }

        return likeCount.get(0);
    }

    private Record getFollow(Record result, Integer userId) {
        if (result.isEmpty()) {
            return result;
        }

        for (RecordRow recordRow : result) {
            if (recordRow.isNull("userId")) continue;
            Integer followId = recordRow.getRecordRow("userId").getInteger("id");
            recordRow.put("follow", petFollowService.selectCount(
                    Condition.equalCondition("followId", followId)));
            recordRow.put("isFollow", petFollowService.selectCount(
                    Condition.equalCondition("userId", userId).
                            andEqualCondition("followId", followId)));
        }
        return result;
    }

    private Record getCommentCount(Record result) {
        if (result.isEmpty()) {
            return result;
        }

        for (RecordRow recordRow : result) {
            Integer shareId = recordRow.getInteger("id");
            recordRow.put("commentCount",
                    petShareCommentService.selectCount(Condition.equalCondition("shareId", shareId)));
        }
        return result;
    }

    private Record getLikeCount(Record result, Integer userId) {
        if (result.isEmpty()) {
            return result;
        }

        for (RecordRow recordRow : result) {
            Integer shareId = recordRow.getInteger("id");
            recordRow.put("likeCount",
                    petShareLikeService.selectCount(Condition.equalCondition("shareId", shareId)
                            .andEqualCondition("thumbUp", true)));
            recordRow.put("isLike",
                    petShareLikeService.selectCount(Condition.equalCondition("shareId", shareId)
                            .andEqualCondition("userId", userId)
                            .andEqualCondition("thumbUp", true)));
        }
        return result;
    }

    /**
     * 获取用户的动态
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param curUserId
     * @param sharePrivacyType 公开 或 私有
     * @return
     */
    public Record getUserShare(Integer pageNum,
                               Integer pageSize,
                               Integer userId,
                               Integer curUserId,
                               String sharePrivacyType,
                               String state) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Condition condition = Condition.equalCondition("userId", userId);
        if (StringUtils.isNotEmpty(sharePrivacyType)) {
            condition = condition.andEqualCondition("sharePrivacyType",
                    sharePrivacyType);
        }
        if (StringUtils.isNotEmpty(state)) {
            condition = condition.andEqualCondition("state",
                    state);
        }

        PageInfo createTimeDesc = selectPage(pageParam, "createTime desc",
                condition, fields);

        return getLikeCount(getCommentCount(getFollow(createTimeDesc.getData(), userId)), curUserId);
    }
}
