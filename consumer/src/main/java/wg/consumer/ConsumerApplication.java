package wg.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("wg.consumer.test.mapper")
public class ConsumerApplication {

    @Value("${test.value:}")
    public String name;
    @Value("${bootstrap.value:}")
    public String name2;
    @Value("${application.value:}")
    public String name3;
    @Value("${spring.datasource.datasource.url:}")
    public String url;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ConsumerApplication.class, args);

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n--------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path+ "/index.html\n\t" +
                "swagger-ui: \thttp://localhost:" + port + path + "/swagger-ui.html\n\t" +
                "----------------------------------------------------------");
    }

    @Configuration
    class BeanConfig{

        @Bean
        @LoadBalanced
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            System.out.println("------------"+name+"------------");
            System.out.println("------------"+name2+"------------");
            System.out.println("------------"+name3+"------------");
            System.out.println("------------"+url+"------------");

            return builder.build();

        }
    }

    @RestController
    public class TestController {

        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

        @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
        public String echo(@PathVariable String str) {
            return restTemplate.getForObject("http://service-provider/echo/" + str, String.class);
        }
    }
}
