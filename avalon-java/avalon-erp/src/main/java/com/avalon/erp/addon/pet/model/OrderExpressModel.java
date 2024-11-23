/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model;

import lombok.Data;

@Data
public class OrderExpressModel {
    private Integer id;
    private String expressNo;
    private String expressCompany;
    private String sendAddressDetail;
    private String senderName;
    private String senderPhone;
}
