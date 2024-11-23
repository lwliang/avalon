/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import com.avalon.core.enums.ServiceOperateEnum;
import lombok.Data;

@Data
public class TeamMember {
    private Integer id;
    private Integer teamId;
    private Integer userId;
    private ServiceOperateEnum op;
}
