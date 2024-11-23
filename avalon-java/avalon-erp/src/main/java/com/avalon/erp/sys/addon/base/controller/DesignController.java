/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.FieldList;
import com.avalon.core.model.FieldHashMap;
import com.avalon.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/design")
@RestController
public class DesignController {
    @Autowired
    private Context context;

    @PostMapping("get/field")
    public FieldList getModelFieldList(@RequestBody Map<String, Object> param) throws AvalonException {

        String serviceName = param.get("service").toString();

        FieldList fieldList = new FieldList();

        AbstractService serviceBean = context.getServiceBean(serviceName);
        FieldHashMap fieldMap = serviceBean.getFieldMap();

        fieldMap.forEach((fieldName, field) -> fieldList.add(field));

        return fieldList;
    }
}
