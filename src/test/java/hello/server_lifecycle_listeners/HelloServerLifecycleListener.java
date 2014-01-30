package hello.server_lifecycle_listeners;

import org.eclipse.jetty.server.Server;

import io.dropwizard.lifecycle.ServerLifecycleListener;

public class HelloServerLifecycleListener implements ServerLifecycleListener {
  @Override
  public void serverStarted(final Server server) {
    System.out.println("my server started");
  }
}
