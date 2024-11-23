/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OdooSensorService {
    private final String model = "electric.sensor";
    private final OdooService odooService;

    public OdooSensorService(OdooService odooService) {
        this.odooService = odooService;
    }

    public Integer createSensor(String title,
                                Integer power_room_id,
                                String sensorType,
                                Float highAlarmValue,
                                Float lowAlarmValue,
                                Boolean enable,
                                Boolean isFake,
                                String successText,
                                String unit,
                                String highErrorText,
                                String lowErrorText,
                                Integer local_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("power_room_id", power_room_id);
        map.put("sensorType", sensorType);
        map.put("highAlarmValue", highAlarmValue.toString());
        map.put("lowAlarmValue", lowAlarmValue.toString());
        map.put("enable", enable);
        map.put("isFake", isFake);
        map.put("successText", successText);
        map.put("unit", unit);
        map.put("highErrorText", highErrorText);
        map.put("lowErrorText", lowErrorText);
        map.put("local_id", local_id);
        return odooService.create(model, map);
    }

    public void updateSensor(Integer id,
                             String title,
                             Integer power_room_id,
                             String sensorType,
                             Float highAlarmValue,
                             Float lowAlarmValue,
                             Boolean enable,
                             Boolean isFake,
                             String successText,
                             String unit,
                             String highErrorText,
                             String lowErrorText,
                             Integer local_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("power_room_id", power_room_id);
        map.put("sensorType", sensorType);
        map.put("highAlarmValue", highAlarmValue.toString());
        map.put("lowAlarmValue", lowAlarmValue.toString());
        map.put("enable", enable);
        map.put("isFake", isFake);
        map.put("successText", successText);
        map.put("unit", unit);
        map.put("highErrorText", highErrorText);
        map.put("lowErrorText", lowErrorText);
        map.put("local_id", local_id);
        odooService.write(model, id, map);
    }
}
