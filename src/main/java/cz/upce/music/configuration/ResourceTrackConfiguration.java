package cz.upce.music.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ResourceTrackConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**")
            .addResourceLocations("file:media/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");

        registry.addResourceHandler("/images/default/**")
                .addResourceLocations("file:images/default/");
    }
}
