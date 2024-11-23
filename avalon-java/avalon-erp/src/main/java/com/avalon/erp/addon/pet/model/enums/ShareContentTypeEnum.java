/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ShareContentTypeEnum implements ISelectFieldEnum {
    image("图片"),
    video("视频");

    private final String name;

    public String getName() {
        return name;
    }

    ShareContentTypeEnum(String name) {
        this.name = name;
    }
}