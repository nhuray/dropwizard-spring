package hello.health;

import com.codahale.metrics.health.HealthCheck;

public class HelloHealthCheck extends HealthCheck {

	public HelloHealthCheck() {
		super();
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
