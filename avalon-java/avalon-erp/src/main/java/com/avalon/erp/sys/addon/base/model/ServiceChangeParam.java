package com.avalon.erp.sys.addon.base.model;

import com.avalon.core.model.RecordRow;
import lombok.Data;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 13:00
 */
@Data
public class ServiceChangeParam {
    private RecordRow changeFieldRow;// 只保存到二级
    private RecordRow newRow;
    private RecordRow oldRow;
}
