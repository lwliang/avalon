/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util.excel;

import com.avalon.core.field.Field;
import com.avalon.core.field.IFieldFormat;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;


/**
 * description: ExcelUtil
 * date: 2022/5/20 16:19
 * author: AN
 * version: 1.0
 */
@Component
public class ExcelUtil {

    /**
     * 读取excel内容
     * <p>
     * 用户模式下：
     * 弊端：对于少量的数据可以，单数对于大量的数据，会造成内存占据过大，有时候会造成内存溢出
     * 建议修改成事件模式
     */
    public Record readExcel(InputStream in) throws Exception {

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        Record record = new Record();

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheetAt.getRow(0);
        // 循环获取每一行数据
        for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            RecordRow map = new RecordRow();
            // 读取每一格内容
            for (int index = 0; index < row.getLastCellNum(); index++) {
                String title = titleRow.getCell(index).getStringCellValue().trim();
                XSSFCell cell = row.getCell(index);

                if (ObjectUtils.isNull(cell)) {
                    map.put(title, "");
                    continue;
                }

                switch (cell.getCellType()) {
                    case STRING:
                        map.put(title, cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            map.put(
                                    title,
                                    DateTimeUtils.getDateTimeFormat(
                                            "yyyy-MM-dd HH:mm:ss",
                                            cell.getDateCellValue()
                                    )
                            );
                            break;
                        }
                        // 除了时间都转换为字符串
                        cell.setCellType(CellType.STRING);
                        map.put(title, cell.getStringCellValue());
                        break;
                }
            }

            if (map.isEmpty()) {
                continue;
            }

            record.add(map);
        }
        return record;
    }

    /**
     * 创建excel xlsx
     *
     * @param record
     * @param fields
     * @param service
     * @return
     */
    public static XSSFWorkbook createExcel(Record record,
                                           List<String> fields,
                                           AbstractService service) {
        //1.创建一个工作簿  07XSSFWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //2.创建一个工作表
        Sheet sheet = workbook.createSheet("z账单");

        renderHeaderRow(sheet, fields, service);

        renderBodyRow(sheet, 1, record, fields, service);

        return workbook;
    }


    /**
     * 渲染excel内容行
     *
     * @param sheet
     * @param beginRowIndex 开始行索引
     * @param data          数据
     * @param fields        字段 用于控制显示顺序
     */
    public static void renderBodyRow(Sheet sheet,
                                     Integer beginRowIndex,
                                     Record data,
                                     List<String> fields,
                                     AbstractService service) {
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            Row row = sheet.createRow(beginRowIndex + rowIndex);
            RecordRow recordRow = data.get(rowIndex);
            for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                Cell cell = row.createCell(colIndex);
                Field field = service.getField(fields.get(colIndex));
                String joinDisplayString = FieldUtils.getJoinDisplayString(fields.get(colIndex));
                if (recordRow.isNotNull(joinDisplayString)) {
                    if (ObjectUtils.isNull(field)) {
                        cell.setCellValue(recordRow.getString(joinDisplayString));
                    } else {
                        if (field instanceof IFieldFormat) {
                            Object parse = field.getSqlValue(recordRow.getRawValue(joinDisplayString));
                            cell.setCellValue(parse.toString());
                        } else {
                            cell.setCellValue(recordRow.getString(joinDisplayString));
                        }
                    }

                }
            }
        }
    }

    /**
     * 初始化表头
     *
     * @param fields
     * @param service
     */
    public static void renderHeaderRow(Sheet sheet,
                                       List<String> fields,
                                       AbstractService service) {

        Row row = sheet.createRow(0);

        for (int i = 0; i < fields.size(); i++) {
            Cell cell = row.createCell(i);
            Field field = service.getField(fields.get(i));
            if (ObjectUtils.isNull(field)) {
                cell.setCellValue(fields.get(i));
            } else {
                cell.setCellValue(field.getLabel());
            }
        }
    }
}
