/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OdooRelayService {
    private final String model = "electric.relay";

    private final OdooService odooService;

    public OdooRelayService(OdooService odooService) {
        this.odooService = odooService;
    }

    public Integer createRelay(String name, Integer power_room_id, Integer port, String type) {
        return odooService.create(model, Map.of("name", name,
                "power_room_id", power_room_id, "port", port, "type", type));
    }

    public void updateRelay(Integer id, String name, Integer power_room_id, Integer port, String type) {
        odooService.write(model, id, Map.of("name", name, "power_room_id", power_room_id,
                "port", port, "type", type));
    }
}
