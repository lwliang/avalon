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
public class AssetMaintenanceService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.maintenance";
    }

    @Override
    public String getLabel() {
        return "资产维护";
    }

    public Field assetId = Fields.createMany2one("资产", "asset.asset");
    public Field maintenanceDate = Fields.createDate("维护日期");
    public Field cost = Fields.createBigDecimal("维护成本");
    public Field responsibleUserId = Fields.createMany2one("维护负责人", "hr.staff");
    public Field remark = Fields.createText("备注");
}
