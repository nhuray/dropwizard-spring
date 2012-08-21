package com.yammer.dropwizard.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DropwizardConfiguration {

    /**
     * Explicitly specify the name of the Spring bean definition associated
     * with this Configuration class.  If left unspecified (the common case),
     * a bean name will be automatically generated.
     * <p/>
     * <p>The custom name applies only if the Configuration class is picked up via
     * component scanning or supplied directly to a {@link org.springframework.context.annotation.AnnotationConfigApplicationContext}.
     * If the Configuration class is registered as a traditional XML bean definition,
     * the name/id of the bean element will take precedence.
     *
     * @return the specified bean name, if any
     * @see org.springframework.beans.factory.support.DefaultBeanNameGenerator
     */
    String value() default "";

}
