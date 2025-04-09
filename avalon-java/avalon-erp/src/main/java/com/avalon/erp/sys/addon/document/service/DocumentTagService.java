/**
 * @author lwlianghehe@gmail.com
 * @date 2025/2/20 15:38
 */
package com.avalon.erp.sys.addon.document.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentTagService extends AbstractService {
    @Override
    public String getServiceName() {
        return "document.file.tag";
    }

    @Override
    public String getLabel() {
        return "文件标签";
    }

    public Field color = Fields.createString("颜色");
}
