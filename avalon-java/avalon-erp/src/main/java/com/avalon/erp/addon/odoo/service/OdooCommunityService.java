/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import com.avalon.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OdooCommunityService {
    private final String model = "electric.community";
    private final OdooService odooService;

    public OdooCommunityService(OdooService odooService) {
        this.odooService = odooService;
    }

    /**
     * 创建小区
     *
     * @param name       小区名字
     * @param local_uuid 社区唯一码
     * @return
     */
    public Integer createCommunity(String name,
                                   String local_uuid,
                                   String province,
                                   String city,
                                   String area,
                                   String detail) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("local_uuid", local_uuid);
        if (StringUtils.isNotEmpty(province)) {
            param.put("province", province);
        }
        if (StringUtils.isNotEmpty(city)) {
            param.put("city", city);
        }
        if (StringUtils.isNotEmpty(area)) {
            param.put("area", area);
        }
        if (StringUtils.isNotEmpty(detail)) {
            param.put("detail", detail);
        }
        return odooService.create(model, param);
    }

    public void updateCommunity(Integer id,
                                String name,
                                String local_uuid,
                                String province,
                                String city,
                                String area,
                                String detail) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("local_uuid", local_uuid);
        if (StringUtils.isNotEmpty(province)) {
            param.put("province", province);
        }
        if (StringUtils.isNotEmpty(city)) {
            param.put("city", city);
        }
        if (StringUtils.isNotEmpty(area)) {
            param.put("area", area);
        }
        if (StringUtils.isNotEmpty(detail)) {
            param.put("detail", detail);
        }
        odooService.write(model, id, param);
    }
}
