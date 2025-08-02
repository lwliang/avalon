package com.avalon.core.model;

import lombok.Data;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/07/30 11:11
 */
@Data
public class ServiceMethodResult {
    private ServiceMethodResultTypeEnum type;
    private Object result; //
    private String fileName; // 只有type为file的情况下使用

    public static ServiceMethodResult json(Object result) {
        ServiceMethodResult jsonResult = new ServiceMethodResult();
        jsonResult.setType(ServiceMethodResultTypeEnum.json);
        jsonResult.setResult(result);
        return jsonResult;
    }

    public static ServiceMethodResult file(String fileName, byte[] fileBytes) {
        ServiceMethodResult fileResult = new ServiceMethodResult();
        fileResult.setType(ServiceMethodResultTypeEnum.file);
        fileResult.setFileName(fileName);
        fileResult.setResult(fileBytes);
        return fileResult;
    }
}













































































































































