/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum PetAdoptStateEnum implements ISelectFieldEnum {
    // 以下是用户宠物状态
    raise("抚养中"),
    sending("送养中"),
    completed("已送养"),


    // 以下是领养记录状态
    adopting("待领养"),
    success("成功领养");

    private final String name;

    public String getName() {
        return name;
    }

    PetAdoptStateEnum(String name) {
        this.name = name;
    }
}