package mvccrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MvcCrudApplication extends SpringBootServletInitializer {
    // inherits configure() and servlet setup from the parent class

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MvcCrudApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MvcCrudApplication.class, args);
    }
}
