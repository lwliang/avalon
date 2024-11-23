/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.lock;

import java.util.Map;

public interface ILockSupport {
    Object getLock(Map mapTable, Object key);
}
