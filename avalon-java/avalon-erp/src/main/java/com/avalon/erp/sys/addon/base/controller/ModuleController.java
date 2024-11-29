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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/module")
@Slf4j
public class ModuleController {
    private final ModuleService moduleService;
    private final Context context;
    private final MenuService menuService;
    private final GroupService groupService;

    @Autowired

    public ModuleController(ModuleService moduleService,
                            Context context,
                            MenuService menuService,
                            GroupService groupService) {
        this.moduleService = moduleService;
        this.context = context;
        this.menuService = menuService;
        this.groupService = groupService;
    }

    @PostMapping("/get/permission/module")
    public Record getModulePermission() {
        return moduleService.getDisplayModules();
    }

    @PostMapping("/get/permission/menu")
    public Record getMenuPermission(@RequestBody RecordRow param) {
        String module = param.getString("module");
        String fields = param.getString("field");

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
}
