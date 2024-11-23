/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.addon.pet.model.enums.ResourceTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonImageService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.common.image";
    }

    @Override
    public String getLabel() {
        return "通用图片,包括首页轮播图，订单状态图片等等";
    }

    public final Field img = Fields.createImage("图片");
    public final Field sequence = Fields.createInteger("排序");
    public final Field key = Fields.createString("关键标识");
    public final Field type = Fields.createSelection("资源类型", ResourceTypeEnum.class);
    public final Field url = Fields.createString("链接地址");
    public final Field param1 = Fields.createString("参数1");
    public final Field param2 = Fields.createString("参数2");
}
