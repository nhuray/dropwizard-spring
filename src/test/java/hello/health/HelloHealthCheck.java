package hello.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Component;

@Component
public class HelloHealthCheck extends HealthCheck {

	public HelloHealthCheck() {
		super("hello-health");
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
