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
public class ProjectService extends AbstractService {
    @Override
    public String getServiceName() {
        return "project.project";
    }

    @Override
    public String getLabel() {
        return "项目";
    }

    public Field code = Fields.createString("项目代码", true);
    public Field userId = Fields.createMany2one("项目负责人", "base.user");
    public Field memberIds = Fields.createMany2many("项目成员",
            "base.user",
            "project.project.base.user.ref",
            "projectId",
            "userId");
    public Field taskIds = Fields.createOne2many("项目任务",
            "project.task");
    public Field startDate = Fields.createDate("项目开始日期");
    public Field endDate = Fields.createDate("项目开始日期");
    public Field state = Fields.createSelection("项目状态",
            new SelectionHashMap() {{
                put("prepare", "准备");
                put("process", "进行中");
                put("success", "完成");
                put("pause", "暂停");
            }});
    public Field description = Fields.createText("项目描述");
    public Field plannedHours = Fields.createFloat("工时");
    public Field totalHoursSpent = Fields.createFloat("实际花费工时");
    public Field progress = Fields.createFloat("项目进度百分比");
    public Field color = Fields.createString("颜色");
}
