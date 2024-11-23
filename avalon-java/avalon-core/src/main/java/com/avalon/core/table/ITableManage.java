/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.table;

public interface ITableManage extends ITableService {

    TableServiceList getTableServiceList();//获取所有子服务

    void addTableService(ITableService tableService);//添加子服务

    ITableService getFirstTableService();//获取第一个子服务
}
