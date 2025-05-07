/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.ParamCheckException;
import com.avalon.core.field.Field;
import com.avalon.core.field.FieldList;
import com.avalon.core.field.SelectionField;
import com.avalon.core.model.ChangeRecordRow;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.sys.addon.base.model.*;
import com.avalon.erp.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service")
public class ServiceV2Controller {

    @Autowired
    private Context context;

    @PostMapping("{serviceName}/create")
    public RecordRow createModel(@PathVariable("serviceName") String serviceName,
                                 @RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        return serviceBean.create(ObjectUtils.isNull(param.getValue()) ? RecordRow.build() : param.getValue());
    }

    @PostMapping("{serviceName}/add")
    public RecordRow addModel(@PathVariable("serviceName") String serviceName,
                              @RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow recordRow = param.getValue();

        return RecordRow.build().put(serviceBean.getPrimaryKeyName(), serviceBean.insert(recordRow));
    }

    @PostMapping("{serviceName}/update")
    public RecordRow updateModel(@PathVariable("serviceName") String serviceName,
                                 @RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow recordRow = param.getValue();

        return RecordRow.build().put("count", serviceBean.update(recordRow));
    }

    @PostMapping("{serviceName}/delete")
    public Integer deleteModel(@PathVariable("serviceName") String serviceName,
                               @RequestBody ServiceModelId param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        if (ObjectUtils.isNull(param.getId()) && ObjectUtils.isNull(param.getIds())) {
            throw new ParamCheckException("id与ids 必须输入一个");
        }
        Record select;
        if (ObjectUtils.isNotNull(param.getId())) {
            select = serviceBean.select(
                    Condition.equalCondition(serviceBean.getPrimaryKeyName(), param.getId()),
                    serviceBean.getAllFieldName().toArray(new String[0]));

            if (select.isEmpty()) {
                throw new ParamCheckException("数据不存在,无法删除");
            }
            serviceBean.delete(select.get(0));
        } else {
            select = serviceBean.select(
                    Condition.inCondition(serviceBean.getPrimaryKeyName(), param.getIds()),
                    serviceBean.getAllFieldName().toArray(new String[0]));

            if (select.isEmpty()) {
                throw new ParamCheckException("数据不存在,无法删除");
            }
            serviceBean.deleteMulti(select);
        }

        return select.size();
    }


    @PostMapping("get/{serviceName}/detail")
    public RecordRow getDetail(@PathVariable("serviceName") String serviceName,
                               @RequestBody ServiceModelField param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        Condition condition = context.conditionManager.interpreter(param.getCondition());
        Record select = serviceBean.select(condition, FieldUtils.getFieldArray(param.getFields()));
        if (select.isEmpty()) {
            return RecordRow.build();
        }
        return select.get(0);
    }


    /**
     * 获取全部数据
     *
     * @param serviceConditionPage
     * @return
     */
    @PostMapping("get/{serviceName}/all")
    public Record getAll(@PathVariable("serviceName") String serviceName,
                         @RequestBody ServiceModelField serviceConditionPage) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        return serviceBean.select(serviceConditionPage.getOrder(),
                context.conditionManager.interpreter(serviceConditionPage.getCondition()),
                FieldUtils.getFieldList(serviceConditionPage.getFields()).toArray(new String[0]));
    }

    @PostMapping("/get/{serviceName}/page")
    public PageInfo getPage(@PathVariable("serviceName") String serviceName,
                            @RequestBody ServiceModelPage serviceModelPage) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);

        return serviceBean.selectPage(serviceModelPage.getPage(),
                serviceModelPage.getOrder(),
                context.conditionManager.interpreter(serviceModelPage.getCondition()),
                FieldUtils.getFieldList(serviceModelPage.getFields()).toArray(new String[0]));
    }

    @PostMapping("get/{serviceName}/fields")
    public Record getModelFieldList(@PathVariable("serviceName") String serviceName,
                                    @RequestBody RecordRow param) throws AvalonException {
        String field = "";
        if (param.containsKey("field")) {
            field = param.getString("field");
        }
        AbstractService serviceBean = context.getServiceBean("base.field");
        String fields = "id,label,name,isPrimaryKey,isAutoIncrement,isRequired,isReadonly,defaultValue," +
                "type,serviceId,isUnique,allowNull,minValue,maxValue,masterForeignKeyName,relativeForeignKeyName," +
                "relativeServiceName,manyServiceTable,relativeFieldName";
        Condition condition = Condition.equalCondition("serviceId.name", serviceName);
        if (StringUtils.isNotEmpty(field)) {
            condition = Condition.andCondition(condition, Condition.likeCondition("label", field));
        }

        return serviceBean.select(condition, FieldUtils.getFieldArray(fields));
    }

    /**
     * description: 获取serviceBean 里面的 selection 值，没有做传值判断
     *
     * @param param
     * @return com.avalon.core.model.RecordRow
     */
    @PostMapping("get/{serviceName}/selection/map")
    public RecordRow getSelection(@PathVariable("serviceName") String serviceName,
                                  @RequestBody ServiceModelField param) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow row = new RecordRow();

        SelectionField selectionField = (SelectionField) serviceBean.getField(param.getFields());
        selectionField.getSection().forEach(((o, s) -> {
            row.put(o.toString(), s);
        }));

        return row;
    }

    @PostMapping("export/{serviceName}/excel")
    public void export(@PathVariable("serviceName") String serviceName, @RequestBody RecordRow param,
                       HttpServletResponse response) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        String fields = param.getString("field");
        String condition = param.getString("condition");
        String order = param.getString("order");

        Record recordRows = serviceBean.exportExcel(fields, condition, order);

        XSSFWorkbook excel = ExcelUtil.createExcel(recordRows, Arrays.asList(fields.split(",")), serviceBean);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excel.write(outputStream);
// 设置 HTTP 响应头以触发文件下载
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode(serviceBean.getLabel() + ".xlsx", StandardCharsets.UTF_8));
            response.setContentLength(outputStream.size());

            // 7. 将字节流写入响应输出流
            response.getOutputStream().write(outputStream.toByteArray());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new AvalonException(e.getMessage(), e);
        }
    }

    /**
     * 读取Excel内容，并且获取对应的格式内容
     *
     * @param serviceName 服务
     * @param file        excel文件
     * @return
     */
    @PostMapping("/read/{serviceName}/excel")
    public RecordRow readExcelContent(@PathVariable("serviceName") String serviceName,
                                      @RequestParam("file") MultipartFile file) {
        AbstractService serviceBean = context.getServiceBean(serviceName);

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // 读取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = ExcelUtil.readFirstRow(sheet);

            // 获取headers 对应的字段名称
            List<String> fields = new ArrayList<>();
            for (String header : headers) {
                FieldList serviceBeanFields = serviceBean.getFields();
                Optional<Field> first = serviceBeanFields.stream().filter(field -> field.getLabel().equals(header)).findFirst();
                fields.add(first.isPresent() ? first.get().getName() : "");
            }

            // 读取数据行
            List<RecordRow> dataRows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                RecordRow dataRow = new RecordRow();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    dataRow.put(headers.get(j), ExcelUtil.getCellValue(cell));
                }
                dataRows.add(dataRow);
            }

            // 将数据转换为 RecordRow
            RecordRow result = new RecordRow();
            result.put("headers", headers);
            result.put("fields", fields);
            result.put("data", dataRows);

            return result;
        } catch (IOException e) {
            throw new RuntimeException("读取 Excel 文件失败：" + e.getMessage(), e);
        }
    }

    @PostMapping("import/{serviceName}/excel")
    public RecordRow importExcel(@PathVariable("serviceName") String serviceName, @RequestBody RecordRow param) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        List<String> headers = param.getRawValue("headers");
        List<String> fields = param.getRawValue("fields");
        Record data = param.getRawValue("data");

        Record newRecord = Record.build();

        for (RecordRow datum : data) {
            RecordRow row = RecordRow.build();
            for (int i = 0; i < headers.size(); i++) {
                Object rawValue = datum.getRawValue(headers.get(i));
                row.put(fields.get(i), rawValue);
            }
            newRecord.add(row);
        }

        Integer i = serviceBean.importExcel(newRecord);

        return RecordRow.build().put("imported", i);
    }

    @PostMapping("invoke/{serviceName}/method")
    public Object invokeService(@PathVariable("serviceName") String serviceName,
                                @RequestBody ServiceInvokeParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        return serviceBean.invokeMethod(param.getServiceName(), param.getMethod(), param.getParam());
    }

    @PostMapping("get/{serviceName}/onchange/field")
    public List<String> getOnChangeFields(@PathVariable("serviceName") String serviceName) {
        AbstractService serviceBean = context.getServiceBean(serviceName);

        return serviceBean.getOnChangeFields();
    }

    @PostMapping("value/{serviceName}/onchange")
    public ChangeRecordRow onChangeRecordRow(@PathVariable("serviceName") String serviceName,
                                             @RequestBody ServiceChangeParam param) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        return serviceBean.onChange(param.getChangeFieldRow(), param.getNewRow(), param.getOldRow());
    }
}
