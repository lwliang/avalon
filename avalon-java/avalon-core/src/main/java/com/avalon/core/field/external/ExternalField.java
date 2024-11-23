/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field.external;

public interface ExternalField {
    /**
     * 是否是扩展字段
     *
     * @return 是否是扩展字段
     */
    Boolean getIsExternalField();

    /**
     * 设置是否是扩展字段
     *
     * @param isExternalField 是否是扩展字段
     */
    void setIsExternalField(Boolean isExternalField);
}
