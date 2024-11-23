/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService {
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getServiceName();
}
