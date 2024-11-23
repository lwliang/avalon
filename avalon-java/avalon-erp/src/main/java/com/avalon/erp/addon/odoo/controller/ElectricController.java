/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.controller;

import com.avalon.core.model.RecordRow;
import com.avalon.erp.addon.odoo.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/electric")
public class ElectricController {
    private final OdooCommunityService odooCommunityService;
    private final OdooPowerRoom odooPowerRoom;
    private final OdooSensorService odooSensorService;
    private final OdooRelayService odooRelayService;
    private final OdooSensorValueService odooSensorValueService;
    private final OdooRelayValueService odooRelayValueService;

    public ElectricController(OdooCommunityService odooCommunityService,
                              OdooPowerRoom odooPowerRoom,
                              OdooSensorService odooSensorService,
                              OdooRelayService odooRelayService, OdooSensorValueService odooSensorValueService, OdooRelayValueService odooRelayValueService) {
        this.odooCommunityService = odooCommunityService;
        this.odooPowerRoom = odooPowerRoom;
        this.odooSensorService = odooSensorService;
        this.odooRelayService = odooRelayService;
        this.odooSensorValueService = odooSensorValueService;
        this.odooRelayValueService = odooRelayValueService;
    }

    @PostMapping("/create/community")
    public Integer createCommunity(@RequestBody RecordRow param) {
        return odooCommunityService.createCommunity(
                param.getString("name"),
                param.getString("local_uuid"),
                param.getString("province"),
                param.getString("city"),
                param.getString("area"),
                param.getString("detail"));
    }

    @PostMapping("/update/community")
    public void updateCommunity(@RequestBody RecordRow param) {
        odooCommunityService.updateCommunity(param.getInteger("id"),
                param.getString("name"),
                param.getString("local_uuid"),
                param.getString("province"),
                param.getString("city"),
                param.getString("area"),
                param.getString("detail"));
    }

    @PostMapping("/create/power/room")
    public Integer createPowerRoom(@RequestBody RecordRow param) {
        return odooPowerRoom.createPowerRoom(param.getString("name"),
                param.getInteger("community_id"));
    }

    @PostMapping("/update/power/room")
    public void updatePowerRoom(@RequestBody RecordRow param) {
        odooPowerRoom.updatePowerRoom(param.getInteger("id"),
                param.getString("name"),
                param.getInteger("community_id"));
    }

    @PostMapping("/create/sensor")
    public Integer createSensor(@RequestBody RecordRow param) {
        return odooSensorService.createSensor(param.getString("title"),
                param.getInteger("power_room_id"),
                param.getString("sensorType"),
                param.getFloat("highAlarmValue"),
                param.getFloat("lowAlarmValue"),
                param.getBoolean("enable"),
                param.getBoolean("isFake"),
                param.getString("successText"),
                param.getString("unit"),
                param.getString("highErrorText"),
                param.getString("lowErrorText"),
                param.getInteger("local_id"));
    }

    @PostMapping("/update/sensor")
    public void updateSensor(@RequestBody RecordRow param) {
        odooSensorService.updateSensor(param.getInteger("id"),
                param.getString("title"),
                param.getInteger("power_room_id"),
                param.getString("sensorType"),
                param.getFloat("highAlarmValue"),
                param.getFloat("lowAlarmValue"),
                param.getBoolean("enable"),
                param.getBoolean("isFake"),
                param.getString("successText"),
                param.getString("unit"),
                param.getString("highErrorText"),
                param.getString("lowErrorText"),
                param.getInteger("local_id"));

    }

    @PostMapping("/create/relay")
    public Integer createRelay(@RequestBody RecordRow param) {
        return odooRelayService.createRelay(
                param.getString("name"),
                param.getInteger("power_room_id"),
                param.getInteger("port"),
                param.getString("type")
        );
    }

    @PostMapping("/update/relay")
    public void updateRelay(@RequestBody RecordRow param) {
        odooRelayService.updateRelay(
                param.getInteger("id"),
                param.getString("name"),
                param.getInteger("power_room_id"),
                param.getInteger("port"),
                param.getString("type")
        );
    }

    @PostMapping("/create/sensor/value")
    public void createSensorValue(@RequestBody RecordRow param) {
        odooSensorValueService.createSensorValue(
                param.getInteger("sensor_id"),
                param.getFloat("value"),
                param.getInteger("state"),
                param.getFloat("highAlarmValue"),
                param.getFloat("lowAlarmValue"),
                param.getString("showText")
        );
    }

    @PostMapping("/create/relay/value")
    public void createRelayValue(@RequestBody RecordRow param) {
        odooRelayValueService.createRelayValue(
                param.getInteger("relay_id"),
                param.getInteger("state")
        );
    }
}
