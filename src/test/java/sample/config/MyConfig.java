//package sample.config;
//
//import com.yammer.dropwizard.spring.config.ConfigurationResolver;
//import org.springframework.context.annotation.*;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//
//@Configuration
//public class MyConfig {
//
//
////    /**
////     * This is the magic that enables variable replacements in @Value definitions
////     */
////    @Bean
////    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
////        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
////        configurer.setPlaceholderPrefix("${p.");
////        configurer.setPlaceholderSuffix("}");
////        return configurer;
////    }
//
//    @Bean
//    public static ConfigurationResolver configurationResolver() {
//        String property = System.getProperty("dropwizard.config.file");
//        if (property == null) return null;
//        ConfigurationResolver placeholder = new ConfigurationResolver();
//        placeholder.setLocation(new FileSystemResource(property));
//        return placeholder;
//    }
//
//
//}
