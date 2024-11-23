/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user.remind;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.field.RelationField;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.addon.pet.model.enums.RemindOftenEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetRemindService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.remind";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("提醒");
    }

    public final Field petIds = Fields.createMany2many("宠物", "pet.user.pet");
    public final Field remindDate = Fields.createDate("提醒日期");
    public final Field remindTime = Fields.createTime("提醒时间");
    public final Field often = Fields.createSelection("提醒次数", RemindOftenEnum.class);
    public final Field done = Fields.createBoolean("完成", false, false);
    public final Field userId = Fields.createMany2one("宠物主", "pet.user");

    @Override
    protected void checkBeforeUpdate(RecordRow recordRow) throws AvalonException {
        super.checkBeforeUpdate(recordRow);
    }
}
