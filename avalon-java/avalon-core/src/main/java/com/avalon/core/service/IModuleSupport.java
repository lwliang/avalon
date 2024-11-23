/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.module.AbstractModule;

public interface IModuleSupport {
    AbstractModule getModule();

    void setModule(AbstractModule module);
}
