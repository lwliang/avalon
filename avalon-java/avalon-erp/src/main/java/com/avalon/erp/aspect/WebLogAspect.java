/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.aspect;

import com.avalon.core.context.Context;
import com.avalon.core.util.HttpRequestUtils;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.model.WebLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一日志处理切面
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class WebLogAspect {
    @Autowired
    private Context context;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {

    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {

    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        return joinPoint.proceed();
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        //记录请求信息(通过Logstash传入Elasticsearch)
        WebLog webLog = new WebLog();
        if (ObjectUtils.isNotEmpty(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            String ipAddress = HttpRequestUtils.getIpAddress(request);
            String urlStr = request.getRequestURL().toString();
            webLog.setUrl(urlStr);
            webLog.setIp(ipAddress);
        }
        List<Object> args = new ArrayList<>();
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            args.add(arg);
        }
        List<Object> param = new ArrayList<>();
        for (Object o : args.toArray()) {
            if(o instanceof MultipartFile) {
                continue;
            }
            param.add(o);
        }
        webLog.setParameter(param);
        log.info("开始接口调用 -> {}", JacksonUtil.object2String(webLog));

        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(log.value());
        }
        webLog.setMethod(method.getName());

        long endTime = System.currentTimeMillis();
        //webLog.setResult(result);
        webLog.setUserId(context.getUserId());
        webLog.setSpendTime(endTime - startTime);

        log.info("接口调用结束 -> {}", JacksonUtil.object2String(webLog));
        return result;
    }
}
