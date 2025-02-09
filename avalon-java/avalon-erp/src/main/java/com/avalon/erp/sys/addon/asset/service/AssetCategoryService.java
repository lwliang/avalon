package com.avalon.erp.sys.addon.asset.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class AssetCategoryService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.category";
    }

    @Override
    public String getLabel() {
        return "资产类别";
    }

    public Field code = Fields.createString("分类代码", true);
    public Field remark = Fields.createText("备注");

}
