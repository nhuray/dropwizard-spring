package hello.server_lifecycle_listeners;

import org.eclipse.jetty.server.Server;

import com.yammer.dropwizard.lifecycle.ServerLifecycleListener;

public class HelloServerLifecycleListener implements ServerLifecycleListener {
  @Override
  public void serverStarted(final Server server) {
    System.out.println("my server started");
  }
}
