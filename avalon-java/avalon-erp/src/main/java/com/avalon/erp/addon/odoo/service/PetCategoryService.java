/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class PetCategoryService {
    private final String model = "pet.category";
    private OdooService odooService;

    public PetCategoryService(OdooService odooService) {
        this.odooService = odooService;
    }

    public List<HashMap<String, Object>> getPetCategory() {
        List<Object> domain = List.of("parent_id", "=", false);
        return odooService.searchRead(model, List.of(List.of(domain)), List.of("name", "sequence", "parent_id"),
                null, null);
    }

    /**
     * base64位图片
     *
     * @return base64位图片
     */
    public Object getImageUrl(Integer id) {
        return odooService.execute_kw_method(model, "get_image_url", id, "avatar");
    }
}
