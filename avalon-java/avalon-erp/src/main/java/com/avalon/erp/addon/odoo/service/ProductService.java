/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.odoo.service;

import com.avalon.core.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final String productCategoryModel = "pet.commodity.category";
    private OdooService odooService;

    public ProductService(OdooService odooService) {
        this.odooService = odooService;
    }

    public List<HashMap<String, Object>> getProductCategory() {

        return odooService.searchReadAll(productCategoryModel, List.of("name", "sequence"));
    }

    /**
     * base64位图片
     * @return base64位图片
     */
    public Object getImageUrl(Integer id) {
        return odooService.execute_kw_method(productCategoryModel, "get_image_url", id, "image_128");
    }
}
