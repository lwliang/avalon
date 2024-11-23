/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.field;

import com.avalon.core.module.AbstractModule;
import org.springframework.stereotype.Service;

@Service
public class FieldModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "field";
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Boolean getDisplay() {
        return null;
    }
}
