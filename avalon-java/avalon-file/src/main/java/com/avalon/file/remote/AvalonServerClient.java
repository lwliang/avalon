/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvalonServerClient {
    @Autowired
    private IAvalonServerClient avalonServerClient;

    public String test(){
        return avalonServerClient.test();
    }
}
