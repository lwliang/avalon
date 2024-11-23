/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.context.Context;
import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class DBService {
    private final Context context;

    public DBService(Context context) {
        this.context = context;
    }

    public void createDataBase() {
        AbstractModule base = context.getModuleMap().getModule("base");
        base.createModule();
    }
}
