/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.enums.SystemStateAnnotation;
import com.avalon.core.enums.SystemStateEnum;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IModuleSupport;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class ModuleService extends AbstractService implements IModuleSupport {
    @Override
    public String getServiceName() {
        return "base.module";
    }

    @Override
    public Field getNameField() {
        return label;
    }

    protected final Field label = Fields.createString("显示名称");
    protected final Field description = Fields.createString("描述");
    protected final Field display = Fields.createBoolean("显示");
    protected final Field icon = Fields.createString("图标");
    public final Field isInstall = Fields.createBoolean("已安装");

    /**
     * 获取已安装且可显示的模块，同时需要满足权限
     *
     * @return
     */
    public Record getDisplayModules() {
        Condition condition = isInstall.eq(true);
        condition = condition.andCondition(display.eq(true));
        List<Integer> getPermissionModule = (List<Integer>) invokeMethod("base.group",
                "getPermissionModule", getContext().getUserId());
        if (!getContext().getUserId().equals(SystemConstant.ADMIN)) {
            if (!getPermissionModule.isEmpty()) {
                condition = condition.andCondition(Condition.inCondition("id", getPermissionModule));
            } else { // 没有权限，则返回空
                condition = condition.andCondition(Condition.equalCondition("id", 0));
            }
        }

        return select("id desc", condition,
                "id", "label", "name", "icon", "description", "display", "isInstall");
    }

    public Record getInstalledModules() {
        Condition condition = isInstall.eq(true);
        return select(condition, "id", "label", "name", "icon", "description", "display");
    }

    @Override
    public PageInfo selectPage(PageParam pageParam, String order, Condition condition, String... fields) throws AvalonException {
        PageInfo pageInfo = super.selectPage(pageParam, order, condition, fields);

        return pageInfo;
    }

    @TemporaryElevate({ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
    public void refreshModuleFromDisk(ArrayList<Object> ids, RecordRow row) {
        for (AbstractModule module : getContext().getModuleList()) {
            RecordRow recordRow = new RecordRow();
            recordRow.put("name", module.getModuleName());
            recordRow.put("label", module.getLabel());
            recordRow.put("description", module.getDescription());
            recordRow.put("display", module.getDisplay());
            recordRow.put("icon", module.getIcon());
            recordRow.put("isInstall", "false");
            Record modules = select(Condition.equalCondition("name", module.getModuleName()),
                    "id", "isInstall");
            if (modules.isEmpty()) {
                insert(recordRow);
            } else {
                recordRow.put("id", modules.get(0).getInteger("id"));
                recordRow.put("isInstall", modules.get(0).getBoolean("isInstall"));
                update(recordRow);
            }
        }
    }

    @SystemStateAnnotation(SystemStateEnum.installModule)
    public void install(ArrayList<Integer> ids, RecordRow row) throws AvalonException {
        List<AbstractModule> modules = new ArrayList<>();
        if (!ObjectUtils.isEmpty(ids)) {
            for (Object id : ids) {
                Record select = select(Condition.equalCondition("id", id), "name");
                String moduleName = select.get(0).getString("name");
                AbstractModule module = getContext().getModule(moduleName);
                modules.add(module);
                module.setIsInstall(true);
                module.createModule();
                select = select(Condition.equalCondition("name", moduleName), "id");
                select.putAll("isInstall", true);
                updateMulti(select);
            }
        } else {
            AbstractModule module = getContext().getModule(row.getString("name"));
            modules.add(module);
            module.setIsInstall(true);
            module.createModule();
        }
        if (!modules.isEmpty()) {
            getContext().installOrUpgrade(modules);
        }
    }

    @SystemStateAnnotation(SystemStateEnum.uninstallModule)
    public void uninstall(ArrayList<Integer> ids, RecordRow row) throws AvalonException {
        List<AbstractModule> modules = new ArrayList<>();
        for (Object id : ids) {
            Record select = select(Condition.equalCondition("id", id), "name");
            String moduleName = select.get(0).getString("name");
            AbstractModule module = getContext().getModule(moduleName);
            modules.add(module);
            module.setIsInstall(false);
            module.dropModule();
            select = select(Condition.equalCondition("name", moduleName), "id");
            select.putAll("isInstall", false);
            updateMulti(select);
        }
        if (!modules.isEmpty()) {
            getContext().uninstall(modules);
        }
    }

    @SystemStateAnnotation(SystemStateEnum.uninstallModule)
    public void upgrade(ArrayList<Integer> ids, RecordRow row) throws AvalonException {
        for (Object id : ids) {
            Record select = select(Condition.equalCondition("id", id), "name");
            String moduleName = select.get(0).getString("name");
            AbstractModule module = getContext().getModule(moduleName);
            module.setIsInstall(true);
            module.upgradeModule();
            select = select(Condition.equalCondition("name", moduleName), "id");
            select.putAll("isInstall", true);
            updateMulti(select);
        }
    }

    @Override
    @TemporaryElevate({ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
    public List<String> getInstalledModule() {
        Condition condition = isInstall.eq(true);

        Record select = select(condition, "id", "name");

        return select.getValues("name");
    }
}
