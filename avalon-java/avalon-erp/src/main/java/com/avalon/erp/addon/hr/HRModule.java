/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.hr;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HRModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "hr";
    }

    @Override
    public String getLabel() {
        return "HR";
    }

    @Override
    public String getDescription() {
        return "考勤，绩效，员工管理，培训";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }
}
