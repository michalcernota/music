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
        registry.addResourceHandler("/tracks/**")
            .addResourceLocations("file:C:\\Users\\michc\\IdeaProjects\\music\\tracks\\");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:/Users/michc/IdeaProjects/music/images/");

        registry.addResourceHandler("/images/default/**")
                .addResourceLocations("file:C:/Users/michc/IdeaProjects/music/images/default/");
    }
}
