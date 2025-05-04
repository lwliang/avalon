package com.avalon.erp.sys.addon.project.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class ProjectTaskService extends AbstractService {
    @Override
    public String getServiceName() {
        return "project.task";
    }

    @Override
    public String getLabel() {
        return "任务";
    }

    public Field projectId = Fields.createMany2one("项目",
            "project.project");
    public Field userId = Fields.createMany2one("任务负责人", "base.user");
    public Field description = Fields.createText("任务描述");
    public Field startDate = Fields.createDate("任务开始日期");
    public Field endDate = Fields.createDate("任务开始日期");
    public Field state = Fields.createSelection("任务状态",
            new SelectionHashMap() {{
                put("new", "新建");
                put("process", "进行中");
                put("success", "完成");
                put("pause", "暂停");
            }});
    public Field plannedHours = Fields.createFloat("任务计划工时");
    public Field hoursSpent = Fields.createFloat("任务计划工时");
    public Field priority = Fields.createSelection("优先级",
            new SelectionHashMap() {{
                put("low", "低");
                put("middle", "中");
                put("high", "高");
                put("urgent", "紧急");
            }});
    public Field sequence = Fields.createInteger("任务排序");
    public Field progress = Fields.createFloat("任务进度");
    public Field tagIds = Fields.createMany2many("任务标签",
            "project.tag",
            "project.task.project.tags.ref",
            "taskId",
            "tagId");

    public Field remark = Fields.createText("备注");
}
