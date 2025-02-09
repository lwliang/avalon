package com.avalon.erp.sys.addon.project;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class ProjectModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "project";
    }

    @Override
    public String getLabel() {
        return "项目管理";
    }

    @Override
    public String getDescription() {
        return "项目管理模块用于管理项目任务、分配资源、跟踪进度等";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/project.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/project.tag.views.xml",
                "resource/view/project.task.views.xml",
                "resource/view/project.views.xml",
                "resource/view/menus.xml",
        };
    }
}
