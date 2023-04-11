package ru.nikitin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("home");
        /*
        Метод addViewControllers() получает экземпляр ViewControllerRegistry, с помощью которого можно
        зарегистрировать один или несколько контроллеров представлений. Здесь мы вызываем addViewController()
        с аргументом "/", определяющим путь в запросах GET,которые должен обрабатывать этот контроллер представления.
         Этот метод возвращает объект ViewControllerRegistration, для которого мы тут же вызываем setViewName(),
         чтобы указать имя home представления, которому должны передаваться запросы "/"
        */
    }
}
