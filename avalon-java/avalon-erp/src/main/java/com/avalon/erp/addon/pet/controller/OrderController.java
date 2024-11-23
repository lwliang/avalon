/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.controller;

import com.avalon.erp.addon.pet.model.OrderExpressModel;
import com.avalon.erp.addon.pet.service.shop.order.PetOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private PetOrderService petOrderService;

    public OrderController(PetOrderService petOrderService) {
        this.petOrderService = petOrderService;
    }

    @PostMapping("/sender/express")
    public void senderExpress(@RequestBody OrderExpressModel orderExpressModel) {
        petOrderService.senderExpress(
                orderExpressModel.getId(),
                orderExpressModel.getExpressNo(),
                orderExpressModel.getExpressCompany(),
                orderExpressModel.getSenderName(),
                orderExpressModel.getSenderPhone(),
                orderExpressModel.getSendAddressDetail());
    }

    @PostMapping("/receive/sure")
    public void orderReceive(@RequestBody Map<String, Object> param) {
        Integer id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        petOrderService.orderReceive(id);
    }

    @PostMapping("/comment/complete")
    public void orderComment(@RequestBody Map<String, Object> param) {
        Integer id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        petOrderService.orderComment(id);
    }

    @PostMapping("custom/cancel")
    public void orderCancel(@RequestBody Map<String, Object> param) {
        Integer id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        String backExpressNo = "";
        if (param.containsKey("backExpressNo")) {
            backExpressNo = param.get("backExpressNo").toString();
        }
        String backExpressCompany = "";
        if (param.containsKey("backExpressCompany")) {
            backExpressCompany = param.get("backExpressCompany").toString();
        }
        String backReason = "";
        if (param.containsKey("backReason")) {
            backReason = param.get("backReason").toString();
        }

        petOrderService.orderBack(id,
                backExpressNo,
                backExpressCompany,
                backReason);
    }

    @PostMapping("/cancel/sure")
    public void orderCancelSure(@RequestBody Map<String, Object> param) {
        Integer id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        petOrderService.orderBackSure(id);
    }
}
