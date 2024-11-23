/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.addon.bank.service.SchoolCourseService;
import com.avalon.erp.addon.bank.service.SchoolService;
import com.avalon.erp.addon.odoo.service.OdooService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("school")
@Slf4j
public class SchoolController {
    private final SchoolService schoolService;
    private final SchoolCourseService schoolCourseService;

    private final OdooService odooService;

    public SchoolController(SchoolService schoolService,
                            SchoolCourseService schoolCourseService,
                            OdooService odooService) {
        this.schoolService = schoolService;
        this.schoolCourseService = schoolCourseService;
        this.odooService = odooService;
    }

    @PostMapping("/get/school/name")
    public List<HashMap<String, Object>> getSchool(@RequestBody Map<String, Object> param) {
        String name = null;
        if (param.containsKey("name")) {
            name = param.get("name").toString();
        }
        List<Object> domain = List.of("name", "ilike", StringUtils.isEmpty(name) ? "" : name);
        List<HashMap<String, Object>> schools = odooService.searchRead("study.school.school", List.of(List.of(domain)),
                List.of("name"), null, "sequence asc,id asc");

        Set<Object> seen = new HashSet<>();
        List<HashMap<String, Object>> result = new ArrayList<>();

        for (HashMap<String, Object> map : schools) {
            Object nameKey = map.get("name");
            if (seen.add(nameKey)) {
                result.add(map);
            }
        }
        return result;
    }

    @PostMapping("/get/course/name")
    public List<HashMap<String, Object>> getCourse(@RequestBody Map<String, Object> param) {
        String schoolName = null;
        if (param.containsKey("schoolName")) {
            schoolName = param.get("schoolName").toString();
        }
        List<Object> domain = List.of("name", "=", StringUtils.isEmpty(schoolName) ? "" : schoolName);
        List<HashMap<String, Object>> schools = odooService.searchRead("study.school.school", List.of(List.of(domain)),
                List.of("name", "course", "collect_info", "amount"), null, "sequence asc,id asc");

        Set<Object> seen = new HashSet<>();
        List<HashMap<String, Object>> result = new ArrayList<>();

        for (HashMap<String, Object> map : schools) {
            Object nameKey = map.get("course");
            if (seen.add(nameKey)) {
                result.add(map);
            }
        }
        return result;
    }

    @PostMapping("/create/course/order")
    public RecordRow courseOrder(@RequestBody Map<String, Object> param) {
        String schoolName = null;
        if (param.containsKey("schoolName")) {
            schoolName = param.get("schoolName").toString();
        }
        String courseName = null;
        if (param.containsKey("courseName")) {
            courseName = param.get("courseName").toString();
        }
        String amount = null;
        if (param.containsKey("amount")) {
            amount = param.get("amount").toString();
        }
        Boolean collect_info = null;
        if (param.containsKey("collectInfo")) {
            collect_info = Boolean.parseBoolean(param.get("collectInfo").toString());
        }

        String student_name = null;
        if (param.containsKey("student_name")) {
            student_name = param.get("student_name").toString();
        }
        String grade = null;
        if (param.containsKey("grade")) {
            grade = param.get("grade").toString();
        }
        String class_name = null;
        if (param.containsKey("class_name")) {
            class_name = param.get("class_name").toString();
        }
        String student_id_card = null;
        if (param.containsKey("student_id_card")) {
            student_id_card = param.get("student_id_card").toString();
        }
        String look_phone = null;
        if (param.containsKey("look_phone")) {
            look_phone = param.get("look_phone").toString();
        }
        String parent_name = null;
        if (param.containsKey("parent_name")) {
            parent_name = param.get("parent_name").toString();
        }
        String parent_phone = null;
        if (param.containsKey("parent_phone")) {
            parent_phone = param.get("parent_phone").toString();
        }
        String data_receiving_address = null;
        if (param.containsKey("data_receiving_address")) {
            data_receiving_address = param.get("data_receiving_address").toString();
        }
        String remark = null;
        if (param.containsKey("remark")) {
            remark = param.get("remark").toString();
        }
        Map<String, Object> model = new HashMap<>();
        model.put("order_no", String.valueOf(SnowflakeIdWorker4rd.nextUId()));
        model.put("school_name", schoolName);
        model.put("course", courseName);
        model.put("collect_info", collect_info);
        model.put("amount", amount);
        model.put("student_name", student_name);
        model.put("grade", grade);
        model.put("class_name", class_name);
        model.put("look_phone", look_phone);
        model.put("student_id_card", student_id_card);
        model.put("parent_name", parent_name);
        model.put("parent_phone", parent_phone);
        model.put("data_receiving_address", data_receiving_address);
        model.put("remark", remark);

        odooService.create("study.school.order", model);

        RecordRow res = RecordRow.build();
        res.put("order_no", model.get("order_no"));
        return res;
    }


    @PostMapping("/import/school")
    public RecordRow importSchoolFromExcel(@RequestParam("file") MultipartFile file) {
        RecordRow result = RecordRow.build();
        int code = 0;
        int sumCount = 0;
        int successCount = 0;
        int failCount = 0;
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheetAt = workbook.getSheetAt(0); // 只有第一页有效
            sumCount = sheetAt.getPhysicalNumberOfRows() - 1;

            Row row = null;
            for (int i = 1; i < sumCount + 1; i++) { // 过滤head
                row = sheetAt.getRow(i);
                String school = row.getCell(0).toString().trim();
                String course = row.getCell(1).toString().trim();
                String collectInfo = row.getCell(2).toString().trim();
                BigDecimal amount = new BigDecimal(row.getCell(3).toString().trim());

                RecordRow courseRow = RecordRow.build();
                courseRow.put("name", course);
                courseRow.put("collectInfo", false);
                if ("是".equals(collectInfo)) {
                    courseRow.put("collectInfo", true);
                }
                courseRow.put("amount", amount);

                Record select = schoolService.select(Condition.equalCondition("name", school), "id");
                Integer schoolId = 0;
                if (!select.isEmpty()) {
                    log.info("存在学校{}", school);
                    schoolId = select.get(0).getInteger("id");

                    try {
                        Record courseRecord = schoolCourseService.select(Condition.equalCondition("name", course)
                                .andEqualCondition("schoolId", schoolId), "id");
                        if (!courseRecord.isEmpty()) {
                            log.info("存在课程{}", course);
                            courseRow.put("id", courseRecord.get(0).getInteger("id"));
                            schoolCourseService.update(courseRow);
                        } else {
                            log.info("新增课程{}", course);
                            courseRow.put("schoolId", schoolId);
                            schoolCourseService.insert(courseRow);
                        }

                        successCount++;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        failCount++;
                    }
                } else {
                    RecordRow schoolRow = RecordRow.build();
                    schoolRow.put("name", school);
                    Record courseRecord = Record.build();
                    courseRecord.add(courseRow);
                    schoolRow.put("courseIds", courseRecord);
                    try {
                        log.info("新增学校{}", JacksonUtil.object2String(schoolRow));
                        schoolService.insert(schoolRow);
                        successCount++;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        failCount++;
                    }
                }
            }

        } catch (IOException e) {
            code = 998;
            log.error(e.getMessage(), e);
        }
        result.put("code", code);
        result.put("sumCount", sumCount);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }
}
