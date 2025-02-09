package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.erp.sys.addon.base.model.ServiceModelField;
import com.avalon.erp.sys.addon.base.service.ActionViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/10 16:59
 */

@RestController
@Slf4j
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private Context context;

    /**
     * 获取全部数据
     *
     * @param serviceConditionPage
     * @return
     */
    @PostMapping("get/base.action.view/all")
    public Record getBaseActionView(@RequestBody ServiceModelField serviceConditionPage) {
        ActionViewService serviceBean = (ActionViewService) context.getServiceBean("base.action.view");
        return serviceBean.getActionView(serviceConditionPage.getFields(), serviceConditionPage.getOrder(),
                Condition.parseRPN(serviceConditionPage.getRpnCondition()));
    }
}
