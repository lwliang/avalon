/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.exception.IAvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class AvalonControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        //response.setStatusCode(HttpStatus.OK);//解决返回状态码 异常问题
        if (null == body) {
            return null;
        }

        return body;
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> errorHandler(HttpServletRequest req, HttpServletResponse response, Exception ex) {
        Map<String, Object> map = new HashMap<>();
        Integer code = 0;
        response.setStatus(500);
        if (ex instanceof FieldCheckException) {
            FieldCheckException fieldCheckException = (FieldCheckException) ex;
            Field field = fieldCheckException.getField();
            String serviceName = fieldCheckException.getServiceName();
            if (ObjectUtils.isNotNull(field)) {
                map.put("field", field.getName());
            }

            if (ObjectUtils.isNotEmpty(serviceName)) {
                map.put("serviceName", serviceName);
            }

            code = fieldCheckException.getCode();
        } else if (ex instanceof IAvalonException) {
            code = ((IAvalonException) ex).getCode();
            response.setStatus(((IAvalonException) ex).getStatusCode());
        } else if (ex instanceof BadSqlGrammarException) {
            code = 8000;
        } else if (ex instanceof NullPointerException) {
            code = 8100;
        } else {
            code = 9000;
        }
        map.put("code", code);
        map.put("msg", ex.getMessage());
        log.error(ex.getMessage(), ex);
        return map;
    }

}
