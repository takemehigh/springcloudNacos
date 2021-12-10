package wg.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.DebuggingClassWriter;

@SpringBootApplication
public class EsApplication {

    public static void main(String[] args) {
        System.getProperties().put( "sun.misc.ProxyGenerator.saveGeneratedFiles" , "true" );
       // System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/gw/Documents/CGLIB动态代理类");

        SpringApplication.run(EsApplication.class, args);
    }

}
