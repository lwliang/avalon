/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.hr;

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

    @Override
    public String getIcon() {
        return "resource/hr.png";
    }

    @Override
    public String[] depends() {
        return new String[]{"crm.common"};
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/job.views.xml",
                "resource/view/org.views.xml",
                "resource/view/staff.views.xml",
                "resource/view/hr.user.views.xml",
                "resource/view/menus.xml",
        };
    }
}
