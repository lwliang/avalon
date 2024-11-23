/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.log;

import com.avalon.core.enums.ServiceOperateEnum;
import lombok.Data;

@Data
public class ServiceLog {
    private Integer handleId;
    private String handleName;
    private String serviceName;
    private Object serviceId;
    private String content;//是json字符串 只记录自身表的数据
    private ServiceOperateEnum op;
}
