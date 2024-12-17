package com.avalon.core.service;

import com.avalon.core.model.Record;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/13 11:13
 */
public interface IExportImportService {
    Record exportExcel(String field, String condition, String order);

    Integer importExcel(Record record);
}
