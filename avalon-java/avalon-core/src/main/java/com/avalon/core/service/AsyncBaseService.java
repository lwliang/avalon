/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class AsyncBaseService {

    @Autowired
    private Context context;

    public Context getContext() {
        return context;
    }

    protected void recoverContext(Map<String, Object> local) {
        context.recoverThreadMap(local);
    }
}
