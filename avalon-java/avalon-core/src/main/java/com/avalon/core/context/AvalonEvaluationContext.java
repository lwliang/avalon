/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.util.DateTimeUtils;
import lombok.Data;

import java.util.Date;

@Data
public class AvalonEvaluationContext {
    private Integer userId;
    private Integer orgId;

    public Date getCurrentDate()
    {
        return DateTimeUtils.getCurrentDate();
    }
}
