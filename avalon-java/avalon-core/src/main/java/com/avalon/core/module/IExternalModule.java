/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.module;

public interface IExternalModule {
    void setModuleName(String moduleName);

    void setLabel(String label);

    void setDescription(String description);

    void setDisplay(Boolean display);

    void setIsInstall(Boolean isInstall);

    void setIcon(String icon);
}
