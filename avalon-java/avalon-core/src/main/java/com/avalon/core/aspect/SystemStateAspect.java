package com.avalon.core.aspect;

import com.avalon.core.context.Context;
import com.avalon.core.enums.SystemStateAnnotation;
import com.avalon.core.enums.SystemStateEnum;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/07 20:26
 */
@Aspect
@Component
public class SystemStateAspect {
    private final Context context;

    public SystemStateAspect(Context context) {
        this.context = context;
    }

    @Around("@annotation(systemStateAnnotation)")
    public Object elevatePermissions(ProceedingJoinPoint joinPoint, SystemStateAnnotation systemStateAnnotation) throws Throwable {
        SystemStateEnum value = systemStateAnnotation.value();
        try {
            context.addSystemState(value);
            return joinPoint.proceed(); // 执行目标方法
        } finally {
            context.clearSystemState(value);
        }
    }
}
