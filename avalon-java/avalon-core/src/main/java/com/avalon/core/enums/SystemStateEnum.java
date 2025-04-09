package com.avalon.core.enums;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/07 19:30
 */
public enum SystemStateEnum {
    createDB, // 安装数据库
    dropDB, // 删除数据库
    installModule,// 安装模块
    uninstallModule,// 卸载模块
    upgradeModule, // 升级模块
    none,// 无状态
}
