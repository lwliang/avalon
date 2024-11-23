/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

public interface IInheritTable {
    /**
     * 是否继承
     *
     * @return 是否继承
     */
    Boolean isInherit();

    /**
     * 获取继承表名
     *
     * @return 继承表名
     */
    String getInheritTable();
}
