/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BaseModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "base";
    }

    @Override
    public String getLabel() {
        return "基础模块";
    }

    @Override
    public String getDescription() {
        return "基础模块 系统登录,权限,菜单等功能";
    }

    @Override
    public String getIcon() {
        return "resource/base.png";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/record/base.user.xml",
                "resource/view/module.views.xml",
                "resource/view/user.views.xml",
                "resource/view/org.views.xml",
                "resource/view/job.views.xml",
                "resource/view/partner.views.xml",
                "resource/view/staff.views.xml",
                "resource/view/menus.xml"
        };
    }

    /**
     * 定制安装模块 顺序
     */
    @Override
    public void createModule() {
        List<String> noUpgrade = List.of("base.field", "base.service.log", "base.module", "base.service");
        for (String s : noUpgrade) {
            getContext().getServiceBean(s).createTable(); // 优先创建
        }

        PrimaryKey key = insertModuleInfo();
        if (ObjectUtils.isNull(getServiceList())) return;
        for (AbstractService service : getServiceList()) {
            if (service.getServiceName().equals("base.module")) {
                PrimaryKey serviceId = service.insertTableInfo(key);
                service.insertFieldInfo(serviceId);
                continue;
            }
            service.createTable();
            PrimaryKey serviceId = service.insertTableInfo(key);
            service.insertFieldInfo(serviceId);
        }

        loadResource();
    }

    @Override
    public void upgradeModule() {
        upgradeModuleInfo();
        if (ObjectUtils.isNull(getServiceList())) return;
        List<String> noUpgrade = List.of("base.field", "base.service.log", "base.module", "base.service");
        for (String s : noUpgrade) {
            getContext().getServiceBean(s).upgradeTable(); // 优先升级
        }

        AbstractService serviceBean = getContext().getServiceBean("base.service");

        for (AbstractService service : getServiceList()) {
            if (noUpgrade.contains(service.getServiceName())) {
                continue;
            }
            service.upgradeTable();
            if (ObjectUtils.isNotNull(serviceBean)) {
                FieldValue fieldValue = serviceBean.getFieldValue("id",
                        Condition.equalCondition("name", service.getServiceName()));
                PrimaryKey serviceId = PrimaryKey.build(fieldValue);
                service.insertFieldInfo(serviceId);
            }
        }

        loadResource();
    }
}
