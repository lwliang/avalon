package com.avalon.erp.sys.addon.document.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.TransientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/05 16:16
 */
//进行文档显示
@Service
@Slf4j
public class DocumentViewService extends TransientService {
    @Override
    public String getServiceName() {
        return "document.show.transient";
    }

    @Override
    public String getLabel() {
        return "文档显示";
    }

    public Field documents = Fields.createOne2many("文档", "document.file", "ownerId"); //当前用户文档

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        AbstractService documentService = getContext().getServiceBean("document.file");
        Condition condition = Condition.equalCondition("ownerId", getContext().getUserId())
                .andEqualCondition("active", false)
                .andEqualCondition("parentId", null); // 默认获取第一层文件与文件夹
        Record select = documentService.select(condition,
                "id", "name", "isFolder", "url", "size", "mine", "ownerId");
        defaultRow.put(documents, select);
        return super.create(defaultRow);
    }

    // 上传文件,前端跳转路由
    public RecordRow uploadFile() {
        RecordRow row = RecordRow.build();
        row.put("type", "ir.actions.client")
                .put("tag", "uploadDocument");
        return row;
    }
}
