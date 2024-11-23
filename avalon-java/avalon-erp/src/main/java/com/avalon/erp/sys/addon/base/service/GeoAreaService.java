/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 省市区服务
 */
@Slf4j
@Service
public class GeoAreaService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.area.2023";
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("名称");
    }

    protected final Field code = Fields.createBigIntegerField("区划代码",
            true, false, true);

    @Override
    public Boolean getNeedDefaultKeyField() {
        return false;
    }

    protected final Field level = Fields.createInteger("级别");//1省2市3区(县)4街道(镇)5社区(村)
    protected final Field pCode = Fields.createMany2one("父编码", "base.area.2023");
    protected final Field category = Fields.createInteger("城乡分类");
}
