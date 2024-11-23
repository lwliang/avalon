/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.hr.service;

import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceShiftService extends AbstractService {
    @Override
    public String getServiceName() {
        return "hr.attendance.shift";
    }
}
