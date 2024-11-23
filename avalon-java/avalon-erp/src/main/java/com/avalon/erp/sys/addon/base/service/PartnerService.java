/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.erp.sys.addon.base.model.enums.PartnerTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PartnerService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "base.partner";
    }

    @Override
    public String getLabel() {
        return "联系人";
    }

    public final Field userId = Fields.createMany2one("账号", "base.user");

    public final Field type = Fields.createSelection("类型", PartnerTypeEnum.class, PartnerTypeEnum.person);
    public final Field phone = Fields.createString("电话");
    public final Field email = Fields.createString("邮箱");
    public final Field provinceId = Fields.createMany2one("省", "base.area.2023");
    public final Field cityId = Fields.createMany2one("市", "base.area.2023");
    public final Field districtId = Fields.createMany2one("区", "base.area.2023");
    public final Field street = Fields.createString("街道地址");
    public final Field longitude = Fields.createFloat("经度");
    public final Field latitude = Fields.createFloat("纬度");
    public final Field fullAddress = Fields.createString("地区全称"); // =province+city+district+street
}
