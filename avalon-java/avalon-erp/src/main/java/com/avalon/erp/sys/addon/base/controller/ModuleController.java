/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.condition.EqualCondition;
import com.avalon.core.context.Context;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.exception.FileIOException;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.util.ClassUtils;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.sys.addon.base.service.GroupService;
import com.avalon.erp.sys.addon.base.service.MenuService;
import com.avalon.erp.sys.addon.base.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/module")
@Slf4j
public class ModuleController {
    @Autowired
    private  Context context;

    @PostMapping("/get/permission/module")
    public Record getModulePermission() {
        ModuleService moduleService = (ModuleService)context.getServiceBean("base.module");
        return moduleService.getDisplayModules();
    }

    @PostMapping("/get/install/module")
    public Record getInstallModule() {
        ModuleService moduleService = (ModuleService)context.getServiceBean("base.module");
        return moduleService.getInstalledModules();
    }

    @PostMapping("/get/permission/menu")
    public Record getMenuPermission(@RequestBody RecordRow param) {
        String module = param.getString("module");
        String fields = param.getString("field");
        MenuService menuService = (MenuService)context.getServiceBean("base.menu");
        GroupService groupService = (GroupService)context.getServiceBean("base.group");
        Condition condition = Condition.equalCondition("moduleId.name", module);
        if (!context.getUserId().equals(SystemConstant.ADMIN)) {
            List<Integer> menuIds = groupService.getPermissionMenu(groupService.getContext().getUserId());
            if (!menuIds.isEmpty()) {
                Condition menuCondition = Condition.inCondition("id", menuIds);
                Record parentMenus = menuService.select(menuCondition, "parentPath");
                if (!parentMenus.isEmpty()) { // 获取父menu
                    for (RecordRow menuId : parentMenus) {
                        if (menuId.isNull("parentPath")) continue;

                        String parentPath = menuId.getString("parentPath");
                        for (String s : FieldUtils.getFieldList(parentPath)) {
                            if (StringUtils.isNotEmpty(s)) {
                                menuIds.add(Integer.parseInt(s));
                            }
                        }
                    }
                    menuCondition = Condition.inCondition("id", menuIds);
                }
                condition = condition.andCondition(menuCondition);
            } else {
                condition = condition.andCondition(Condition.inCondition("id", 0));
            }
        }

        return menuService.select("sequence asc,id asc",
                condition,
                FieldUtils.getFieldArray(fields));
    }

    @GetMapping("icon/down/{module}/**")
    public void downloadFile(@PathVariable("module") String moduleName,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException, URISyntaxException {
        String prefix = "/module/icon/down/" + moduleName;
        String path = request.getServletPath().substring(prefix.length());
        AbstractModule module = context.getModule(moduleName);
        String filePath = ClassUtils.getModulePackagePath(module);
        if (path.startsWith("/")) {
            filePath = filePath + path;
        } else {
            filePath = filePath + "/" + path;
        }

        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            byte[] content = resourceAsStream.readAllBytes();

            //设置下载响应头
            response.setContentType("image/png");
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode("icon", StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException("icon图片读取发生错误");
        }
    }

    @GetMapping("icon/default")
    public void downloadModuleDefaultIcon(HttpServletRequest request,
                                          HttpServletResponse response) throws IOException, URISyntaxException {
        try {
            String path = "module/module.png";

            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
            byte[] content = resourceAsStream.readAllBytes();

            //设置下载响应头
            response.setContentType("image/png");
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode("defaultIcon", StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException("默认icon图片读取发生错误");
        }
    }

    @GetMapping("/get/start/js/{module}")
    public List<String> getModuleStartJS(@PathVariable("module") String moduleName) {
        AbstractModule module = context.getModule(moduleName);
        if (ObjectUtils.isEmpty(module.getStartJS())) {
            return new ArrayList<>();
        }
        return List.of(module.getStartJS());
    }

    @GetMapping("/download/start/js/{module}/**")
    public void downloadModuleStartJS(@PathVariable("module") String moduleName,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws FileIOException {
        String prefix = "/module/download/start/js/" + moduleName;
        String path = request.getServletPath().substring(prefix.length());
        AbstractModule module = context.getModule(moduleName);
        String filePath = ClassUtils.getModulePackagePath(module);
        if (path.startsWith("/")) {
            filePath = filePath + path;
        } else {
            filePath = filePath + "/" + path;
        }

        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            byte[] content = resourceAsStream.readAllBytes();

            //设置下载响应头
            response.setContentType("text/javascript");
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode(path.replaceAll("/", "_"), StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException("start js 读取发生错误");
        }
    }

    @GetMapping("/get/start/vue/{module}")
    public List<String> getModuleVue(@PathVariable("module") String moduleName) {
        AbstractModule module = context.getModule(moduleName);
        if (ObjectUtils.isEmpty(module.getVue())) {
            return new ArrayList<>();
        }
        return List.of(module.getVue());
    }

    @GetMapping("/download/start/vue/{module}/**")
    public void downloadModuleVue(@PathVariable("module") String moduleName,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws FileIOException {
        String prefix = "/module/download/start/vue/" + moduleName;
        String path = request.getServletPath().substring(prefix.length());
        AbstractModule module = context.getModule(moduleName);
        String filePath = ClassUtils.getModulePackagePath(module);
        if (path.startsWith("/")) {
            filePath = filePath + path;
        } else {
            filePath = filePath + "/" + path;
        }

        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            byte[] content = resourceAsStream.readAllBytes();

            //设置下载响应头
            response.setContentType("text/javascript");
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode(path.replaceAll("/", "_"), StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException("start vue 读取发生错误");
        }
    }
}
