/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.model.RecordRow;

public interface IServiceDataService {
    void insert(String moduleName, Integer dstServiceId,
                String id, String serviceName, RecordRow recordRow);

    void update(String serviceName, RecordRow recordRow);

    Integer refId(String moduleName, String id);

    Integer refId(String id);
}