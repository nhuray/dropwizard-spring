package hello.config;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

public class HelloInjectionResolver implements InjectionResolver<String> {

  private InjectionResolver<String> systemResolver;

  @Override
  public boolean isConstructorParameterIndicator() {
    return false;
  }

  @Override
  public boolean isMethodParameterIndicator() {
    return false;
  }

  @Override
  public Object resolve(Injectee var1, ServiceHandle<?> var2) {
    return "";
  }
}