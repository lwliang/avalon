/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model;

import com.avalon.erp.sys.addon.base.model.ServiceModelPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdoptServiceModelPage extends ServiceModelPage {
    private Integer userId;
}
