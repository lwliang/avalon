package com.avalon.erp.remote;

import com.avalon.core.model.RecordRow;
import com.avalon.core.remote.AvalonRemoteClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/25 13:46
 */
@FeignClient(name = "avalon-file", fallback = AvalonFileClient.class)
public interface IAvalonFileClient extends AvalonRemoteClient {
    @PostMapping(value = "/file/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RecordRow uploadFile(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/file/file/upload/database", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RecordRow uploadFileWithDatabase(@RequestPart("file") MultipartFile file, @RequestPart("db") String db);
}
