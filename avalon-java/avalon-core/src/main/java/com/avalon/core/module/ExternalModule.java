/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class ExternalModule extends AbstractModule implements IExternalModule {
    private String moduleName;
    private String label;
    private String description;
    private Boolean display = false;
    private Boolean isInstall = false;
    private String icon;

    private Boolean isExternal = true;

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean getDisplay() {
        return display;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public Boolean getIsInstall() {
        return isInstall;
    }

    @Override
    public void postConstruct() {
    }

    @Override
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDisplay(Boolean display) {
        this.display = display;
    }

    @Override
    public void setIsInstall(Boolean isInstall) {
        this.isInstall = isInstall;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /***
     * 注册模块
     */
    public void register() {
        addModule();
    }
}
