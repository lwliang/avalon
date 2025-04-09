package com.avalon.core.service;

import com.avalon.core.permission.PermissionEnum;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/30 14:51
 */
public interface ICheckPermission {
    boolean needCheckRecordRule();
    boolean needCheckPermission();

    boolean checkPermission(Integer userId, String service, PermissionEnum permissionEnum);
}
