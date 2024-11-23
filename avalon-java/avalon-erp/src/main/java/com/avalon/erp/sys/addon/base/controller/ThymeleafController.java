/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.context.Context;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.module.ModuleHashMap;
import com.avalon.core.module.ModuleList;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        ModuleHashMap moduleMap = context.getModuleMap();

        modelAndView.addObject("serviceList",
                moduleMap.getAllService().stream().map(AbstractService::getServiceName).collect(Collectors.toList()));

        modelAndView.setViewName("index");
        return modelAndView;
    }
}
