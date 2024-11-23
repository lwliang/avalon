/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
@Slf4j
public class OdooPowerRoom {
    private final String model = "electric.power.room";
    private final OdooService odooService;

    public OdooPowerRoom(OdooService odooService) {
        this.odooService = odooService;
    }

    public Integer createPowerRoom(String name, Integer community_id) {
        Map<String, Object> param = Map.of("name", name, "community_id", community_id);
        return odooService.create(model, param);
    }

    public void updatePowerRoom(Integer id, String name, Integer community_id) {
        Map<String, Object> param = Map.of("name", name, "community_id", community_id);
        odooService.write(model, id, param);
    }
}
