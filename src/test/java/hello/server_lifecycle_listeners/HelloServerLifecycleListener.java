package hello.server_lifecycle_listeners;

import io.dropwizard.lifecycle.ServerLifecycleListener;
import org.eclipse.jetty.server.Server;


public class HelloServerLifecycleListener implements ServerLifecycleListener {
  @Override
  public void serverStarted(final Server server) {
    System.out.println("my server started");
  }
}
