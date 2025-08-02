package com.avalon.core.model.report.table;

import java.util.List;

import com.avalon.core.model.Record;

import lombok.Data;

@Data
public class ReportTable {
    private List<ReportTableColumn> columns;
    private Record data;

    // 分页信息 默认显示全部数据
    private Integer total;//总个数
    private Integer pageCur;//当前页数
    private Integer pageSize;//每页大小
    private Integer pageCount;//页面个数
}
