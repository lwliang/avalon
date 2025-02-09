package com.avalon.erp.sys.addon.asset.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class AssetDiscardService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.discard";
    }

    @Override
    public String getLabel() {
        return "资产报废";
    }

    public Field assetId = Fields.createMany2one("资产", "asset.asset");
    public Field discardDate = Fields.createDate("维护日期");
    public Field remainValueValue = Fields.createBigDecimal("剩余价值");
    public Field responsibleUserId = Fields.createMany2one("报废负责人", "hr.staff");
    public Field remark = Fields.createText("报废原因");
    public Field discardMethod = Fields.createSelection("报废方式",
            new SelectionHashMap() {{
                put("sale", "销售");
                put("destroy", "销毁");
            }}, "destroy");
}
