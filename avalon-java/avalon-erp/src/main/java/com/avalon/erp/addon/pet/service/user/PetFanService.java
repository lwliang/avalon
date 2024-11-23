/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetFanService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.fan";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户id", "pet.user");
    public final Field fanId = Fields.createMany2one("粉丝id", "pet.user");

    /**
     * 增加粉丝
     *
     * @param userId 当前用户
     * @param fanId  粉丝
     */
    public void addFan(Integer userId, Integer fanId) {
        Integer count = selectCount(this.userId.eq(userId).andEqualCondition(this.fanId, fanId));

        if (count == 0) {
            RecordRow row = RecordRow.build();
            row.put(this.userId, userId);
            row.put(this.fanId, fanId);
            insert(row);
        }
    }

    /**
     * 删除粉丝
     *
     * @param userId
     * @param fanId
     */
    public void deleteFan(Integer userId, Integer fanId) {
        delete(this.userId.eq(userId).andEqualCondition(this.fanId, fanId));
    }
}
