package hello.health;

import com.codahale.metrics.health.HealthCheck;

public class HelloHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
