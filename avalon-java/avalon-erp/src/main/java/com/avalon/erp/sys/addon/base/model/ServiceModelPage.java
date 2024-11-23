/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model;

import com.avalon.core.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceModelPage extends ServiceModel {
    private PageParam page;
    private String fields; // field,field field.field,field
    private String order;// field asc, field.field desc
    private Boolean isDistinct;
    private String rpnCondition;
}
