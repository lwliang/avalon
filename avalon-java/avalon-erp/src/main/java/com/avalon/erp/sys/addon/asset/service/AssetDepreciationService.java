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
public class AssetDepreciationService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.depreciation";
    }

    @Override
    public String getLabel() {
        return "折旧表";
    }

    public Field assetId = Fields.createMany2one("资产", "asset.asset");
    public Field depreciationDate = Fields.createDate("折旧时间");
    public Field depreciationValue = Fields.createBigDecimal("折旧金额");
    public Field cumulativeValue = Fields.createBigDecimal("累计折旧金额");
    public Field remainValue = Fields.createBigDecimal("剩余价值");
    public Field remark = Fields.createText("备注");
}
