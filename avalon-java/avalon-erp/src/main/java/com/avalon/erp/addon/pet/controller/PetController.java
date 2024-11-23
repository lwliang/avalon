/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.HttpRequestUtils;
import com.avalon.erp.addon.pet.model.AdoptServiceModelPage;
import com.avalon.erp.addon.pet.service.shop.UserCartService;
import com.avalon.erp.addon.pet.service.shop.commodity.PetCommodityService;
import com.avalon.erp.addon.pet.service.user.*;
import com.avalon.erp.addon.pet.service.user.account.PetAccountService;
import com.avalon.erp.addon.pet.service.user.pet.PetAdoptLikeService;
import com.avalon.erp.addon.pet.service.user.pet.PetAdoptMessageService;
import com.avalon.erp.addon.pet.service.user.pet.PetAdoptService;
import com.avalon.erp.addon.pet.service.user.share.PetShareCommentService;
import com.avalon.erp.addon.pet.service.user.share.PetShareLikeService;
import com.avalon.erp.addon.pet.service.user.share.PetShareService;
import com.avalon.erp.sys.addon.base.model.ServiceModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    protected HttpServletRequest request;
    private final PetUserService petUserService;
    private final PetCommodityService petCommodityService;
    private final UserCartService userCartService;
    private final PetUserPetService petUserPetService;
    private final PetFriendService petFriendService;
    private final PetFanService petFanService;
    private final PetFollowService petFollowService;
    private final PetShareService petShareService;

    private final PetShareLikeService petShareLikeService;

    private final PetShareCommentService petShareCommentService;

    private final PetAccountService petAccountService;

    private final PetAdoptService petAdoptService;
    private final PetAdoptLikeService petAdoptLikeService;
    private final PetAdoptMessageService petAdoptMessageService;
    private final PetNotifyService petNotifyService;

    @Autowired
    public PetController(PetUserService petUserService,
                         PetCommodityService petCommodityService,
                         UserCartService userCartService,
                         PetUserPetService petUserPetService,
                         PetFriendService petFriendService,
                         PetFanService petFanService,
                         PetFollowService petFollowService,
                         PetShareService petShareService,
                         PetShareLikeService petShareLikeService,
                         PetShareCommentService petShareCommentService,
                         PetAccountService petAccountService,
                         PetAdoptService petAdoptService,
                         PetAdoptLikeService petAdoptLikeService,
                         PetAdoptMessageService petAdoptMessageService,
                         PetNotifyService petNotifyService) {
        this.petUserService = petUserService;
        this.petCommodityService = petCommodityService;
        this.userCartService = userCartService;
        this.petUserPetService = petUserPetService;
        this.petFriendService = petFriendService;
        this.petFanService = petFanService;
        this.petFollowService = petFollowService;
        this.petShareService = petShareService;
        this.petShareLikeService = petShareLikeService;
        this.petShareCommentService = petShareCommentService;
        this.petAccountService = petAccountService;
        this.petAdoptService = petAdoptService;
        this.petAdoptLikeService = petAdoptLikeService;
        this.petAdoptMessageService = petAdoptMessageService;
        this.petNotifyService = petNotifyService;
    }

    @PostMapping("/get/user")
    public RecordRow getOrCreateUser(@RequestBody Map<String, Object> param) {
        String openId = "";
        if (param.containsKey("openId")) {
            openId = (String) param.get("openId");
        }
        return petUserService.getOrCreateUser(openId);
    }

    @PostMapping("/commodity/page")
    public Record getFirstProduct(@RequestBody PageParam pageParam) {
        return petCommodityService.getFirstProduct(pageParam.getPageNum(), pageParam.getPageSize());
    }

    @PostMapping("commodity/category/list")
    public Record getProductCategoryPage(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer petBigTypeId = 0;
        Integer productCategoryId = 0;
        String order = "";
        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("petBigTypeId")) {
            petBigTypeId = (Integer) param.get("petBigTypeId");
        }
        if (param.containsKey("productCategoryId")) {
            productCategoryId = (Integer) param.get("productCategoryId");
        }
        if (param.containsKey("order")) {
            order = (String) param.get("order");
        }
        return petCommodityService.getProductCategoryPage(
                pageNum,
                pageSize,
                petBigTypeId,
                productCategoryId,
                order
        );
    }

    @PostMapping("/cart/add")
    public void addCartProduct(@RequestBody Map<String, Object> param) {
        Integer productId = 0;
        Integer userId = 0;
        Integer count = 0;
        if (param.containsKey("productId")) {
            productId = (Integer) param.get("productId");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        if (param.containsKey("count")) {
            count = (Integer) param.get("count");
        }
        if (productId.equals(0) || userId.equals(0)) {
            return;
        }
        userCartService.addCartProduct(productId, userId, count);
    }

    @PostMapping("/cart/get")
    public Record getCartProductByUserId(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        return userCartService.getCartProductByUserId(userId);
    }

    /**
     * 获取全部用户宠物列表
     *
     * @param param
     * @return
     */
    @PostMapping("get/user/pets")
    public Record getUserPets(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        String state = null;
        if (param.containsKey("state")) {
            state = param.get("state").toString();
        }
        return petUserPetService.getUserPets(userId, state);
    }

    @PostMapping("get/user/thing/count")
    public RecordRow getUserFanAndFollowAndFriendCount(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        RecordRow row = RecordRow.build();
        row.put("friend",
                petFriendService.selectCount(Condition.equalCondition("userId", userId)));
        row.put("fan",
                petFriendService.selectCount(Condition.equalCondition("userId", userId)));
        row.put("follow",
                petFriendService.selectCount(Condition.equalCondition("userId", userId)));
        row.put("like", 0);

        return row;
    }


    @PostMapping("get/recommend/share")
    public Record getRecommendShare(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;

        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        String state = null;
        if (param.containsKey("state")) {
            state = param.get("state").toString();
        }

        String condition = "";
        if (param.containsKey("condition")) {
            condition = param.get("condition").toString();
        }

        return petShareService.getRecommendShare(pageNum, pageSize, userId, state, condition);
    }

    @PostMapping("get/help/share")
    public Record getHelpShare(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;
        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        String state = null;
        if (param.containsKey("state")) {
            state = param.get("state").toString();
        }
        return petShareService.getHelpShare(pageNum, pageSize, userId, state);
    }

    @PostMapping("get/follow/share")
    public Record getFanShare(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;
        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        String state = null;
        if (param.containsKey("state")) {
            state = param.get("state").toString();
        }
        return petShareService.getFollowShare(pageNum, pageSize, userId, state);
    }

    @PostMapping("get/share/detail")
    public RecordRow getShareDetailById(@RequestBody Map<String, Object> param) {
        Integer shareId = 0;
        if (param.containsKey("shareId")) {
            shareId = (Integer) param.get("shareId");
        }

        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }

        if (shareId == 0 || userId == 0) {
            throw new AvalonException("未输入参数id");
        }
        return petShareService.getShareDetailById(shareId, userId);
    }

    @PostMapping("get/user/share")
    public Record getUserShare(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;
        String sharePrivacyType = "";
        String state = "";
        Integer curUserId = 0;
        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        if (param.containsKey("curUserId")) {
            curUserId = (Integer) param.get("curUserId");
        }
        if (param.containsKey("sharePrivacyType")) {
            sharePrivacyType = param.get("sharePrivacyType").toString();
        }
        if (param.containsKey("state")) {
            state = param.get("state").toString();
        }
        return petShareService.getUserShare(pageNum, pageSize, userId, curUserId, sharePrivacyType, state);
    }

    @PostMapping("like/share")
    public void likeShare(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        Integer shareId = 0;
        Integer thumbUp = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        if (param.containsKey("shareId")) {
            shareId = (Integer) param.get("shareId");
        }
        if (param.containsKey("thumbUp")) {
            thumbUp = (Integer) param.get("thumbUp");
        }
        petShareLikeService.like(shareId, userId, thumbUp);
    }

    @PostMapping("add/user/follow")
    public void addFollow(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }

        Integer followId = 0;

        if (param.containsKey("followId")) {
            followId = (Integer) param.get("followId");
        }

        if (userId == 0 || followId == 0) {
            throw new AvalonException("未输入userId,followId参数");
        }

        petFollowService.addFollow(userId, followId);
    }

    @PostMapping("delete/user/follow")
    public void deleteFollow(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }

        Integer followId = 0;

        if (param.containsKey("followId")) {
            followId = (Integer) param.get("followId");
        }

        if (userId == 0 || followId == 0) {
            throw new AvalonException("未输入userId,followId参数");
        }

        petFollowService.deleteFollow(userId, followId);
    }

    @PostMapping("share/comment/like")
    public void shareCommentLike(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }

        Integer commentId = 0;

        if (param.containsKey("commentId")) {
            commentId = (Integer) param.get("commentId");
        }

        Integer like = -1;
        if (param.containsKey("like")) {
            like = (Integer) param.get("like");
        }
        petShareCommentService.shareCommentLike(commentId, userId, like);
    }

    @PostMapping("/share/comment/add")
    public RecordRow addComment(@RequestBody Map<String, Object> param) {
        Integer shareId = 0;
        Integer userId = 0;
        String comment = "";
        String image = "";
        Integer relayShareCommentId = null;
        if (param.containsKey("shareId")) {
            shareId = (Integer) param.get("shareId");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        if (param.containsKey("comment")) {
            comment = (String) param.get("comment");
        }
        if (param.containsKey("image")) {
            image = (String) param.get("image");
        }
        if (param.containsKey("relayShareCommentId")) {
            relayShareCommentId = (Integer) param.get("relayShareCommentId");
        }
        String ipAddress = HttpRequestUtils.getIpAddress(request);

        return petShareCommentService.addComment(shareId,
                userId,
                comment,
                image,
                relayShareCommentId,
                ipAddress,
                "");
    }

    @PostMapping("/share/comment/delete")
    public void deleteComment(@RequestBody Map<String, Object> param) {
        Integer commentId = 0;
        if (param.containsKey("commentId")) {
            commentId = (Integer) param.get("commentId");
        }
        petShareCommentService.deleteComment(commentId);
    }

    /**
     * 获取分享评论
     *
     * @param param
     * @return
     */
    @PostMapping("/share/comment/get")
    public Record getShareComment(@RequestBody Map<String, Object> param) {
        Integer shareId = 0;
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;
        if (param.containsKey("shareId")) {
            shareId = (Integer) param.get("shareId");
        }
        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        return petShareCommentService.getShareComment(shareId, pageNum, pageSize, userId);
    }

    @PostMapping("/share/comment/child/get")
    public Record getChildShareComment(@RequestBody Map<String, Object> param) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userId = 0;
        Integer rootId = 0;

        if (param.containsKey("pageNum")) {
            pageNum = (Integer) param.get("pageNum");
        }
        if (param.containsKey("pageSize")) {
            pageSize = (Integer) param.get("pageSize");
        }
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        if (param.containsKey("rootId")) {
            rootId = (Integer) param.get("rootId");
        }
        return petShareCommentService.getChildShareComment(rootId,
                pageNum,
                pageSize,
                userId);
    }

    @PostMapping("/get/account/sum")
    public RecordRow getSumAccount(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        Integer year = null;
        if (param.containsKey("year")) {
            year = (Integer) param.get("year");
        }
        Integer month = null;
        if (param.containsKey("month")) {
            month = (Integer) param.get("month");
        }
        return petAccountService.getSum(userId, year, month);
    }

    @PostMapping("/adopt/success")
    public void successAdopt(@RequestBody Map<String, Object> param) {
        Integer id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }

        String uid = "";
        if (param.containsKey("uid")) {
            uid = param.get("uid").toString();
        }

        petAdoptService.successAdopt(id, uid);
    }

    @PostMapping("/adopt/like")
    public void adoptLike(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        Integer adoptId = 0;
        if (param.containsKey("adoptId")) {
            adoptId = (Integer) param.get("adoptId");
        }
        petAdoptLikeService.like(adoptId, userId);
    }

    @PostMapping("/adopt/unlike")
    public void adoptUnlike(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = (Integer) param.get("userId");
        }
        Integer adoptId = 0;
        if (param.containsKey("adoptId")) {
            adoptId = (Integer) param.get("adoptId");
        }
        petAdoptLikeService.unlike(adoptId, userId);
    }

    @PostMapping("/get/adopt/list")
    public PageInfo getAdoptList(@RequestBody AdoptServiceModelPage serviceConditionPage) {
        PageInfo pageInfo = petAdoptService.selectPage(serviceConditionPage.getPage(),
                serviceConditionPage.getOrder(),
                Condition.parseRPN(serviceConditionPage.getRpnCondition()),
                FieldUtils.getFieldList(serviceConditionPage.getFields()).toArray(new String[0]));

        Record data = pageInfo.getData();
        for (RecordRow datum : data) {
            Condition condition = Condition.equalCondition("petAdoptId", datum.getInteger("id"));
            Integer isLike = petAdoptLikeService.selectCount(condition.andEqualCondition("isLike", true));
            datum.put("likeCount", isLike);
            condition = condition.andEqualCondition("petUserId", serviceConditionPage.getUserId());
            condition = condition.andEqualCondition("isLike", true);
            Integer i = petAdoptLikeService.selectCount(condition);
            datum.put("userLike", i);
        }

        return pageInfo;
    }

    @PostMapping("/get/adopt/message/count")
    public int getAdoptMessageCount(@RequestBody Map<String, Object> param) {
        Integer adoptId = 0;
        if (param.containsKey("adoptId")) {
            adoptId = Integer.parseInt(param.get("adoptId").toString());
        }
        return petAdoptMessageService.selectMessageCount(adoptId);
    }

    @PostMapping("/get/adopt/message/root")
    public Record getAdoptRootMessage(@RequestBody Map<String, Object> param) {
        Integer adoptId = 0;
        if (param.containsKey("adoptId")) {
            adoptId = Integer.parseInt(param.get("adoptId").toString());
        }

        Integer pageNum = 1;
        if (param.containsKey("pageNum")) {
            pageNum = Integer.parseInt(param.get("pageNum").toString());
        }

        Integer pageSize = 1;
        if (param.containsKey("pageSize")) {
            pageSize = Integer.parseInt(param.get("pageSize").toString());
        }

        return petAdoptMessageService.selectRootPage(adoptId, pageNum, pageSize);
    }

    @PostMapping("/get/adopt/message/child")
    public Record getAdoptChildMessage(@RequestBody Map<String, Object> param) {
        Integer adoptId = 0;
        if (param.containsKey("adoptId")) {
            adoptId = Integer.parseInt(param.get("adoptId").toString());
        }

        Integer rootId = 0;
        if (param.containsKey("rootId")) {
            rootId = Integer.parseInt(param.get("rootId").toString());
        }

        Integer pageNum = 1;
        if (param.containsKey("pageNum")) {
            pageNum = Integer.parseInt(param.get("pageNum").toString());
        }

        Integer pageSize = 1;
        if (param.containsKey("pageSize")) {
            pageSize = Integer.parseInt(param.get("pageSize").toString());
        }

        return petAdoptMessageService.selectChildPage(adoptId, rootId, pageNum, pageSize);
    }

    @PostMapping("/get/user/notify/unread/count")
    public RecordRow getEventUnreadCount(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = Integer.parseInt(param.get("userId").toString());
        }

        Integer unreadCount = petNotifyService.getUnreadCount(userId);

        RecordRow result = RecordRow.build();

        result.put("count", unreadCount);

        return result;
    }

    @PostMapping("/get/user/last/notify")
    public Record getUserLastNotify(@RequestBody Map<String, Object> param) {
        Integer userId = 0;
        if (param.containsKey("userId")) {
            userId = Integer.parseInt(param.get("userId").toString());
        }
        return petNotifyService.getLastNotify(userId);
    }
}
