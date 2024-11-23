/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.context.Context;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.model.Record;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.sys.addon.base.service.UserService;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.BCryptUtil;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.redis.RedisCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class HomeController {
    private final RedisCommon redisCommon;
    private final UserService userService;
    private final Context context;

    public HomeController(RedisCommon redisCommon,
                          UserService userService,
                          Context context) {
        this.redisCommon = redisCommon;
        this.userService = userService;
        this.context = context;
    }

    @PostMapping("login")
    public RecordRow sysLogin(@RequestBody Map<String, Object> param) throws AvalonException {
        String name = param.get("username").toString();
        String db = param.get("db").toString();
        if (StringUtils.isEmpty(db)) {
            throw new FieldCheckException("请选择数据库");
        }
        context.init(db);
        String password = param.get("password").toString();
        Record login = userService.login(name, password);

        if (login.isEmpty()) {
            throw new FieldCheckException("账号或密码错误");
        }
        RecordRow row = login.get(0);
        String token = BCryptUtil.simpleUUID();
        row.put("token", token);
        row.put("db", db);
        String s = row.convert2Json();
        redisCommon.set(SystemConstant.TOKEN_PREFIX + token, s);
        return row;
    }

    @PostMapping("register")
    public void sysRegister(@RequestBody Map<String, Object> param) throws AvalonException {
        String account = param.get("username").toString();
        String name = param.get("name").toString();
        String password = param.get("password").toString();
        userService.register(name, account, password);
    }

    @PostMapping("validate")
    public void validateLogin(@RequestBody Map<String, Object> param) throws AvalonException {
        String token = param.get("token").toString();

        Object o = redisCommon.getToken(token);
        if (ObjectUtils.isNull(o)) {
            throw new AvalonException("token失效，请重新登录");
        }
    }


    @GetMapping("test")
    public String test() throws AvalonException {
        return "OK";
    }
}
