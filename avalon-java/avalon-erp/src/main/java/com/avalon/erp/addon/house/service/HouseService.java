package com.avalon.erp.addon.house.service;

import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/08 11:29
 */
@Service
@Slf4j
public class HouseService extends AbstractService {
    @Override
    public String getServiceName() { // 模型名，等价于表名，第一个house表示模块名
        return "house.house";
    }

    @Override
    public String getLabel() {
        return "房屋";// 标题
    }
}
