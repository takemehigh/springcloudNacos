package wg.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(EsConfigProperties.class)
public class EsConfig{

    @Autowired
    EsConfigProperties esConfigProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(HttpHost.create(esConfigProperties.getAddress().get(0)))
        );
        // 这里可以配置多个 es服务，我当前服务不是集群，所以目前只配置一个
        return client;

    }
}


@ConfigurationProperties(prefix = "es.config")
class EsConfigProperties {


    List<String> address;

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
