/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import com.avalon.core.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OdooRelayValueService {
    private final String model = "electric.relay.value";
    private final OdooService odooService;

    public OdooRelayValueService(OdooService odooService) {
        this.odooService = odooService;
    }

    public void createRelayValue(Integer relay_id, Integer value) {
        Map<String, Object> param = new HashMap<>();
        param.put("relay_id", relay_id);
        param.put("value", value.toString());
        param.put("simple_time", DateTimeUtils.getCurrentDateTimeString());
        odooService.create(model, param);
    }
}
