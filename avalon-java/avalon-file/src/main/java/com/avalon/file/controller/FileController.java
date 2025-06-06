/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.controller;

import com.avalon.core.model.Record;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.util.ClassUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.file.config.FileConfig;
import com.avalon.core.exception.FileIOException;
import com.avalon.file.enums.CacheTypeEnum;
import com.avalon.file.minio.MinioService;
import com.avalon.file.service.FileService;
import com.avalon.core.condition.Condition;
import com.avalon.core.condition.EqualCondition;
import com.avalon.core.context.Context;
import com.avalon.core.model.RecordRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
public class FileController {
    @Autowired
    private Context context;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private MinioService minioService;

    @Value("${application.cache-type}")
    private CacheTypeEnum cacheType;

    @ResponseBody
    @GetMapping("/test")
    public RecordRow test() {
        RecordRow recordRow = new RecordRow();
        recordRow.put("result", "OK");
        return recordRow;
    }

    @ResponseBody
    @PostMapping("/file/upload")
    public RecordRow fileUpload(@RequestParam("file") MultipartFile file) throws FileIOException {
        try {
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();
            RecordRow result = RecordRow.build();
            if (cacheType == CacheTypeEnum.file) {
                RecordRow recordRow = fileService.saveFile(fileName, mime, content);
                result.put("url", recordRow.get("url"));
                result.put("originName", recordRow.get("originName"));
                result.put("mine", recordRow.get("mime"));
                result.put("size", recordRow.get("size"));
            } else {
                String minioFileName = minioService.getFileName(file.getOriginalFilename());
                minioService.uploadFile(context.getBaseName(), minioFileName, file.getInputStream(), file.getContentType());
                String url = "/minio/down/" + context.getBaseName() + "/" + minioFileName;
                fileService.saveMinioRecord(file.getOriginalFilename(), mime, url, content.length);
                result.put("url", url);
                result.put("originName", file.getOriginalFilename());
                result.put("mine", mime);
                result.put("size", content.length);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/image/upload")
    public RecordRow imageUpload(@RequestParam("file") MultipartFile file) throws FileIOException {
        try {
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();

            RecordRow result = RecordRow.build();
            if (cacheType == CacheTypeEnum.file) {
                RecordRow recordRow = fileService.saveFile(fileName, mime, content);
                result.put("url", recordRow.get("url"));
                result.put("originName", recordRow.get("originName"));
                result.put("mine", recordRow.get("mime"));
                result.put("size", recordRow.get("size"));
            } else {
                String minioFileName = minioService.getFileName(file.getOriginalFilename());
                minioService.uploadFile(context.getBaseName(), minioFileName, file.getInputStream(), file.getContentType());
                String url = "/minio/down/" + context.getBaseName() + "/" + minioFileName;
                fileService.saveMinioRecord(file.getOriginalFilename(), mime, url, content.length);
                result.put("url", url);
                result.put("originName", file.getOriginalFilename());
                result.put("mine", mime);
                result.put("size", content.length);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/video/upload")
    public RecordRow videoUpload(@RequestParam("file") MultipartFile file) throws FileIOException {
        try {
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();

            RecordRow result = RecordRow.build();
            if (cacheType == CacheTypeEnum.file) {
                RecordRow recordRow = fileService.saveFile(fileName, mime, content);
                result.put("url", recordRow.get("url"));
                result.put("originName", recordRow.get("originName"));
                result.put("mine", recordRow.get("mime"));
                result.put("size", recordRow.get("size"));
            } else {
                String minioFileName = minioService.getFileName(file.getOriginalFilename());
                minioService.uploadFile(context.getBaseName(), minioFileName, file.getInputStream(), file.getContentType());
                String url = "/minio/down/" + context.getBaseName() + "/" + minioFileName;
                fileService.saveMinioRecord(file.getOriginalFilename(), mime, url, content.length);
                result.put("url", url);
                result.put("originName", file.getOriginalFilename());
                result.put("mine", mime);
                result.put("size", content.length);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/file/upload/database")
    public RecordRow fileUploadWithDatabase(@RequestParam("file") MultipartFile file, @RequestParam("db") String db) throws FileIOException {
        try {
            context.init(db);
            String fileName = file.getOriginalFilename();
            String mime = file.getContentType();
            byte[] content = file.getBytes();

            RecordRow result = RecordRow.build();
            if (cacheType == CacheTypeEnum.file) {
                RecordRow recordRow = fileService.saveFile(fileName, mime, content);
                result.put("url", recordRow.get("url"));
                result.put("originName", recordRow.get("originName"));
                result.put("mine", recordRow.get("mime"));
                result.put("size", recordRow.get("size"));
            } else {
                String minioFileName = minioService.getFileName(file.getOriginalFilename());
                minioService.uploadFile(context.getBaseName(), minioFileName, file.getInputStream(), file.getContentType());
                String url = minioFileName + "/" + minioFileName + "?cache=minio";
                fileService.saveMinioRecord(file.getOriginalFilename(), mime, url, content.length);
                result.put("url", url);
                result.put("originName", file.getOriginalFilename());
                result.put("mine", mime);
                result.put("size", content.length);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException(e.getMessage());
        }
    }

    @RequestMapping(value = {"minio/down/{bucket}/{first}/{second}/{filename}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public void minioDownloadFile(@PathVariable(value = "bucket", required = false) String bucket,
                                  @PathVariable("first") String first,
                                  @PathVariable("second") String second,
                                  @PathVariable("filename") String filename,
                                  HttpServletResponse response) throws IOException {
        byte[] bytes = minioService.downloadFile(bucket, first + "/" + second + "/" + filename);
        response.setHeader("content-disposition", "attachment;fileName=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8));
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = {"file/down/{first}/{second}/{filename}",
            "file/down/{db}/{first}/{second}/{filename}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadFile(@PathVariable(value = "db", required = false) String db,
                             @PathVariable("first") String first,
                             @PathVariable("second") String second,
                             @PathVariable("filename") String filename,
                             HttpServletResponse response) throws IOException {

        String fullFileName = "";
        if (StringUtils.isNotEmpty(db)) {
            fullFileName = db + File.separator;
        }
        fullFileName += first +
                File.separator + second +
                File.separator + filename;

        String filePath = fileConfig.getFile() + fullFileName;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileIOException(String.format("文件不存在:%s", filePath));
        }

        try {
            FileChannel channel = new FileInputStream(file).getChannel();

            long size = channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) size);

            channel.read(byteBuffer);
            channel.close();
            byte[] content = byteBuffer.array();
            Condition condition = new EqualCondition("name", filename);
//            Record select = fileService.select(condition, "mime", "originName");
//            if (select.isEmpty()) {
//                throw new FileIOException("文件不存在");
//            }
//            RecordRow recordRow = select.get(0);
            //设置下载响应头
            //response.setContentType(recordRow.get("mime").toString());
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            throw new FileIOException("文件读取发生错误");
        }
    }

    @RequestMapping(value = {"video/down/{db}/{first}/{second}/{filename}"},
            method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadVideoFile(@PathVariable(value = "db", required = false) String db,
                                  @PathVariable("first") String first,
                                  @PathVariable("second") String second,
                                  @PathVariable("filename") String filename,
                                  HttpServletResponse response) throws IOException {

        String fullFileName = "";
        if (StringUtils.isNotEmpty(db)) {
            fullFileName = db + File.separator;
        }
        fullFileName += first +
                File.separator + second +
                File.separator + filename;

        String filePath = fileConfig.getVideo() + fullFileName;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileIOException(String.format("文件不存在:%s", filePath));
        }

        try {
            FileChannel channel = new FileInputStream(file).getChannel();

            long size = channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) size);

            channel.read(byteBuffer);
            channel.close();
            byte[] content = byteBuffer.array();
            Condition condition = new EqualCondition("name", filename);
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            throw new FileIOException("文件读取发生错误");
        }
    }

    @RequestMapping(value = {"image/down/{db}/{first}/{second}/{filename}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadImageFile(@PathVariable(value = "db", required = false) String db,
                                  @PathVariable("first") String first,
                                  @PathVariable("second") String second,
                                  @PathVariable("filename") String filename,
                                  HttpServletResponse response) throws IOException {

        String fullFileName = "";
        if (StringUtils.isNotEmpty(db)) {
            fullFileName = db + File.separator;
        }
        fullFileName += first +
                File.separator + second +
                File.separator + filename;

        String filePath = fileConfig.getImage() + fullFileName;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileIOException(String.format("文件不存在:%s", filePath));
        }

        try {
            FileChannel channel = new FileInputStream(file).getChannel();

            long size = channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) size);

            channel.read(byteBuffer);
            channel.close();
            byte[] content = byteBuffer.array();
            Condition condition = new EqualCondition("name", filename);
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            throw new FileIOException("文件读取发生错误");
        }
    }

    @GetMapping("/user/default/avatar")
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, URISyntaxException {
        try {
            String path = "admin.png";

            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
            byte[] content = resourceAsStream.readAllBytes();

            //设置下载响应头
            response.setContentType("image/png");
            response.setHeader("content-disposition", "attachment;fileName=" +
                    URLEncoder.encode("defaultIcon", StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (FileIOException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException("默认头像读取发生错误");
        }
    }
}
