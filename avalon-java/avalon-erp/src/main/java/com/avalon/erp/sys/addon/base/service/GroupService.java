/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 权限服务，用户组 含权限，菜单，规则, 组织等
 */

@Slf4j
@Service
public class GroupService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.group";
    }

    public final Field description = Fields.createString("描述");
    public final Field parentId = Fields.createMany2one("父组",
            "base.group");
    public final Field parentPath = Fields.createString("父组路径");
    public final Field level = Fields.createInteger("层级");
    public final Field active = Fields.createBoolean("是否激活",
            true, true);
    public final Field userIds = Fields.createMany2many("用户",
            "base.user");
    public final Field ruleIds = Fields.createMany2many("规则",
            "base.rule");
    public final Field menuIds = Fields.createMany2many("菜单",
            "base.menu");
    public final Field serviceAccess = Fields.createOne2many("服务访问",
            "base.service.access", "groupId");

    public final Field orgIds = Fields.createMany2many("组织",
            "base.org");


    /**
     * 判断当前用户是否有该服务的读权限
     *
     * @param serviceName
     * @return
     */
    public Boolean hasReadPermission(String serviceName) {
        Integer userId = getContext().getUserId();
        Condition condition = Condition.equalCondition("userIds.baseUserId", userId);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.serviceId.active", true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.permRead", true);
        Record ids = select("id", condition);
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的写权限
     * @param serviceName
     * @return
     */
    public Boolean hasWritePermission(String serviceName) {
        Integer userId = getContext().getUserId();
        Condition condition = Condition.equalCondition("userIds.baseUserId", userId);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.serviceId.active", true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.permWrite", true);
        Record ids = select("id", condition);
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的删除权限
     * @param serviceName
     * @return
     */
    public Boolean hasUnlinkPermission(String serviceName) {
        Integer userId = getContext().getUserId();
        Condition condition = Condition.equalCondition("userIds.baseUserId", userId);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.serviceId.active", true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.permUnlink", true);
        Record ids = select("id", condition);
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的创建权限
     * @param serviceName
     * @return
     */
    public Boolean hasCreatePermission(String serviceName) {
        Integer userId = getContext().getUserId();
        Condition condition = Condition.equalCondition("userIds.baseUserId", userId);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.serviceId.active", true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.permCreate", true);
        Record ids = select("id", condition);
        return ObjectUtils.isNotEmpty(ids);
    }
}
