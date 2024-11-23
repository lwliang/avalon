/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.model;

import lombok.Data;

@Data
public class WebLog {
    private String description;
    private Integer userId;
    private String platform;//平台 派单台 接单宝
    private Long spendTime;
    private String url;
    private String method;
    private String ip;
    private Object parameter;
    private Object result;
}
