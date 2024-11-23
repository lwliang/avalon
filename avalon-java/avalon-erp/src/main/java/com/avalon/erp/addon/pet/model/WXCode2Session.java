/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model;

import lombok.Data;

@Data
public class WXCode2Session {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode; // 0 正常; -1 系统繁忙 ;40029 code无效 ;45011 频率限制;40226 高风险等级用户，小程序登录拦截
    private String errmsg;
    private Integer userId;
}
