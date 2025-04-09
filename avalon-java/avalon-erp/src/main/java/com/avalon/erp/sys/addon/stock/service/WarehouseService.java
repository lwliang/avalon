package com.avalon.erp.sys.addon.stock.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 11:18
 */
@Service
@Slf4j
public class WarehouseService extends AbstractService {
    @Override
    public String getServiceName() {
        return "stock.warehouse";
    }

    @Override
    public String getLabel() {
        return "仓库";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("仓库名称", true);
    }

    public final Field code = Fields.createString("仓库编码", true);
    public final Field active = Fields.createBoolean("启用", false, true);
    public final Field provinceId = Fields.createMany2one("省", "base.area.2023");
    public final Field cityId = Fields.createMany2one("市", "base.area.2023");
    public final Field districtId = Fields.createMany2one("区", "base.area.2023");
    public final Field street = Fields.createString("街道地址");
    public final Field fullAddress = Fields.createString("地区全称"); // =province+city+district+street
    public final Field longitude = Fields.createFloat("经度");
    public final Field latitude = Fields.createFloat("纬度");

    public final Field remark = Fields.createText("备注");
}
