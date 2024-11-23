/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceModelField extends ServiceModelId {
    private String fields; // field,field field.field,field
    private String rpnCondition;
    private String order;// field asc, field.field desc
    private Boolean isDistinct;
}
