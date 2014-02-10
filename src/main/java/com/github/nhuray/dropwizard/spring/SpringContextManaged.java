package com.github.nhuray.dropwizard.spring;

import io.dropwizard.lifecycle.Managed;
import org.springframework.context.ConfigurableApplicationContext;

import io.dropwizard.lifecycle.Managed;

public class SpringContextManaged implements Managed {
  private final ConfigurableApplicationContext context;

  public SpringContextManaged(final ConfigurableApplicationContext context) {
    this.context = context;
  }

  @Override
  public void start() throws Exception {
    // do nothing
  }

  @Override
  public void stop() throws Exception {
    context.close();
  }
}
