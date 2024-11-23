/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.controller;

import com.avalon.core.model.RecordRow;
import com.avalon.file.service.FileService;
import com.avalon.file.util.ZxingTool;
import com.avalon.core.exception.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class QRCodeController {

    @Autowired
    private FileService fileService;

    @ResponseBody
    @PostMapping("/barcode/upload")
    public RecordRow getBarCodeCode(@RequestParam("file") MultipartFile file) throws FileIOException {
        try {
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();

            RecordRow recordRow = fileService.saveFile(fileName, mime, content);
            String s = ZxingTool.decodeBarcode(file.getBytes());
            recordRow.put("decode", s);
            recordRow.remove("filePath");
            return recordRow;
        } catch (Exception e) {
            throw new FileIOException(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/qrcode/upload")
    public RecordRow getQRCode(@RequestParam("file") MultipartFile file) throws FileIOException {
        try {
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();

            RecordRow recordRow = fileService.saveFile(fileName, mime, content);
            String s = ZxingTool.decodeQRcode(file.getBytes());
            recordRow.put("qrcode", s);
            recordRow.remove("filePath");
            return recordRow;
        } catch (Exception e) {
            throw new FileIOException(e.getMessage());
        }
    }

}
