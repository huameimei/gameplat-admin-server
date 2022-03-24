
package com.gameplat.admin.feign;


import com.gameplat.common.game.config.FeignRestConfig;
import com.gameplat.common.game.config.FeignJsonConfig.JsonDecoder;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author james
 */
@FeignClient(
    name = "SportFeignClient",
    url = "${game.proxy.host}",
        path = "/im/api",
    configuration = {FeignRestConfig.class, JsonDecoder.class}
)
public interface SportFeignClient {
    @PostMapping({"/api/sports/config/updateAppConfig"})
    String updateAppConfig(@RequestHeader Map<String, String> var1, @SpringQueryMap Map<String, String> var2);

}
