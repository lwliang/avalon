/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import lombok.Data;

@Data
public class Team {
    private Integer id;
    private String name;
    private TeamMemberList members;
}
