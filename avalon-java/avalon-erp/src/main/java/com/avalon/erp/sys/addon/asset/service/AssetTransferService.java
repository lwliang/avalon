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
public class AssetTransferService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.transfer";
    }

    @Override
    public String getLabel() {
        return "资产转移";
    }

    public Field assetId = Fields.createMany2one("资产", "asset.asset");
    public Field fromLocationId = Fields.createMany2one("转移前位置", "stock.location");
    public Field toLocationId = Fields.createMany2one("转移后位置", "stock.location");
    public Field transferDate = Fields.createDate("转移日期");
    public Field responsibleUserId = Fields.createMany2one("转移负责人", "hr.staff");
    public Field remark = Fields.createText("备注");
}
