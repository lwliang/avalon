package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.avalon.core.service.TransientService;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.StringUtils;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.JsonServiceMethodContent;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.ServiceMethodResult;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class OdooConvertService extends TransientService {
    @Override
    public String getServiceName() {
        return "odoo.convert";
    }

    public Field moduleName = Fields.createString("模块名称");

    public Field models = Fields.createOne2many("模型", "odoo.model", "convertId");

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        RecordRow row = super.create(defaultRow);
        row.put("models", new Record());
        row.put("moduleName", "base");
        return row;
    }

    public ServiceMethodResult generateAvalonModuleCode(RecordRow recordRow) {
        if (!recordRow.containsKey("moduleName") || recordRow.isNull("moduleName")) {
            throw new AvalonException("模块名称不能为空");
        }
        Record modelNewModels = recordRow.getRecord("models");
        String moduleName = recordRow.getString("moduleName");
        byte[] zipBytes = generateAvalonModuleCode(moduleName, modelNewModels);
        return ServiceMethodResult.file(moduleName + ".zip", zipBytes);
    }

    public JsonServiceMethodContent getModuleModels(RecordRow recordRow) {
        if (!recordRow.containsKey("moduleName") || recordRow.isNull("moduleName")) {
            throw new AvalonException("模块名称不能为空");
        }
        String moduleName = recordRow.getString("moduleName");
        Record record = new Record();
        // 获取模块下的所有模型
        List<HashMap<String, Object>> models = getModelNameList(moduleName);
        for (HashMap<String, Object> model : models) {
            String modelName = (String) model.get("name");
            String originalModelName = (String) model.get("originalName");
            HashMap<String, Object> modelInfo = getModelInfo(modelName); // 获取模型信息 里面有label字段
            String modelLabel = modelInfo != null ? (String) modelInfo.get("name") : capitalize(modelName); // 模型显示名称
            Integer modelId = modelInfo != null ? (Integer) modelInfo.get("id") : null; // 模型id
            RecordRow modelRow = new RecordRow();
            modelRow.put("id", modelId);
            modelRow.put("name", modelName);
            modelRow.put("label", modelLabel);
            modelRow.put("module", moduleName);
            modelRow.put("originalName", originalModelName);
            record.add(modelRow);
        }
        JsonServiceMethodContent content = new JsonServiceMethodContent();
        content.setType("form.model.update");
        content.setParam(RecordRow.build().put("models", record));
        if (record.size() == 0) {
            throw new AvalonException("未找到模型或未安装模块");
        }
        return content;
    }

    /**
     * 根据模块名生成 Avalon 对应的模块代码
     *
     * @param moduleName 模块名，例如 "sale", "purchase", "stock" 等
     * @return 压缩包的字节数组
     */
    public byte[] generateAvalonModuleCode(String moduleName, Record models) {
        if (StringUtils.isEmpty(moduleName)) {
            throw new AvalonException("模块名不能为空");
        }

        try {
            // 获取模块下的所有模型
//            List<HashMap<String, Object>> models = getModelNameList(moduleName);

            // 生成压缩包
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            // 生成 Module 代码
            String moduleCode = generateModuleCode(moduleName, models);
            addToZip(zos, moduleName + "/" + capitalize(moduleName) + "Module.java", moduleCode);

            // 为每个模型生成 Service 代码
            for (RecordRow model : models) {
                String modelName = model.getString("name");
                if (StringUtils.isEmpty(modelName))
                    continue;

                try {
                    HashMap<String, Object> modelInfo = getModelInfo(modelName); // 获取模型信息 里面有label字段
                    String modelLabel = modelInfo != null ? (String) modelInfo.get("name") : capitalize(modelName);
                    HashMap<String, HashMap<String, Object>> modelFields = getModelFields(modelName);
                    String serviceCode = generateServiceCode(moduleName, modelName, modelLabel, modelFields);
                    addToZip(zos, moduleName + "/service/" + capitalize(getValidClassName(modelName)) + "Service.java",
                            serviceCode);
                } catch (Exception e) {
                    log.error("生成模型 {} 的 Service 代码失败: {}", modelName, e.getMessage());
                    throw new AvalonException(e.getMessage(), e);
                }
            }

            zos.close();
            byte[] zipBytes = baos.toByteArray();

            log.info("成功生成模块 {} 的 Avalon 代码，包含 {} 个模型", moduleName, models.size());

            return zipBytes;

        } catch (Exception e) {
            String msg = "生成模块 " + moduleName + " 的 Avalon 代码失败";
            log.error(msg, e);
            throw new AvalonException(e.getMessage(), e);
        }
    }

    /**
     * 生成 Module 代码
     */
    private String generateModuleCode(String moduleName, Record models) {
        String className = capitalize(moduleName) + "Module";

        StringBuilder code = new StringBuilder();
        code.append("/**\n");
        code.append(" * @author Generated by OdooConvertService\n");
        code.append(" * @date ").append(new SimpleDateFormat("yyyy/MM/dd").format(new Date())).append("\n");
        code.append(" */\n\n");
        code.append("package com.avalon.erp.addon.").append(moduleName).append(";\n\n");
        code.append("import com.avalon.core.module.AbstractModule;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.stereotype.Component;\n\n");
        code.append("@Slf4j\n");
        code.append("@Component\n");
        code.append("public class ").append(className).append(" extends AbstractModule {\n");
        code.append("    @Override\n");
        code.append("    public String getModuleName() {\n");
        code.append("        return \"").append(moduleName).append("\";\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public String getLabel() {\n");
        code.append("        return \"").append(capitalize(moduleName)).append("\";\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public String getDescription() {\n");
        code.append("        return \"").append(capitalize(moduleName)).append(" 模块，包含 ").append(models.size())
                .append(" 个模型\";\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public Boolean getDisplay() {\n");
        code.append("        return true;\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public String getIcon() {\n");
        code.append("        return \"resource/").append(moduleName).append(".png\";\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * 生成 Service 代码
     */
    private String generateServiceCode(String moduleName, String modelName, String modelLabel,
                                       HashMap<String, HashMap<String, Object>> modelFields) {
        String className = getValidClassName(modelName) + "Service";
        String serviceName = modelName;

        StringBuilder code = new StringBuilder();
        code.append("/**\n");
        code.append(" * @author Generated by OdooConvertService\n");
        code.append(" * @date ").append(new SimpleDateFormat("yyyy/MM/dd").format(new Date())).append("\n");
        code.append(" */\n\n");
        code.append("package com.avalon.erp.addon.").append(moduleName).append(".service;\n\n");
        code.append("import com.avalon.core.field.Field;\n");
        code.append("import com.avalon.core.field.Fields;\n");
        code.append("import com.avalon.core.service.AbstractService;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.stereotype.Service;\n\n");
        code.append("@Slf4j\n");
        code.append("@Service\n");
        code.append("public class ").append(className).append(" extends AbstractService {\n\n");
        code.append("    @Override\n");
        code.append("    public String getServiceName() {\n");
        code.append("        return \"").append(serviceName).append("\";\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public String getLabel() {\n");
        code.append("        return \"").append(modelLabel).append("\";\n");
        code.append("    }\n\n");

        // 生成字段定义
        code.append("    // 字段定义\n");
        for (String field : modelFields.keySet()) {
            HashMap<String, Object> fieldInfo = modelFields.get(field);
            if (fieldInfo == null)
                continue;
            if (!fieldInfo.containsKey("name")){
                continue;
            }
            String fieldName = Fields.camelName(fieldInfo.get("name").toString());
            String fieldType = getFieldType(fieldInfo.get("type"));
            String fieldLabel = getFieldLabel(fieldInfo.get("string"));

            if (fieldName.equals("id") ||
                    fieldName.equals("createDate") ||
                    fieldName.equals("createUid") ||
                    fieldName.equals("writeDate") ||
                    fieldName.equals("writeUid")) {
                continue;
            }
            if (fieldName.equals("name")) {
                code.append("    @Override\n");
                code.append("    protected Field createNameField() {\n");
                code.append("        return Fields.createString(\"").append(fieldLabel).append("\");\n");
                code.append("    }\n");
                continue;
            }

            if (fieldType.equals("Many2one")) {
                code.append("    protected final Field ").append(fieldName)
                        .append(" = Fields.createMany2one(\"")
                        .append(fieldLabel).append("\", \"")
                        .append(fieldInfo.get("relation"))
                        .append("\");\n");
            } else if (fieldType.equals("One2many")) {
                code.append("    protected final Field ").append(fieldName)
                        .append(" = Fields.createOne2many(\"")
                        .append(fieldLabel).append("\", \"")
                        .append(fieldInfo.get("relation"))
                        .append("\");\n");
            } else if (fieldType.equals("Many2many")) {
                code.append("    protected final Field ").append(fieldName)
                        .append(" = Fields.createMany2many(\"")
                        .append(fieldLabel).append("\", \"")
                        .append(fieldInfo.get("relation"))
                        .append("\");\n");
            } else if (fieldType.equals("Selection")) {
                code.append("    protected final Field ").append(fieldName)
                        .append(" = Fields.createSelection(\"")
                        .append(fieldLabel).append("\", \"")
                        .append(fieldInfo.get("selection"))
                        .append("\");\n");
            } else {
                if (StringUtils.isNotEmpty(fieldName) && StringUtils.isNotEmpty(fieldType)) {
                    code.append("    protected final Field ").append(fieldName).append(" = Fields.create")
                            .append(fieldType)
                            .append("(\"").append(fieldLabel).append("\");\n");
                }
            }
        }
        code.append("\n");

        code.append("}\n");

        return code.toString();
    }

    /**
     * 首字母大写
     */
    private String capitalize(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 获取字段名，处理特殊字符
     */
    private String getFieldName(Object nameObj) {
        if (nameObj == null)
            return null;
        String name = nameObj.toString();
        if (StringUtils.isEmpty(name))
            return null;

        // 移除特殊字符，只保留字母、数字和下划线
        name = name.replaceAll("[^a-zA-Z0-9_]", "_");
        // 确保不以数字开头
        if (name.matches("^\\d.*")) {
            name = "field_" + name;
        }
        return name;
    }

    /**
     * 获取字段类型，映射到 Avalon 字段类型
     */
    private String getFieldType(Object typeObj) {
        if (typeObj == null)
            return null;
        String type = typeObj.toString().toLowerCase();

        switch (type) {
            case "char":
            case "text":
                return "String";
            case "integer":
            case "int":
                return "Integer";
            case "float":
            case "numeric":
                return "Float";
            case "boolean":
            case "bool":
                return "Boolean";
            case "date":
                return "Date";
            case "datetime":
                return "DateTime";
            case "many2one":
                return "Many2one";
            case "one2many":
                return "One2many";
            case "many2many":
                return "Many2many";
            case "binary":
                return "Binary";
            case "selection":
                return "Selection";
            default:
                return "String"; // 默认使用 String 类型
        }
    }

    /**
     * 获取字段标签，处理特殊字符
     */
    private String getFieldLabel(Object labelObj) {
        if (labelObj == null)
            return "";
        String label = labelObj.toString();
        if (StringUtils.isEmpty(label))
            return "";

        // 转义双引号
        return label.replace("\"", "\\\"");
    }

    /**
     * 获取有效的类名，处理特殊字符
     */
    private String getValidClassName(String modelName) {
        if (StringUtils.isEmpty(modelName))
            return "Unknown";

        // 移除点号和其他特殊字符，转换为驼峰命名
        String[] parts = modelName.split("\\.");
        StringBuilder className = new StringBuilder();

        for (String part : parts) {
            if (StringUtils.isNotEmpty(part)) {
                className.append(capitalize(part.replaceAll("[^a-zA-Z0-9]", "")));
            }
        }

        // 如果结果为空，使用默认名称
        if (className.length() == 0) {
            className.append("Model");
        }

        return className.toString();
    }

    /**
     * 添加文件到压缩包
     */
    private void addToZip(ZipOutputStream zos, String fileName, String content) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /**
     * 根据模块名获取 Odoo 对应模块下的所有模型
     *
     * @param modelName 模型名，例如 "sale.order", "purchase.order" 等
     * @return 模型信息，包含模型名称、显示名称等信息
     */
    public HashMap<String, Object> getModelInfo(String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            throw new AvalonException("模型名不能为空");
        }

        // 获取 Odoo 服务实例
        OdooService odooService = (OdooService) getContext().getServiceBean("odoo.service");

        // 查询 ir.model 模型，获取指定模块下的所有模型
        // 在 Odoo 中，模型名称通常以模块名开头，例如 sale.order, purchase.order 等
        List<Object> domain = List.of(List.of(Arrays.asList("model", "=", modelName)));
        // 查询字段：模型名称、显示名称、模块名等
        List<String> fields = Arrays.asList("id", "model", "name");

        // 执行查询
        List<HashMap<String, Object>> models = odooService.searchRead(
                "ir.model",
                domain,
                fields,
                null,
                "model");

        log.info("获取模型 {} 的信息: {}", modelName, models.size());

        if (models.isEmpty()) {
            log.warn("未找到模型 {} 的信息", modelName);
            return null;
        }

        return models.get(0);
    }

    /**
     * 根据模块名和模型名获取模型下的所有字段信息
     *
     * @param modelName 模型名，例如 "order", "product", "customer" 等
     * @return 字段信息列表，包含字段名称、类型、显示名称等详细信息
     */
    public HashMap<String, HashMap<String, Object>> getModelFields(String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            throw new AvalonException("模型名不能为空");
        }

        try {
            // 获取 Odoo 服务实例
            OdooService odooService = (OdooService) getContext().getServiceBean("odoo.service");

            // 获取模型的所有字段信息
            HashMap<String, HashMap<String, Object>> fields = odooService.getModelFields(
                    modelName,
                    Arrays.asList("string", "name", "type", "required", "readonly", "help",
                            "domain", "relation", "selection"));

            log.info("获取模型 {} 的字段数量: {}", modelName, fields.size());

            if (fields == null) {
                log.warn("模型 {} 的字段信息为空", modelName);
                return new HashMap<>();
            }

            return fields;

        } catch (Exception e) {
            String msg = "获取模型 " + modelName + " 的字段信息失败";
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 从 ir.model.data 获取模型数据
     *
     * @param modelName 模型名，例如 "sale.order", "purchase.order" 等
     * @return 模型数据列表，包含 name 字段
     */
    public List<HashMap<String, Object>> getModelNameList(String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            throw new AvalonException("模型名不能为空");
        }

        try {
            // 获取 Odoo 服务实例
            OdooService odooService = (OdooService) getContext().getServiceBean("odoo.service");

            // 查询 ir.model.data 模型，根据模型名筛选，并限制 model = 'ir.model'
            List<Object> domain = List.of(List.of(
                    Arrays.asList("module", "=", modelName),
                    Arrays.asList("model", "=", "ir.model")));

            // 查询字段：只返回 name 字段
            List<String> fields = Arrays.asList("name");

            // 执行查询
            List<HashMap<String, Object>> modelData = odooService.searchRead(
                    "ir.model.data",
                    domain,
                    fields,
                    null,
                    "name");

            log.info("获取模型 {} 的数据记录数量: {}", modelName, modelData.size());

            // 处理返回的数据，将 model_xxx_xxx 格式转换为 xxx.xxx 格式
            List<HashMap<String, Object>> processedData = new ArrayList<>();
            RecordRow modelNameConvert = getModelNameConvert();
            for (HashMap<String, Object> record : modelData) {
                HashMap<String, Object> processedRecord = new HashMap<>(record);
                processedRecord.put("originalName", record.get("name"));
                String name = (String) record.get("name");
                if (name != null && name.startsWith("model_")) {
                    // 移除 "model_" 前缀，并将下划线替换为点号
                    String processedName = name.substring(6).replace("_", ".");
                    if (modelNameConvert.containsKey(name)) {
                        processedName = modelNameConvert.getString(name);
                    } 

                    processedRecord.put("name", processedName);
                    processedData.add(processedRecord);
                }

            }
            return processedData;
        } catch (Exception e) {
            String msg = "获取模型 " + modelName + " 的数据失败";
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    public RecordRow getModelNameConvert() {
        RecordRow record = new RecordRow();

        record.put("model_ir_qweb_field_float_time", "ir.qweb.field.float_time");

      
        record.put("model_report_base_report_irmodulereference", "report.base.report_irmodulereference");
        
        record.put("model__unknown", "_unknown");
        record.put("model_ir_sequence_date_range", "ir.sequence.date_range");

        record.put("model_ir_qweb_field_image_url", "ir.qweb.field.image_url");
        record.put("model_ir_actions_act_url", "ir.actions.act_url");

        record.put("model_ir_actions_act_window", "ir.actions.act_window");
        record.put("model_ir_actions_act_window_close", "ir.actions.act_window_close");

        record.put("model_ir_actions_act_window_view", "ir.actions.act_window.view");
        record.put("model_ir_config_parameter", "ir.config_parameter");

        record.put("model_ir_demo_failure", "ir.demo_failure");
        record.put("model_ir_demo_failure_wizard", "ir.demo_failure.wizard");

        record.put("model_ir_mail_server", "ir.mail_server");

        record.put("model_ir_qweb_field_float_time", "ir.qweb.field.float_time");


        return record;
    }
}
