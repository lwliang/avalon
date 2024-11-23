/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.addon.pet.model.enums.PetAdoptStateEnum;
import com.avalon.erp.addon.pet.model.enums.PetSexEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetUserPetService extends AbstractService {
    private final PetUserService petUserService;

    public PetUserPetService(PetUserService petUserService) {
        this.petUserService = petUserService;
    }

    @Override
    public String getServiceName() {
        return "pet.user.pet";
    }

    @Override
    public String getLabel() {
        return "用户宠物";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("宠物名");
    }

    public final Field petUserId = Fields.createMany2one("用户",
            "pet.user");

    public final Field petSmallTypeId = Fields.createMany2one("宠物小类别",
            "pet.small.type");

    public final Field petCategoryId = Fields.createMany2one("宠物类别",
            "pet.big.type");
    public final Field sex = Fields.createSelection("性别", PetSexEnum.class);

    public final Field birthday = Fields.createDate("生日");
    public final Field avatar = Fields.createImage("头像");
    public final Field hairColor = Fields.createString("发色");
    public final Field weight = Fields.createString("体重");
    public final Field sterilization = Fields.createBoolean("绝育");

    public final Field state = Fields.createSelection("状态", PetAdoptStateEnum.class);

    public final Field dewormIds = Fields.createOne2many("驱虫",
            "pet.pet.deworm", "petId");
    public final Field vaccinationIds = Fields.createOne2many("疫苗",
            "pet.pet.vaccination", "petId");

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (!recordRow.containsKey(state)) {
            recordRow.put(state, PetAdoptStateEnum.raise);
        }
        return super.insert(recordRow);
    }

    public void petSending(Integer petId) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("id", petId);
        recordRow.put(state, PetAdoptStateEnum.sending);
        update(recordRow);
    }

    public void petNormal(Integer petId) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("id", petId);
        recordRow.put(state, PetAdoptStateEnum.raise);
        update(recordRow);
    }

    public void petNormal(Integer petId, String uid) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("id", petId);
        recordRow.put(state, PetAdoptStateEnum.raise);
        Record select = petUserService.select(Condition.equalCondition("uid", uid), "id");
        if (select.isEmpty()) {
            throw new AvalonException("用户Id不存在");
        }
        recordRow.put("petUserId", select.get(0).get("id"));
        update(recordRow);
    }

    /**
     * 获取用户宠物列表
     *
     * @param userId
     * @return
     */
    public Record getUserPets(Integer userId, String state) {
        Condition condition = petUserId.eq(userId);
        if (StringUtils.isNotEmpty(state)) {
            condition = condition.andEqualCondition("state", state);
        }
        return select("id asc",
                condition,
                "id", "name", "petCategoryId.name", "petSmallTypeId.name", "avatar");
    }
}
