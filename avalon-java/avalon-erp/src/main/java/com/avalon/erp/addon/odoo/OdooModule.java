/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OdooModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "odoo";
    }

    @Override
    public String getLabel() {
        return "Odoo";
    }

    @Override
    public String getDescription() {
        return "Odoo 对接接口";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

}
