/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model;

import com.avalon.core.model.RecordRow;
import lombok.Data;

import java.util.List;

@Data
public class ServiceInvokeParam {
    private String method;
    private List<Object> ids;
    private RecordRow param;
}
