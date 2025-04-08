/**
 * @author lwlianghehe@gmail.com
 * @date 2025/2/20 15:38
 */
package com.avalon.erp.sys.addon.document;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "document";
    }

    @Override
    public String getLabel() {
        return "文档管理";
    }

    @Override
    public String getDescription() {
        return "文件存储,组织,共享,权限管理,标签和搜索功能";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/document.png";
    }

    @Override
    public String[] getVue() {
        return new String[]{
                "resource/vue/test.vue"
        };
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/document.file.views.xml",
                "resource/view/document.show.transient.xml",
                "resource/view/menu.xml",
        };
    }
}
