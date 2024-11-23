/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.pet;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.addon.pet.model.enums.CheckStateEnum;
import com.avalon.erp.addon.pet.model.enums.PetAdoptStateEnum;
import com.avalon.erp.addon.pet.service.user.PetUserPetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetAdoptService extends AbstractService {
    private PetUserPetService petUserPetService;

    public PetAdoptService(PetUserPetService petUserPetService) {
        this.petUserPetService = petUserPetService;
    }

    @Override
    public String getServiceName() {
        return "pet.adopt";
    }

    @Override
    public Field getNameField() {
        return selfDescription;
    }

    @Override
    public String getLabel() {
        return "宠物转养,领养";
    }

    public final Field petId = Fields.createMany2one("宠物", "pet.user.pet");
    public final Field selfDescription = Fields.createString("自述");
    public final Field publishId = Fields.createMany2one("发布人", "pet.user");

    public final Field avatar = Fields.createImage("首页");
    public final Field video = Fields.createImage("视频");

    public final Field state = Fields.createSelection("状态", PetAdoptStateEnum.class);
    public final Field checkState = Fields.createSelection("审核状态", CheckStateEnum.class, CheckStateEnum.checking);
    public final Field images = Fields.createOne2many("pet.adopt.image", "adoptId");

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (!recordRow.containsKey("state")) {
            recordRow.put("state", PetAdoptStateEnum.adopting);
        }

        if (!recordRow.containsKey(avatar)) {
            if (!recordRow.containsKey(checkState)) {
                recordRow.put(checkState, CheckStateEnum.success);
            }
        }

        if (recordRow.isNotNull(images)) {
            recordRow.put(avatar, recordRow.getRecord(images).get(0).getString("image"));
        }

        PrimaryKey key = super.insert(recordRow);
        petUserPetService.petSending(recordRow.getInteger(petId));
        return key;
    }

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        petUserPetService.petNormal(row.getInteger(petId));
        return super.delete(row);
    }

    /**
     * 成功领养
     *
     * @param id  id
     * @param uid 用户id
     */
    public void successAdopt(Integer id, String uid) {
        RecordRow row = RecordRow.build();
        row.put("id", id);
        row.put(state, PetAdoptStateEnum.success);

        update(row);

        Record select = select(Condition.equalCondition("id", id), "petId");
        if (select.isEmpty()) {
            throw new AvalonException("领养宠物不存在");
        }

        petUserPetService.petNormal(select.get(0).getInteger(petId), uid);
    }
}
