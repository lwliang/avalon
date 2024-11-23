/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.RecordRowUtils;
import com.avalon.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OdooService extends AbstractService {
    private final XmlRpcClient client = new XmlRpcClient();
    @Value("${odoo.host:http://localhost:8069}")
    private String host;
    @Value("${odoo.db:electric}")
    private String db;
    @Value("${odoo.username:admin}")
    private String username;
    @Value("${odoo.password:123456}")
    private String password;
    private Optional<Integer> uid = Optional.empty();

    @Override
    public String getServiceName() {
        return "odoo.service";
    }

    public Field url = Fields.createString("接口");
    public Field model = Fields.createString("模型");
    public Field method = Fields.createString("方法");
    public Field params = Fields.createString("参数", 2000);


    /**
     * 获取Odoo版本
     *
     * @return 版本号 “17.0"
     */
    public String getVersion() {
        try {
            String url = String.format("%s/xmlrpc/2/common", host);
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(url));
            HashMap<String, Object> version = (HashMap) client.execute(config, "version", Collections.emptyList());
            return version.get("server_version").toString();
        } catch (MalformedURLException | XmlRpcException e) {
            log.error("获取Odoo版本失败", e);
            throw new AvalonException("获取Odoo版本失败", e);
        }
    }

    /**
     * 登录认证
     *
     * @return 用户ID
     */
    public int authenticate() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", host)));
            return (int) client.execute(config, "authenticate",
                    Arrays.asList(db, username, password, Collections.emptyMap()));
        } catch (MalformedURLException | XmlRpcException e) {
            log.error("odoo登录认证失败", e);
            throw new AvalonException("odoo登录认证失败", e);
        }
    }

    public int getUid() {
        if (uid.isEmpty()) {
            uid = Optional.of(authenticate());
        }
        return uid.get();
    }

    /**
     * 查询
     *
     * @param model  模型
     * @param domain 条件 例如：Arrays.asList(Arrays.asList(Arrays.asList("name", "=", "test")))  三层列表
     * @param offset 偏移
     * @param limit  限制个数
     * @return 查询结果 只返回主键列表
     */
    public List<Integer> search(String model, List<Object> domain, Integer offset, Integer limit) {
        try {
            HashMap<String, Integer> pageMap = new HashMap<>();
            if (ObjectUtils.isNotNull(offset)) {
                pageMap.put("offset", offset);
            }
            if (ObjectUtils.isNotNull(limit)) {
                pageMap.put("limit", limit);
            }
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "search",
                    domain,
                    pageMap);
            Object[] executeKw = (Object[]) execute_kw(param);
            return Arrays.stream(executeKw).map(obj -> (int) obj).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = "search" + model + "失败";
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    public Object execute_kw(List param) throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(String.format("%s/xmlrpc/2/object", host)));
        return client.execute(config, "execute_kw", param);
    }

    /**
     * 查询主键集合
     *
     * @param model  模型
     * @param domain 条件
     * @return 查询结果 只返回主键列表
     */
    public List<Integer> search(String model, List<Object> domain) {
        return search(model, domain, null, null);
    }

    /**
     * 查询记录个数
     *
     * @param model  模型
     * @param domain 条件
     * @return 个数
     */
    public Integer searchCount(String model, List<Object> domain) {
        try {
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "search_count",
                    domain);
            return (Integer) execute_kw(param);
        } catch (Exception e) {
            String msg = "searchCount" + model + "个数失败";
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    public List<HashMap> read(String model, List<Integer> ids, List<String> fields) {
        try {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (ObjectUtils.isNotEmpty(fields)) {
                hashMap.put("fields", fields);
            }
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "read",
                    Arrays.asList(ids),
                    hashMap);
            Object[] executeKw = (Object[]) execute_kw(param);
            return Arrays.stream(executeKw).map(obj -> (HashMap) obj).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = "read" + model + "失败,查询字段:" + StringUtils.join(fields);
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 读取模型的字段列表
     *
     * @param model      模型
     * @param attributes 返回的属性列表 string, name,type 例如：Arrays.asList("string", "name", "type")
     */
    public HashMap<String, HashMap<String, Object>> getModelFields(String model, List<String> attributes) {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (ObjectUtils.isNotEmpty(attributes)) {
                hashMap.put("attributes", attributes);
            }
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "fields_get",
                    Collections.emptyList(),
                    hashMap);
            HashMap<String, HashMap<String, Object>> executeKw = (HashMap<String, HashMap<String, Object>>) execute_kw(param);
            return executeKw;
        } catch (Exception e) {
            String msg = "getModelFields" + model + "失败,查询属性字段:" + StringUtils.join(attributes);
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 获取全部
     *
     * @param model  模型
     * @param fields 字段
     * @return 记录
     */
    public List<HashMap<String, Object>> searchReadAll(String model, List<String> fields) {
        return searchRead(model, List.of(List.of()), fields, null, null);
    }

    public List<HashMap<String, Object>> searchRead(String model,
                                                    List<Object> domain,
                                                    List<String> fields,
                                                    Integer limit,
                                                    String order) {
        try {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (ObjectUtils.isNotEmpty(fields)) {
                hashMap.put("fields", fields);
            }
            if (ObjectUtils.isNotNull(limit)) {
                hashMap.put("limit", limit);
            }
            if (ObjectUtils.isNotNull(order)) {
                hashMap.put("order", order);
            }
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "search_read",
                    domain,
                    hashMap);
            Object[] executeKw = (Object[]) execute_kw(param);
            return Arrays.stream(executeKw).map(obj -> (HashMap<String, Object>) obj).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = "read" + model + "失败,查询字段:" + StringUtils.join(fields);
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 创建
     *
     * @param model  模型
     * @param values 创建的数据
     * @return 主键
     */
    public Integer create(String model, Map<String, Object> values) {
        try {
            RecordRow row = RecordRow.build();
            row.put("model", model);
            row.put("url", "/xmlrpc/2/object");
            row.put(method, "create");
            row.put(params, JacksonUtil.object2String(values));
            insert(row);
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "create",
                    Arrays.asList(values));
            return (Integer) execute_kw(param);
        } catch (Exception e) {
            String msg = "create" + model + "失败,创建数据:" + values;
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 更新
     *
     * @param model  模型
     * @param id     主键
     * @param values 更新的数据
     */
    public void write(String model, Integer id, Map<String, Object> values) {
        try {
            RecordRow row = RecordRow.build();
            row.put("model", model);
            row.put("url", "/xmlrpc/2/object");
            row.put(method, "write");
            values.put("id", id);
            row.put(params, JacksonUtil.object2String(values));
            values.remove("id");
            insert(row);
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "write",
                    Arrays.asList(Arrays.asList(id),
                            values)
            );
            execute_kw(param);
        } catch (Exception e) {
            String msg = "write" + model + "失败,更新数据:" + values;
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }

    /**
     * 删除
     *
     * @param model 模型
     * @param id    主键
     */
    public void unlink(String model, Integer id) {
        try {
            RecordRow row = RecordRow.build();
            row.put("model", model);
            row.put("url", "/xmlrpc/2/object");
            row.put(method, "unlink");
            row.put(params, id);
            insert(row);
            List<Object> param = Arrays.asList(
                    db,
                    getUid(),
                    password,
                    model,
                    "unlink",
                    Arrays.asList(Arrays.asList(id))
            );
            execute_kw(param);
        } catch (Exception e) {
            String msg = "unlink" + model + "失败,删除数据:" + id;
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }


    public Object execute_kw_method(String model, String method, Integer id, Object... params) {
        try {
            List<Object> param = new ArrayList<>();
            param.add(db);
            param.add(getUid());
            param.add(password);
            param.add(model);
            param.add(method);
            param.add(List.of(Arrays.asList(id), params));
            return execute_kw(param);
        } catch (Exception e) {
            String msg = "method:" + method + model + "执行失败,参数:" + Arrays.toString(params);
            log.error(msg, e);
            throw new AvalonException(msg, e);
        }
    }
}
