/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.enums.ServiceOperateEnum;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.PermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.sys.addon.base.model.enums.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务，用户组 含权限，菜单，规则, 组织等
 */

@Slf4j
@Service
public class GroupService extends AbstractTreeService {
    private final String[] noNeedPermissionService = new String[]{ // 读取数据 不需要权限
            "base.action.view",
            "base.action.window",
            "base.action.window.view",
            "base.service.data",
            "base.field",
            "base.group",
            "base.menu",
            "base.rule",
            "base.module",
            "base.service.log",
            "base.service",
            "base.service.access",
            "base.user"
    };

    public GroupService() {
    }

    @Override
    public String getServiceName() {
        return "base.group";
    }

    public final Field description = Fields.createString("描述");
    public final Field active = Fields.createBoolean("启用",
            true, true);
    public final Field userIds = Fields.createMany2many("用户",
            "base.user", "base.group.user.ref",
            "groupId", "userId");
    public final Field ruleIds = Fields.createMany2many("规则",
            "base.rule", "base.group.rule.ref",
            "groupId", "ruleId");
    public final Field menuIds = Fields.createMany2many("菜单",
            "base.menu", "base.group.menu.ref",
            "groupId", "menuId");
    public final Field serviceAccess = Fields.createOne2many("服务访问",
            "base.service.access", "groupId");

    /**
     * 新增用户
     *
     * @param groupId 权限组id
     * @param userId  用户id
     */
    public void addGroupUser(Integer groupId, Integer userId) {
        RecordRow row = RecordRow.build();
        row.put("id", groupId);
        RecordRow userRow = RecordRow.build();
        userRow.put("userId", userId);
        userRow.put("groupId", groupId);
        userRow.put(OPERATE, ServiceOperateEnum.insert);
        Record userRows = Record.build();
        userRows.add(userRow);
        row.put("userIds", userRows);
        update(row);
    }

    /**
     * 获取 用户所在组总，可以访问的模块的id
     *
     * @param userId 用户
     * @return 模块id
     */
    public List<Integer> getPermissionModule(Integer userId) {
        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);
        Record moduleIds = select(condition, "serviceAccess.serviceId.moduleId");
        return moduleIds.stream().flatMap(service -> service.getRecord("serviceAccess").stream())
                .map(service -> service.getRecordRow("serviceId").getInteger("moduleId"))
                .collect(Collectors.toList());
    }

    public List<Integer> getPermissionMenu(Integer userId) {
        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);

        Record serviceIds = select(condition, "serviceAccess.serviceId");
        List<Integer> serviceIdList = serviceIds.stream().flatMap(service -> service.getRecord("serviceAccess").stream())
                .map(service -> service.getInteger("serviceId"))
                .collect(Collectors.toList());

        Condition menuCondition = Condition.equalCondition("type", ActionTypeEnum.action);
        if (!serviceIdList.isEmpty()) { // 如果存在服务id
            menuCondition = menuCondition.andInCondition("action.serviceId", (List<?>) serviceIdList);
        } else { // 如果不存在服务id
            menuCondition = menuCondition.andInCondition("action.serviceId", 0);
        }
        Record menuIds = getServiceBean("base.menu").select(menuCondition, "id"); // 获取模型对应的菜单id

        Record menuIdsRecord = select(condition, "menuIds.menuId"); // 获取权限下的菜单
        List<Integer> menuIdsPer = menuIdsRecord.stream().flatMap(row -> row.getRecord("menuIds").stream())
                .map(row -> row.getInteger("menuId"))
                .collect(Collectors.toList());
        for (RecordRow menuId : menuIds) {
            menuIdsPer.add(menuId.getInteger("id"));
        }

        return menuIdsPer;
    }

    /**
     * 判断当前用户是否有该服务的读权限
     *
     * @param serviceName
     * @return
     */
    @TemporaryElevate(ElevatePermissionEnum.permission)
    public Boolean hasReadPermission(Integer userId, String serviceName) {
        if (userId.equals(SystemConstant.ADMIN)) {
            return true;
        }

        boolean b = Arrays.asList(noNeedPermissionService).contains(serviceName); // 基本服务不需要权限
        if (b) {
            return true;
        }

        Integer i = getServiceBean("base.service").selectCount(Condition.equalCondition("name", serviceName));
        if (i <= 0) { // 是many2many字段生成的动态表
            return true;
        }

        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.permRead", true);
        Record ids = select(condition, "id");
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的写权限
     *
     * @param serviceName
     * @return
     */
    @TemporaryElevate(ElevatePermissionEnum.permission)
    public Boolean hasWritePermission(Integer userId, String serviceName) {
        if (userId.equals(SystemConstant.ADMIN)) {
            return true;
        }
        Integer i = getServiceBean("base.service").selectCount(Condition.equalCondition("name", serviceName));
        if (i <= 0) { // 是many2many字段生成的动态表
            return true;
        }
        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.permWrite", true);
        Record ids = select(condition, "id");
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的删除权限
     *
     * @param serviceName
     * @return
     */
    @TemporaryElevate(ElevatePermissionEnum.permission)
    public Boolean hasUnlinkPermission(Integer userId, String serviceName) {
        if (userId.equals(SystemConstant.ADMIN)) {
            return true;
        }
        Integer i = getServiceBean("base.service").selectCount(Condition.equalCondition("name", serviceName));
        if (i <= 0) { // 是many2many字段生成的动态表
            return true;
        }
        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.permUnlink", true);
        Record ids = select(condition, "id");
        return ObjectUtils.isNotEmpty(ids);
    }

    /**
     * 判断当前用户是否有该服务的创建权限
     *
     * @param serviceName
     * @return
     */
    @TemporaryElevate(ElevatePermissionEnum.permission)
    public Boolean hasCreatePermission(Integer userId, String serviceName) {
        if (userId.equals(SystemConstant.ADMIN)) {
            return true;
        }
        Integer i = getServiceBean("base.service").selectCount(Condition.equalCondition("name", serviceName));
        if (i <= 0) { // 是many2many字段生成的动态表
            return true;
        }
        Condition condition = Condition.equalCondition("userIds.userId", userId);
        condition = condition.andEqualCondition(active, true);
        condition = condition.andEqualCondition("serviceAccess.serviceId.name", serviceName);
        condition = condition.andEqualCondition("serviceAccess.permCreate", true);
        Record ids = select(condition, "id");
        return ObjectUtils.isNotEmpty(ids);
    }

    @TemporaryElevate(ElevatePermissionEnum.recordRule)
    public Condition getReadRecordRule(Integer userId, String service) {
        return getRecordRule(userId, service, PermissionEnum.read);
    }

    @TemporaryElevate(ElevatePermissionEnum.recordRule)
    public Condition getWriteRecordRule(Integer userId, String service) {
        return getRecordRule(userId, service, PermissionEnum.write);
    }

    @TemporaryElevate(ElevatePermissionEnum.recordRule)
    public Condition getCreateRecordRule(Integer userId, String service) {
        return getRecordRule(userId, service, PermissionEnum.create);
    }

    @TemporaryElevate(ElevatePermissionEnum.recordRule)
    public Condition getUnlinkRecordRule(Integer userId, String service) {
        return getRecordRule(userId, service, PermissionEnum.unlink);
    }

    protected Condition getRecordRule(Integer userId, String service, PermissionEnum operation) {
        if (userId.equals(SystemConstant.ADMIN)) {
            return null;
        }

        Condition userRuleCondition = Condition.equalCondition("userIds.userId", userId)
                .andEqualCondition("ruleIds.ruleId.serviceId.name", service);
        userRuleCondition = userRuleCondition.andEqualCondition("ruleIds.ruleId.active", true);
        if (operation == PermissionEnum.read) {
            userRuleCondition = userRuleCondition.andEqualCondition("ruleIds.ruleId.permRead", true);
        } else if (operation == PermissionEnum.write) {
            userRuleCondition = userRuleCondition.andEqualCondition("ruleIds.ruleId.permWrite", true);
        } else if (operation == PermissionEnum.create) {
            userRuleCondition = userRuleCondition.andEqualCondition("ruleIds.ruleId.permCreate", true);
        } else if (operation == PermissionEnum.unlink) {
            userRuleCondition = userRuleCondition.andEqualCondition("ruleIds.ruleId.permUnlink", true);
        }
        Record rules = select(userRuleCondition, "ruleIds.ruleId.domainForce");
        Condition condition = null;

        // 获取用户组下记录规则
        for (RecordRow rule : rules) {
            Record ruleIds1 = rule.getRecord("ruleIds");
            for (RecordRow recordRow : ruleIds1) {
                String domainForce = recordRow.getRecordRow("ruleId")
                        .getString("domainForce");
                if (ObjectUtils.isNull(condition)) {
                    condition = getCondition(domainForce);
                } else {
                    condition = condition.orCondition(getCondition(domainForce));
                }
            }
        }

        Condition globalCondition = Condition.equalCondition("global", true);
        globalCondition = globalCondition.andEqualCondition("active", true);
        globalCondition = globalCondition.andEqualCondition("serviceId.name", service);
        if (operation == PermissionEnum.read) {
            globalCondition = globalCondition.andEqualCondition("permRead", true);
        } else if (operation == PermissionEnum.write) {
            globalCondition = globalCondition.andEqualCondition("permWrite", true);
        } else if (operation == PermissionEnum.create) {
            globalCondition = globalCondition.andEqualCondition("permCreate", true);
        } else if (operation == PermissionEnum.unlink) {
            globalCondition = globalCondition.andEqualCondition("permUnlink", true);
        }
        // 获取全局规则
        Record globalRules = getContext().getServiceBean("base.rule").select(globalCondition,
                "domainForce");

        for (RecordRow globalRule : globalRules) {
            String domainForce = globalRule.getString("domainForce");
            if (ObjectUtils.isNull(condition)) {
                condition = getCondition(domainForce);
            } else {
                condition = condition.orCondition(getCondition(domainForce));
            }
        }

        return condition;
    }
}
