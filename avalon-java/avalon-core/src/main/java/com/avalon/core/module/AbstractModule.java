/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.module;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.xml.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractServiceList;
import com.avalon.core.service.IServiceDataService;
import com.avalon.core.service.TransientService;
import com.avalon.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public abstract class AbstractModule {
    @Autowired
    private Context context;

    public Context getContext() {
        return context;
    }

    public AbstractModule getModule() {
        return context.getModule(this.getClass());
    }

    public abstract String getModuleName();


    public abstract String getLabel();


    public abstract String getDescription();

    /// 安装之后，是否显示在菜单中
    public abstract Boolean getDisplay();

    public String[] depends() {
        return null;
    }

    /**
     * 自动安装,true，则depends的模块已安装，则自动安装当前模块
     *
     * @return
     */
    public Boolean autoInstall() {
        return false;
    }

    private boolean isInstall = false;

    public boolean getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(boolean isInstall) {
        this.isInstall = isInstall;
    }

    /**
     * 模块安装之后，允许js
     *
     * @return js路径
     */
    public String[] getStartJS() {
        return null;
    }

    /**
     * 模块依赖的js
     *
     * @return js路径
     */
    public String[] getWebJS() {
        return null;
    }

    public String[] getVue() {
        return null;
    }

    /**
     * 模块图标
     *
     * @return url 本地文件
     */
    public String getIcon() {
        return null;
    }

    private final AbstractServiceList moduleServiceList = new AbstractServiceList();

    public AbstractServiceList getServiceList() {
        if (ObjectUtils.isNotEmpty(moduleServiceList)) {
            return moduleServiceList;
        }

        synchronized (this) {
            if (ObjectUtils.isNotEmpty(moduleServiceList)) {
                return moduleServiceList;
            }
            Hashtable<String, AbstractService> serviceClassServiceNameDic = context.getServiceClassServiceNameDic();

            for (Map.Entry<String, AbstractService> serviceName : serviceClassServiceNameDic.entrySet()) {
                AbstractService serviceBean = serviceName.getValue();
                if (serviceName.getKey().startsWith(getClass().getPackageName())) {
                    moduleServiceList.add(serviceBean);
                }
            }
        }
        return moduleServiceList;
    }


    public String[] getResource() {
        return new String[]{};
    }

    /**
     * 加载文件
     *
     * @param fileName 文件名
     * @throws IOException 加载错误
     */
    public void loadData(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = this.getClass().getPackageName().replaceAll("\\.", "/");
        path += "/data/";
        InputStream resourceAsStream = classLoader.getResourceAsStream(path + fileName);
        byte[] content = resourceAsStream.readAllBytes();
    }

    @PostConstruct
    public void postConstruct() {
        addModule();
    }

    protected void addModule() {
        if (log.isDebugEnabled()) {
            log.debug("模块初始化->" + getModuleName());
        }
        try {
            if (context.getAvalonApplicationContext().isActive()) {
                String firstBeanName = context.getFirstBeanName(this.getClass());
                if (StringUtils.isNotEmpty(firstBeanName) && StringUtils.isNotEmpty(getModuleName())) {
                    context.registerAlias(firstBeanName, getModuleName());
                    log.debug("registerAlias module {}->{}", firstBeanName, getModuleName());
                }
            }
        } catch (AvalonException e) {
            log.error("bean 注册别名失败,错误信息->" + e.getMessage());
            log.error(e.getMessage(), e);
        }
        getContext().addModule(this);
    }

    /**
     * 创建模块
     */
    public void createModule() {
        String[] depends = depends();
        if (ObjectUtils.isNotEmpty(depends)) { // 有依赖模块
            for (String dependModule : depends) {
                if (StringUtils.isEmpty(dependModule)) continue;

                if (!getModuleInstall(dependModule)) { // 未安装
                    context.invokeServiceMethod("base.module", "install",
                            new ArrayList<Integer>(),
                            RecordRow.build().put("name", dependModule));
                }
            }
        }
        if (ObjectUtils.isNull(getServiceList())) return;
        PrimaryKey key = upgradeModuleInfo();
        for (AbstractService service : getServiceList()) {
            if (service instanceof TransientService) { // 即时模型 只生成模型数据
                PrimaryKey serviceId = service.insertTableInfo(key);
                service.insertFieldInfo(serviceId);
                continue;
            }
            service.createTable();
            if (StringUtils.isEmpty(service.getInherit()) || !service.getServiceName().equals(service.getInherit())) { // 新模型
                PrimaryKey serviceId = service.insertTableInfo(key);
                service.insertFieldInfo(serviceId);
            } else { // 继承模型
                Integer serviceId1 = getServiceId(service.getInherit());
                service.insertFieldInfo(PrimaryKey.build(serviceId1));
            }
        }

        loadResource();
    }

    protected void uninstallResource() {
        IServiceDataService serviceDataService = getServiceDataService();

        if (ObjectUtils.isNull(serviceDataService)) return;

        com.avalon.core.model.Record moduleRecord = serviceDataService.getModuleRecord(getModuleName());
        if (moduleRecord.isEmpty()) return;

        for (RecordRow recordRow : moduleRecord) {
            if (recordRow.isNull("serviceId")) {
                continue;
            }
            AbstractService serviceBean = getContext().getServiceBean(recordRow.getRecordRow("serviceId").getString("name"));
            serviceBean.delete(recordRow.getInteger("sourceId")); // 直接删除记录
            serviceDataService.deleteServiceData(recordRow.getInteger("id"));
        }
    }

    // 加载资源文件，增加基本数据，无新增，有更新
    protected void loadResource() {
        try {
            ArrayList<InputStream> files = XmlDom4jUtils.loadModuleResourceFile(this);

            for (InputStream inputStream : files) {
                Element rootElement = XmlDom4jUtils.getRootElement(inputStream);

                NodeList childNodes = rootElement.getChildNodes(); // 获取子元素

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node item = childNodes.item(i);
                    if (item.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    String nodeName = item.getNodeName();
                    if (nodeName.equals("record")) { // 读取记录
                        Record record = createRecord(item);
                        NodeList fields = item.getChildNodes();
                        Integer sourceServiceId = getServiceId(record.getService());
                        for (int i1 = 0; i1 < fields.getLength(); i1++) {
                            if (item.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            Node item1 = fields.item(i1);
                            if (item1.getNodeName().equals("field")) {
                                RecordRow fieldRow = createField(item1, record);
                                record.setRow(fieldRow);
                            }
                        }
                        record.getRow().put("moduleId", getModuleId(getModuleName()));
                        upgradeRecord(sourceServiceId, record.getId(), record.getService(),
                                record.getRow());
                    } else if (nodeName.equals("update")) {
                        Record record = createRecord(item);
                        NodeList fields = item.getChildNodes();
                        Integer serviceId = null;
                        for (int i1 = 0; i1 < fields.getLength(); i1++) {
                            if (item.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            Node item1 = fields.item(i1);
                            if (item1.getNodeName().equals("field")) {
                                RecordRow fieldRow = createField(item1, record);
                                record.setRow(fieldRow);
                            }
                        }
                        Integer resourceId = getResourceId(getModuleName(), record.getId());
                        record.getRow().put("id", resourceId);
                        updateRecord(record.getService(), record.getRow());
                    } else if (nodeName.equals("menuitem")) { // 菜单 只识别三级
                        readMenuItem(item);
                    }
                }
            }
        } catch (AvalonException e) {
            e.printStackTrace();
            throw new AvalonException("创建模块时发出错误: " + e.getMessage(), e);
        }
    }

    private Integer getResourceId(String moduleName, String id) {
        IServiceDataService serviceBean = getServiceDataService();
        return serviceBean.refId(moduleName, id);
    }

    // 识别菜单
    protected void readMenuItem(Node item) {
        if (item.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }

        Node parentNode = item.getParentNode();
        String parentMenuId = "";
        if (parentNode.getNodeName().equals("menuitem")) { // 获取上级菜单
            parentMenuId = parentNode.getAttributes().getNamedItem("id").getNodeValue();
        }

        NamedNodeMap attributes = item.getAttributes();
        RecordRow row = RecordRow.build();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item1 = attributes.item(i);
            String nodeName = item1.getNodeName(); // 属性
            Object nodeValue = item1.getNodeValue(); // 值
            if (nodeName.equals("parentId")) { // 有上级
                nodeValue = getModelDataSourceId(this.getModuleName(), nodeValue.toString());
                if (ObjectUtils.isNull(nodeValue)) {
                    throw new AvalonException("模块[" + getModuleName() + "]的菜单的父级菜单[" + nodeValue + "]不存在");
                }
            } else if (nodeName.equals("action")) { // 替换actionId
                if (row.containsKey("type")) {
                    if (row.getString("type").equals("action")) {
                        nodeValue = getModelDataSourceId(this.getModuleName(), nodeValue.toString());
                    } else {
                        nodeName = "objectAction";
                    }
                }
            } else if (nodeName.equals("type")) {
                if (nodeValue.equals("action")) {
                    if (row.containsKey("action")) {
                        row.put("action", getModelDataSourceId(this.getModuleName(), row.getString("action")));
                    }
                } else {
                    if (row.containsKey("action")) {
                        row.put("objectAction", row.getString("action"));
                        row.remove("action");
                    }
                }
            } else if (nodeName.equals("serviceId")) {
                nodeValue = getServiceId(nodeValue.toString());
            }
            row.put(nodeName, nodeValue);
        }
        if (row.containsKey("action") && !row.containsKey("type")) {
            row.put("action", getModelDataSourceId(this.getModuleName(), row.getString("action")));
        }

        try {
            AbstractService moduleService = context.getServiceBean("base.module");
            com.avalon.core.model.Record select = moduleService
                    .select(Condition.equalCondition("name", getModuleName()), "id");
            if (!select.isEmpty()) {
                row.put("moduleId", select.get(0).getInteger("id"));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (StringUtils.isNotEmpty(parentMenuId)) { // 上级菜单
            row.put("parentId", refId(parentMenuId));
        }

        upgradeRecord(getServiceId("base.menu"), row.getString("id"), "base.menu", row);
        NodeList childNodes = item.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item1 = childNodes.item(i);
            if (item1.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String nodeName = item1.getNodeName();
            if (nodeName.equals("menuitem")) {
                readMenuItem(item1);
            }
        }
    }

    /**
     * 插入模块信息 到moduleService表中
     *
     * @return id
     */
    protected PrimaryKey insertModuleInfo() {
        RecordRow row = new RecordRow();
        row.put("name", getModuleName());
        row.put("label", getLabel());
        row.put("description", getDescription());
        row.put("display", getDisplay());
        row.put("icon", getIcon());
        row.put("isInstall", true);
        return context.getServiceBean("base.module").insert(row);
    }

    public Integer getModuleId(String moduleName) {
        AbstractService moduleService = context.getServiceBean("base.module");
        com.avalon.core.model.Record select = moduleService
                .select(Condition.equalCondition("name", moduleName), "id");
        if (select.isEmpty()) {
            return null;
        }
        return select.get(0).getInteger("id");
    }

    public boolean getModuleInstall(String moduleName) {
        AbstractService moduleService = context.getServiceBean("base.module");
        com.avalon.core.model.Record select = moduleService
                .select(Condition.equalCondition("name", getModuleName()), "id", "isInstall");
        if (select.isEmpty()) {
            return false;
        }
        return select.get(0).getBoolean("isInstall");
    }

    protected PrimaryKey upgradeModuleInfo() {
        AbstractService moduleService = context.getServiceBean("base.module");
        log.info("upgradeModuleInfo class-> {}", moduleService.getClass().getName());
        RecordRow row = new RecordRow();
        com.avalon.core.model.Record select = moduleService
                .select(Condition.equalCondition("name", getModuleName()), "id");
        row.put("name", getModuleName());
        row.put("label", getLabel());
        row.put("description", getDescription());
        row.put("display", getDisplay());
        row.put("icon", getIcon());
        row.put("isInstall", true);
        if (select.isEmpty()) {
            return moduleService.insert(row);
        } else {
            row.put("id", select.get(0).getInteger("id"));
            moduleService.update(row);
            return new PrimaryKey(select.get(0).getInteger("id"));
        }
    }

    public void dropModule() {
        if (ObjectUtils.isNull(getServiceList())) return;
        uninstallResource(); // 删除表 之前 删除资源记录
        for (AbstractService service : getServiceList()) {
            if (service instanceof TransientService) {
                continue;
            }
            if (StringUtils.isEmpty(service.getInherit()) || !service.getServiceName().equals(service.getInherit())) { // 新模型
                Integer serviceId = getServiceId(service.getServiceName());
                clearServiceField(serviceId);
                deleteBaseServiceData(serviceId);

                service.dropTable();
            } else { // 继承模型
                service.dropTable();
            }
        }
    }

    // 删除模型下的所有字段数据
    public void clearServiceField(Integer serviceId) {
        AbstractService serviceBean = context.getServiceBean("base.field");
        com.avalon.core.model.Record fields = serviceBean.select(Condition.equalCondition("serviceId", serviceId), "id");
        for (RecordRow field : fields) {
            serviceBean.delete(field.getInteger("id"));
        }
    }

    // 删除模型记录
    public void deleteBaseServiceData(Integer serviceId) {
        AbstractService serviceBean = context.getServiceBean("base.service");
        serviceBean.delete(serviceId);
    }

    public void upgradeModule() {
        String[] depends = depends();
        if (ObjectUtils.isNotEmpty(depends)) { // 有依赖模块
            for (String dependModule : depends) {
                if (StringUtils.isEmpty(dependModule)) continue;

                if (!getModuleInstall(dependModule)) { // 未安装
                    context.invokeServiceMethod("base.module", "install",
                            new ArrayList<Integer>(),
                            RecordRow.build().put("name", dependModule));
                } else {
                    Integer moduleId = getModuleId(dependModule);
                    if (ObjectUtils.isNull(moduleId)) {
                        throw new AvalonException("模块:" + dependModule + "不存在");
                    }

                    ArrayList<Integer> objects = new ArrayList<>();
                    objects.add(moduleId);
                    context.invokeServiceMethod("base.module", "upgrade",
                            objects,
                            RecordRow.build().put("name", dependModule));
                }
            }
        }
        PrimaryKey moduleId = upgradeModuleInfo();
        if (ObjectUtils.isNull(getServiceList())) return;
        AbstractService serviceBean = context.getServiceBean("base.service");
        for (AbstractService service : getServiceList()) {
            if (service instanceof TransientService) {
                continue;
            }
            service.upgradeTable();
            service.upgradeTableInfo(moduleId);
            if (ObjectUtils.isNotNull(serviceBean)) {
                FieldValue fieldValue = serviceBean.getFieldValue("id",
                        Condition.equalCondition("name", service.getServiceName()));
                PrimaryKey serviceId;
                if (fieldValue.isNull()) { // 新模型
                    serviceId = service.insertTableInfo(moduleId);
                } else {
                    serviceId = PrimaryKey.build(fieldValue);
                }
                service.insertFieldInfo(serviceId);
            }
        }

        loadResource();
    }


    protected Record createRecord(Node item) {
        Record record = new Record();
        NamedNodeMap attributes = item.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String nodeName = attribute.getNodeName();
            String nodeValue = attribute.getNodeValue();
            switch (nodeName) {
                case "id":
                    record.setId(nodeValue);
                    break;
                case "service":
                    record.setService(nodeValue);
            }
        }
        return record;
    }

    protected RecordRow createField(Node item, Record record) {
        RecordRow row = record.getRow();
        if (ObjectUtils.isNull(row)) {
            row = RecordRow.build();
            record.setRow(row);
        }
        NamedNodeMap attributes = item.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String nodeName = attribute.getNodeName();
            String nodeValue = attribute.getNodeValue();
            Object value = item.getTextContent();
            if ("ref_serviceId".equals(nodeValue)) { // 逻辑写死
                nodeValue = "serviceId";
                FieldValue fieldValue = getContext().getServiceBean("base.service")
                        .getFieldValue("id",
                                Condition.equalCondition("name", value));
                if (ObjectUtils.isNull(fieldValue)) {
                    throw new AvalonException(value + "服务不存在");
                }
                value = fieldValue.getInteger();
            } else if ("arch".equals(nodeValue)) {
                value = getInnerXml(item);
            }
            if ("refId".equals(nodeName)) {
                value = getResourceId(getModuleName(), value.toString());
            } else if ("ref".equals(nodeName)) {
                value = nodeValue;
                nodeValue = nodeName;
            }

            row.put(nodeValue, value);
        }

        if (row.containsKey("inheritId")) {
            if (row.containsKey("ref")) {
                row.put("inheritId", computeInheritId(row.getString("ref"), getModuleName()));
                row.remove("ref");
            }
        }

        return row;
    }

    /**
     * @param ref    继承的id
     * @param module 模块
     */
    private Integer computeInheritId(String ref, String module) {
        if (FieldUtils.hasJoinSelect(ref)) {
            module = FieldUtils.getJoinFirstTableString(ref);
            ref = FieldUtils.getJoinFirstFieldString(ref);
        }
        return refId(module, ref);
    }

    protected String getInnerXml(Node element) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.transform(new DOMSource(item), new StreamResult(writer));
            }

            return writer.getBuffer().toString().replaceAll("\n|\r|\t", "")
                    .replaceAll("\\s+", " ");
        } catch (Exception e) {
            throw new AvalonException(e.getMessage(), e);
        }

    }

    protected Integer getServiceId(String service) {
        AbstractService serviceBean = getContext().getServiceBean("base.service");
        com.avalon.core.model.Record select = serviceBean.select(Condition.equalCondition("name", service), "id");

        if (select.isEmpty()) {
            return null;
        } else {
            return select.get(0).getInteger("id");
        }
    }

    protected Integer getModelDataSourceId(String moduleName, String id) {
        IServiceDataService serviceBean = getServiceDataService();
        return serviceBean.refId(moduleName, id);
    }

    private IServiceDataService getServiceDataService() {
        try {
            return (IServiceDataService) getContext().getServiceBean("base.service.data");
        } catch (Exception e) {
            log.error("getServiceDataService", e);
            return null;
        }
    }

    protected void insertRecord(String moduleName, Integer dstServiceId,
                                String id, String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;

        serviceBean = getServiceDataService();

        if (ObjectUtils.isNull(serviceBean)) return;

        serviceBean.insert(moduleName, dstServiceId, id, serviceName, row);
    }

    protected Integer refId(String id) {
        return refId(getModuleName(), id);
    }

    protected Integer refId(String module, String id) {
        IServiceDataService serviceBean = null;

        serviceBean = getServiceDataService();
        return serviceBean.refId(module, id);
    }

    /**
     * 刷新数据，有两种情况，一种是直接更新数据，一种是更新页面，同时更新数据
     *
     * @param dstServiceId 模型id
     * @param id           xml id
     * @param serviceName  模型名称
     * @param row          数据
     */
    protected void upgradeRecord(Integer dstServiceId, String id, String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;

        serviceBean = getServiceDataService();
        Integer refId = serviceBean.refId(getModuleName(), id);
        if (ObjectUtils.isNull(refId)) {
            insertRecord(getModuleName(), dstServiceId, id, serviceName, row);
        } else {
            row.put(getContext().getServiceBean(serviceName).getPrimaryKeyName(), refId);
            updateRecord(serviceName, row);
        }
    }

    protected void updateRecord(String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;

        serviceBean = getServiceDataService();

        if (ObjectUtils.isNull(serviceBean)) return;

        serviceBean.update(serviceName, row);
    }

    public RecordRow getRecordRow() {
        RecordRow row = RecordRow.build();
        row.put("name", getModuleName());
        row.put("label", getLabel());
        row.put("description", getDescription());
        row.put("display", getDisplay());
        row.put("icon", getIcon());
        row.put("isInstall", false);

        return row;
    }
}
