package hello.config;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class HelloBinder extends AbstractBinder {
  @Override
  protected void configure() {
    // @Context
    bind(HelloInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Object>>() {
    }).in(Singleton.class);
  }
}
