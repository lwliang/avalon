package com.avalon.erp.remote;

import com.avalon.core.model.RecordRow;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/25 13:47
 */
@Component
public class AvalonFileClient {
    /**
     * 上传失败返回 null
     *
     * @param file 文件内容
     * @return url
     */
    RecordRow uploadFile(@RequestPart("file") MultipartFile file) {
        return null;
    }
}
