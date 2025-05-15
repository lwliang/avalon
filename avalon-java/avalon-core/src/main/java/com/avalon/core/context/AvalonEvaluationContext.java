/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IServiceDataService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.FieldUtils;
import lombok.Data;

import java.util.Date;

@Data
public class AvalonEvaluationContext {
    private Integer userId;
    private Integer orgId;
    private Context context;

    public Date getCurrentDate() {
        return DateTimeUtils.getCurrentDate();
    }

    /**
     * 获取资源对应的主键id
     *
     * @param resourceId 资源id 含模块名 格式 模块名.id
     * @return 主键
     */
    public Object refId(String resourceId) {
        String module = FieldUtils.getJoinFirstTableString(resourceId);
        resourceId = FieldUtils.getJoinFirstFieldString(resourceId);

        IServiceDataService resourceService = (IServiceDataService) context.getServiceBean("base.service.data");


        return resourceService.refId(module, resourceId);
    }

    /**
     * 获取模型id
     *
     * @param serviceName 模型名
     * @return 模型id
     */
    public Object refServiceId(String serviceName) {
        AbstractService serviceDataService = context.getServiceBean("base.service");
        Record name = serviceDataService.search(Condition.equalCondition("name", serviceName));

        return name.isEmpty() ? null : name.get(0).getInteger("id");
    }
}
