package com.avalon.erp.addon.odoo.service;

import lombok.extern.slf4j.Slf4j;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.TransientService;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OdooModelService extends TransientService {
    @Override
    public String getServiceName() {
        return "odoo.model";
    }
    @Override
    public Field getNameField() {
        return name;
    }

    public Field convertId = Fields.createMany2one("转换ID", "odoo.convert");
    public Field module = Fields.createString("模块");
    public Field originalName = Fields.createString("原始名称");
    public Field name = Fields.createString("模型名称");
    public Field label = Fields.createString("模型标签");
}
