package wg.es.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import wg.es.service.ICourseService;
import wg.es.service.impl.CourseServiceImpl;

@Configuration
public class TestConfiguration {

    TestConfiguration(){
        System.out.println("-------TestConfiguration------");
    }
    // 如果一个bean依赖另一个bean，则直接调用对应JavaConfig类中依赖bean的创建方法即可
    // 这里直接调用dependencyService()
    @Bean
    public ICourseService courseService() {
        System.out.println("调用BEAN");
        return new CourseServiceImpl();
    }

    @Bean
    public Object otherService() {

        System.out.println("otherService--------------------"+courseService());

        Object a =  courseService();
        return a;
    }
    @Bean
    public Object otherService2() {

        System.out.println("otherService2--------------------"+courseService());

        return new Object();
    }
    
}