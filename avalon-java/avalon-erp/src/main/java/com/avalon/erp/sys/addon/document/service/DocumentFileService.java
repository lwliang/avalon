/**
 * @author lwlianghehe@gmail.com
 * @date 2025/2/20 15:38
 */
package com.avalon.erp.sys.addon.document.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentFileService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "document.file";
    }

    @Override
    public String getLabel() {
        return "文件";
    }

    public Field isFolder = Fields.createBoolean("文件夹", false, false);
    public Field url = Fields.createString("url");
    public Field size = Fields.createInteger("大小");// 字节
    public Field mine = Fields.createString("文件类型");
    public Field hash = Fields.createString("文件哈希");
    public Field ownerId = Fields.createMany2one("拥有者", "base.user");
    public Field active = Fields.createBoolean("删除", false, false);// 假删除
}
