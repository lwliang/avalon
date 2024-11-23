/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class OdooUserService {
    private final String model = "pet.user";
    private OdooService odooService;

    public OdooUserService(OdooService odooService) {
        this.odooService = odooService;
    }


    public Integer createOdooUser(String openId) {
        List<Integer> idList = odooService.search(model, List.of(List.of(Arrays.asList("openid", "=", openId))));
        if (idList.size() > 0) {
            return idList.get(0);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("openid", openId);
        return odooService.create(model, data);
    }
}
