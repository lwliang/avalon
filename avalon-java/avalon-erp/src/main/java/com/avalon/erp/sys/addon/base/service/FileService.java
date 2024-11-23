/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileService extends AbstractService {
    @Override
    public String getServiceName() {
        return "file.upload";
    }

    public final Field originName = Fields.createString("源文件名");//包含后缀
    public final Field url = Fields.createString("url");
    public final Field mime = Fields.createString("web文件类型", 100);
    public final Field ext = Fields.createString("文件扩展名", 20);
    public final Field filePath = Fields.createString("文件存放路径");
    public final Field thumbPath = Fields.createString("缩略图");

    @Override
    protected Field createNameField() {
        return Fields.createString("文件名", 256);
    }
}

