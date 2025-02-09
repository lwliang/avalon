package com.avalon.erp.sys.addon.asset;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class AssetModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "asset";
    }

    @Override
    public String getLabel() {
        return "资产管理";
    }

    @Override
    public String getDescription() {
        return "资产管理模块通常用于管理公司资产的生命周期，包括资产的创建、折旧、报废、转移等功能";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/asset.png";
    }

    @Override
    public String[] depends() {
        return new String[]{"hr", "stock"};
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/asset.category.views.xml",
                "resource/view/asset.depreciation.views.xml",
                "resource/view/asset.discard.views.xml",
                "resource/view/asset.maintenance.views.xml",
                "resource/view/asset.transfer.views.xml",
                "resource/view/asset.views.xml",
                "resource/view/menus.xml",
        };
    }
}
