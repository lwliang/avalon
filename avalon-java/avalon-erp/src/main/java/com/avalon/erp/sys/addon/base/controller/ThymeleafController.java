/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.context.Context;
import com.avalon.core.service.AbstractServiceList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ThymeleafController {
    @Autowired
    private Context context;

    @GetMapping("/")
    public ModelAndView getHome() {
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/index")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();

        AbstractServiceList serviceList = context.getServiceList();

        modelAndView.addObject("serviceList", serviceList);

        modelAndView.setViewName("index");
        return modelAndView;
    }
}
