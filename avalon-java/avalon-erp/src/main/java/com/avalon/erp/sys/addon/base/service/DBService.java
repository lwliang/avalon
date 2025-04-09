/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.context.Context;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class DBService {
    private final Context context;

    public DBService(Context context) {
        this.context = context;
    }

    @TemporaryElevate(value = {ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
    public void createDataBase() {
        AbstractModule base = context.getModule("base");
        base.createModule();
        context.installOrUpgrade(List.of(base));
    }
}
