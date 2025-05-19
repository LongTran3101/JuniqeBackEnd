package juniqe.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả endpoint
                        .allowedOrigins("http://localhost:3000") // Cho phép frontend này
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method cho phép
                        .allowedHeaders("*")
                        .allowCredentials(true); // Cho phép gửi cookie, Authorization header nếu có
            }
        };
    }
}
