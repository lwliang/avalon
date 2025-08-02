package com.avalon.core.service;

import org.springframework.stereotype.Service;

import com.avalon.core.model.RecordRow;
import com.avalon.core.model.report.table.ReportTable;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public abstract class AbstractReportService extends AbstractService {
    @Override
    public Boolean needDefaultNameField() {
        return false;
    }
    
    @Override
    public Boolean getNeedDefaultField() { //报表模型不生成默认字段 字段有 createTime,creator,updateTime,updater
        return false;
    }

    @Override
    public Boolean getNeedDefaultKeyField() { //报表模型不生成默认主键字段 主键有 id
        return false;
    }

    /**
     * 默认获取报表数据
     * @param recordRow 参数 来自自身字段
     * @return 报表值，含列，与数据
     */
    public ReportTable getReportTable(RecordRow recordRow) {
        return new ReportTable();
    }
}
