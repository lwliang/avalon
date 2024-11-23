/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.aspect;

import com.avalon.core.context.Context;
import com.avalon.core.exception.PermissionException;
import com.avalon.core.permission.PermissionEnum;
import com.avalon.core.permission.RequiresPermission;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.base.service.GroupService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {
    private final Context context;
    private final GroupService groupService;

    public PermissionAspect(Context context,
                            GroupService groupService) {
        this.context = context;
        this.groupService = groupService;
    }

    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        PermissionEnum[] value = requiresPermission.value();
        Object targetBean = joinPoint.getTarget();
        if (!(targetBean instanceof AbstractService)) { // 不在AbstractService实例，直接返回
            return;
        }
        AbstractService service = (AbstractService) targetBean;
        for (PermissionEnum permissionEnum : value) {
            switch (permissionEnum) {
                case read -> {
                    if (!hasReadPermission(service.getServiceName())) {
                        throw new PermissionException(
                                String.format("没有模型%s读取权限", service.getServiceName()));
                    }
                    return;
                }
                case write -> {
                    if (!hasWritePermission(service.getServiceName())) {
                        throw new PermissionException(
                                String.format("没有模型%s写入权限", service.getServiceName()));
                    }
                    return;
                }
                case create -> {
                    if (!hasCreatePermission(service.getServiceName())) {
                        throw new PermissionException(
                                String.format("没有模型%s创建权限", service.getServiceName()));
                    }
                    return;
                }
                case unlink -> {
                    if (!hasUnlinkPermission(service.getServiceName())) {
                        throw new PermissionException(
                                String.format("没有模型%s删除权限", service.getServiceName()));
                    }
                    return;
                }
            }
        }
    }

    private boolean hasReadPermission(String serviceName) {
        return groupService.hasReadPermission(serviceName);
    }

    private boolean hasWritePermission(String serviceName) {
        return groupService.hasWritePermission(serviceName);
    }

    private boolean hasCreatePermission(String serviceName) {
        return groupService.hasCreatePermission(serviceName);
    }

    private boolean hasUnlinkPermission(String serviceName) {
        return groupService.hasUnlinkPermission(serviceName);
    }
}
