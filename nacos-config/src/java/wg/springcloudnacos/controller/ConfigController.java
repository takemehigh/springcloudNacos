package wg.springcloudnacos.controller;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("config")
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @Value("${nacos.config.server-addr}")
    private String serverAddr;

    @Bean
    private ConfigService getConfigService() throws NacosException {
        if(configService==null){
            return ConfigFactory.createConfigService(serverAddr);
        }
        else{
            return configService;
        }
    }

    @Autowired
    private ConfigService configService;
    /*@Value(value = "${useLocalCache:false}")
    private boolean useLocalCache1;*/

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @RequestMapping(value = "/get2", method = GET)
    @ResponseBody
    public String get2() throws NacosException {
        return configService.getConfig("example","DEFAULT_GROUP",5000);
    }
}