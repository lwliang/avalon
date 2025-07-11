/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.util.PinyinUtil;
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
    protected Field createPrimaryKeyField() {
        return Fields.createBigIntegerField("区划代码",
                true, false, true);
    }

    public final Field level = Fields.createInteger("级别");//1省2市3区(县)4街道(镇)5社区(村)
    public final Field pCode = Fields.createMany2one("父编码", "base.area.2023");
    public final Field letter = Fields.createString("首字母");
    public final Field category = Fields.createInteger("城乡分类");

    public Record getCity() {
        Condition condition = Condition.inCondition("pCode", "120000000000",
                "110000000000",
                "440000000000",
                "430000000000",
                "350000000000",
                "340000000000",
                "330000000000",
                "320000000000",
                "310000000000");
        condition = condition.andCondition(Condition.equalCondition("level", 2));
        return select(condition, "id", "name", "level", "pCode", "letter");
    }
}
