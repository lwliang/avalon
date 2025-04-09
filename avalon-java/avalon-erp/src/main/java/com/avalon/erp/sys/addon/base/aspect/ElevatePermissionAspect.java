/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.aspect;

import com.avalon.core.context.Context;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ElevatePermissionAspect {
    private final Context context;

    public ElevatePermissionAspect(Context context) {
        this.context = context;
    }

    @Around("@annotation(temporaryElevate)")
    public Object elevatePermissions(ProceedingJoinPoint joinPoint, TemporaryElevate temporaryElevate) throws Throwable {
        ElevatePermissionEnum[] value = temporaryElevate.value();
        try {
            for (ElevatePermissionEnum elevatePermissionEnum : value) {
                context.addTemporaryElevate(elevatePermissionEnum);
            }
            return joinPoint.proceed(); // 执行目标方法
        } finally {
            for (ElevatePermissionEnum elevatePermissionEnum : value) {
                context.clearTemporaryElevate(elevatePermissionEnum);
            }
        }
    }
}
