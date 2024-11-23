/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.log;

public interface IAvalonServiceLog {
    void insert(ServiceLog log);

    void update(ServiceLog log);

    void delete(ServiceLog log);

    Boolean enableLog();
}
