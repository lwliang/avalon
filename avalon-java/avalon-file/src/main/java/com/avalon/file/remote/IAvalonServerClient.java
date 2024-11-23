/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.remote;

import com.avalon.core.remote.AvalonRemoteClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "avalon-server", fallback = AvalonServerClient.class)
public interface IAvalonServerClient extends AvalonRemoteClient {

    @GetMapping("/test")
    String test();
}
