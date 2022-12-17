package ecommerce.web.configuration;

import com.google.gson.Gson;
import winter.core.annotation.Bean;
import winter.core.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
