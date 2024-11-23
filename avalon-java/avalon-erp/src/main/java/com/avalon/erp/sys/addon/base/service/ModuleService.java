/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModuleService extends AbstractService {
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


    public Record getInstalledModules() {
        Condition condition = isInstall.eq(true);
        return select(condition, "id", "label", "name", "icon", "description", "display");
    }

    @Override
    public PageInfo selectPage(PageParam pageParam, String order, Condition condition, String... fields) throws AvalonException {
        PageInfo pageInfo = super.selectPage(pageParam, order, condition, fields);


        return pageInfo;
    }

    public void refreshModuleFromDisk(List<Object> ids, RecordRow row) {
        for (AbstractModule module : getContext().getModuleMap().getModuleList()) {
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

    public void install(List<Object> ids, RecordRow row) throws AvalonException {
        if (!ObjectUtils.isEmpty(ids)) {
            for (Object id : ids) {
                Record select = select(Condition.equalCondition("id", id), "name");
                String moduleName = select.get(0).getString("name");
                AbstractModule module = getContext().getModuleMap().getModule(moduleName);
                module.setIsInstall(true);
                module.createModule();
                select = select(Condition.equalCondition("name", moduleName), "id");
                select.putAll("isInstall", true);
                updateMulti(select);
            }
        } else {
            AbstractModule module = getContext().getModuleMap().getModule(row.getString("name"));
            module.setIsInstall(true);
            module.createModule();
        }
    }

    public void uninstall(List<Object> ids, RecordRow row) throws AvalonException {
        for (Object id : ids) {
            Record select = select(Condition.equalCondition("id", id), "name");
            String moduleName = select.get(0).getString("name");
            AbstractModule module = getContext().getModuleMap().getModule(moduleName);
            module.setIsInstall(false);
            module.dropModule();
            select = select(Condition.equalCondition("name", moduleName), "id");
            select.putAll("isInstall", false);
            updateMulti(select);
        }
    }

    public void upgrade(List<Object> ids, RecordRow row) throws AvalonException {
        for (Object id : ids) {
            Record select = select(Condition.equalCondition("id", id), "name");
            String moduleName = select.get(0).getString("name");
            AbstractModule module = getContext().getModuleMap().getModule(moduleName);
            module.setIsInstall(true);
            module.upgradeModule();
            select = select(Condition.equalCondition("name", moduleName), "id");
            select.putAll("isInstall", true);
            updateMulti(select);
        }
    }
}
