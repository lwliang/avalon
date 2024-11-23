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
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.core.util.XmlDom4jUtils;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

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

    private Boolean isInstall = false;

    public Boolean getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(Boolean isInstall) {
        this.isInstall = isInstall;
    }

    /**
     * 模块图标
     *
     * @return url 本地文件
     */
    public String getIcon() {
        return null;
    }

    public AbstractServiceList getServiceList() {
        return context.getModuleMap().getModuleServiceList(getModuleName());
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
        if (ObjectUtils.isNull(getServiceList())) return;
        PrimaryKey key = upgradeModuleInfo();
        for (AbstractService service : getServiceList()) {
            service.createTable();
            PrimaryKey serviceId = service.insertTableInfo(key);
            service.insertFieldInfo(serviceId);
        }

        loadResource();
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
                        Integer serviceId = null;
                        for (int i1 = 0; i1 < fields.getLength(); i1++) {
                            if (item.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            Node item1 = fields.item(i1);
                            if (item1.getNodeName().equals("field")) {
                                RecordRow fieldRow = createField(item1, record);
                                record.setRow(fieldRow);
                                if (fieldRow.containsKey("serviceId")) {
                                    serviceId = fieldRow.getInteger("serviceId");
                                }
                            }
                        }
                        upgradeRecord(serviceId, record.getId(), record.getService(),
                                record.getRow());
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
        upgradeRecord(0, row.getString("id"), "base.menu", row);
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

    protected void deleteModuleInfo() {
        AbstractService serviceBean = context.getServiceBean("base.module");
        if (ObjectUtils.isNotNull(serviceBean)) {
            serviceBean.delete(Condition.equalCondition("name", getModuleName()));
        }
    }


    public void dropModule() {
        if (ObjectUtils.isNull(getServiceList())) return;

        for (AbstractService service : getServiceList()) {
            service.dropTable();
        }
    }

    public void upgradeModule() {
        PrimaryKey moduleId = upgradeModuleInfo();
        if (ObjectUtils.isNull(getServiceList())) return;
        AbstractService serviceBean = context.getServiceBean("base.service");
        for (AbstractService service : getServiceList()) {
            service.upgradeTable();
            service.upgradeTableInfo(moduleId);
            if (ObjectUtils.isNotNull(serviceBean)) {
                FieldValue fieldValue = serviceBean.getFieldValue("id",
                        Condition.equalCondition("name", service.getServiceName()));
                PrimaryKey serviceId = PrimaryKey.build(fieldValue);
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

            row.put(nodeValue, value);
        }

        return row;
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
        IServiceDataService serviceBean = getContext().getServiceBean(IServiceDataService.class);
        return serviceBean.refId(moduleName, id);
    }

    protected void insertRecord(String moduleName, Integer dstServiceId,
                                String id, String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;
        try {
            serviceBean = getContext().getServiceBean(IServiceDataService.class);
        } finally {

        }
        if (ObjectUtils.isNull(serviceBean)) return;

        serviceBean.insert(moduleName, dstServiceId, id, serviceName, row);
    }

    protected Integer refId(String id) {
        IServiceDataService serviceBean = null;
        try {
            serviceBean = getContext().getServiceBean(IServiceDataService.class);
            return serviceBean.refId(getModuleName(), id);
        } finally {

        }
    }


    protected void upgradeRecord(Integer dstServiceId, String id, String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;
        try {
            serviceBean = getContext().getServiceBean(IServiceDataService.class);
            Integer refId = serviceBean.refId(getModuleName(), id);
            if (ObjectUtils.isNull(refId)) {
                insertRecord(getModuleName(), dstServiceId, id, serviceName, row);
            } else {
                row.put(getContext().getServiceBean(serviceName).getPrimaryKeyName(), refId);
                updateRecord(serviceName, row);
            }
        } finally {

        }
    }

    protected void updateRecord(String serviceName, RecordRow row) {
        IServiceDataService serviceBean = null;
        try {
            serviceBean = getContext().getServiceBean(IServiceDataService.class);
        } finally {

        }
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
