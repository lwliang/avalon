package com.avalon.file.minio;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.BCryptUtil;
import com.avalon.core.util.FileUtils;
import com.avalon.file.config.MinioConfig;
import com.avalon.file.util.PathUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/23 17:26
 */
@Slf4j
@Component
public class MinioService {
    @Autowired
    private MinioConfig minioConfig;
    private io.minio.MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = io.minio.MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                .build();
    }

    /**
     * 获取存储的唯一名字
     *
     * @param fileName 原始名称
     * @return uuid文件名
     */
    public String getFileName(String fileName) {
        String token = BCryptUtil.simpleUUID();
        String first = PathUtil.getFirst(minioConfig.getMode());
        String second = PathUtil.getSecond(minioConfig.getMode());


        String uuidFileName = PathUtil.getUUIDFileName(token, fileName);

        return first + "/" + second + "/" + uuidFileName;
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucketName
     * @param objectName  对象名
     * @param inputStream 对象内容
     * @throws AvalonException 错误
     */
    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws AvalonException {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            if (!minioClient.bucketExists(bucketExistsArgs)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AvalonException(e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     *
     * @param bucketName bucketName
     * @param objectName 文件名
     * @throws AvalonException 错误
     */
    public byte[] downloadFile(String bucketName, String objectName) throws AvalonException {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            return minioClient.getObject(getObjectArgs).readAllBytes();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AvalonException(e.getMessage(), e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName bucketName
     * @param objectName 文件名
     * @throws AvalonException 错误
     */
    public void deleteFile(String bucketName, String objectName) throws AvalonException {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AvalonException(e.getMessage(), e);
        }
    }
}
