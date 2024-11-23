/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAddressService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.address";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field petUserId = Fields.createMany2one("用户",
            "pet.user");
    public final Field address = Fields.createString("街道地址");
    public final Field addressDetail = Fields.createString("详细地址"); // =province+city+district+address
    public final Field isDefault = Fields.createBoolean("是否默认地址");
    public final Field province = Fields.createString("省");
    public final Field provinceCode = Fields.createMany2one("省code", "base.area.2023");
    public final Field city = Fields.createString("市");
    public final Field cityCode = Fields.createMany2one("市code", "base.area.2023");
    public final Field district = Fields.createString("区");
    public final Field districtCode = Fields.createMany2one("区code", "base.area.2023");
    public final Field longitude = Fields.createFloat("经度");
    public final Field latitude = Fields.createFloat("纬度");
    public final Field phone = Fields.createString("手机号");
    public final Field receiverName = Fields.createString("收货人姓名");
    public final Field postCode = Fields.createString("邮编");
}
