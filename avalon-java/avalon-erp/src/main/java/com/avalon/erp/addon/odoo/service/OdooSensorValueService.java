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
public class OdooSensorValueService {
    private final String model = "electric.sensor.value";
    private final OdooService odooService;

    public OdooSensorValueService(OdooService odooService) {
        this.odooService = odooService;
    }

    /**
     * 更新值
     *
     * @param sensor_id
     * @param value
     * @param State
     * @param highAlarmValue
     * @param lowAlarmValue
     * @param show_text
     */
    public void createSensorValue(Integer sensor_id,
                                  Float value,
                                  Integer State,
                                  Float highAlarmValue,
                                  Float lowAlarmValue,
                                  String show_text) {
        Map<String, Object> param = new HashMap<>();
        param.put("sensor_id", sensor_id);
        param.put("value", value.toString());
        param.put("state", State.toString());
        param.put("highAlarmValue", highAlarmValue.toString());
        param.put("lowAlarmValue", lowAlarmValue.toString());
        param.put("show_text", show_text);
        param.put("simple_time", DateTimeUtils.getCurrentDateTimeString());
        odooService.create(model, param);
    }
}
