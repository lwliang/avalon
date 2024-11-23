/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.redis.IRedisLock;
import com.avalon.core.redis.RedisCommon;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.addon.im.service.ImService;
import com.avalon.erp.addon.pet.model.enums.UserSexEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetUserService extends AbstractService {
    public PetUserService(RedisCommon redisCommon, ImService imService) {
        this.redisCommon = redisCommon;
        this.imService = imService;
    }


    private String company = "pet";
    private String app = "pet";

    @Override
    public String getServiceName() {
        return "pet.user";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("用户名");
    }

    @Override
    public Field getNameField() {
        return nickName;
    }

    protected final Field nickName = Fields.createString("昵称");
    protected final Field phone = Fields.createString("手机号"); // 作为账号使用
    public final Field countryCode = Fields.createString("国家区号");
    protected final Field sex = Fields.createSelection("性别", UserSexEnum.class);
    protected final Field birthday = Fields.createDate("生日");
    protected final Field avatar = Fields.createImage("头像");
    public final Field password = Fields.createPasswordField("密码");

    public final Field openId = Fields.createString("微信id");
    public final Field unionId = Fields.createString("微信统一id");
    public final Field uid = Fields.createString("uid", true);
    public final Field imId = Fields.createInteger("imKey");

    protected final Field pets = Fields.createOne2many("宠物列表",
            "pet.user.pet",
            "petUserId");

    protected final Field address = Fields.createOne2many("地址列表",
            "pet.address",
            "petUserId");

    private final RedisCommon redisCommon;

    private final ImService imService;

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);

        Integer imKey = imService.registerIm(company, app,
                recordRow.getInteger("id"));
        RecordRow newBuild = RecordRow.build();
        newBuild.put("id", recordRow.getInteger("id"));
        newBuild.put("imId", imKey);
        update(newBuild);
    }

    @Override
    protected void checkBeforeInsert(RecordRow recordRow) throws AvalonException {
        super.checkBeforeInsert(recordRow);

        if (!recordRow.containsKey("name") && recordRow.containsKey(phone)) {
            recordRow.put(getNameField(), recordRow.get(phone));
        }

        Integer nextUid = getNextUid();
        if (ObjectUtils.isNull(nextUid)) {
            throw new AvalonException("获取uid失败");
        }
        recordRow.put(uid, nextUid);

        if (!recordRow.containsKey(nickName)) {
            recordRow.put(nickName, "萌宝" + nextUid);
        }
        if (!recordRow.containsKey(avatar)) {
            recordRow.put(avatar,
                    "/file/down/2024/06/a62e79c6084e4faa89be4b0b90e54730.png");
        }
    }


    public RecordRow getOrCreateUser(String openId) {
        log.info("getOrCreateUser: openId={}", openId);
        Record select = select(this.openId.eq(openId), "openId", "id");
        RecordRow recordRow = null;
        if (select.isEmpty()) {
            recordRow = RecordRow.build();
            recordRow.put(this.openId, openId);
            recordRow.put("id", insert(recordRow));
        } else {
            recordRow = select.get(0);
        }
        return recordRow;
    }

    private Integer getNextUid() {
        IRedisLock uidLock = redisCommon.getLock("uid_lock");
        Boolean b = uidLock.tryLock(2);
        if (b) {
            Object uid = redisCommon.get("uid");
            if (ObjectUtils.isNull(uid)) {
                String s = getContext().getJdbcTemplate().executeScalar("select max(uid) from pet_user", String.class);
                if (ObjectUtils.isEmpty(s)) {
                    uid = 10000;
                } else {
                    uid = Integer.parseInt(s);
                }
            }
            Integer nextUid = Integer.parseInt(uid.toString()) + 1;
            redisCommon.set("uid", nextUid.toString());
            return nextUid;
        }
        return null;
    }

    public Integer getImUserId(Integer userId) {
        Record select = select(Condition.equalCondition("id", userId), "imId");

        if (select.isEmpty()) {
            return null;
        }
        return select.get(0).getInteger("imId");
    }

    public String getUserNickName(Integer userId) {
        Record select = select(Condition.equalCondition("id", userId), "nickName");

        if (select.isEmpty()) {
            return null;
        }
        return select.get(0).getString("nickName");
    }
}
