/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.interceptor;

import com.avalon.core.context.Context;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.HttpRequestUtils;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.StringUtils;
import com.avalon.core.redis.RedisCommon;
import com.avalon.erp.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AvalonHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCommon redisCommon;
    @Autowired
    private Context context;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = HttpRequestUtils.getRequestToken(request);

        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException("未登录不能访问");
        }

        Object o = redisCommon.getToken(token);

        if (StringUtils.isEmpty(o)) {
            throw new AuthenticationException("未登录不能访问");
        }

        RecordRow recordRow = JacksonUtil.convert2Map(o.toString());
        context.init(recordRow.getString("db"));
        context.setToken(token);
        context.setUserId(recordRow.get("id").getInteger());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);//正常流程
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
