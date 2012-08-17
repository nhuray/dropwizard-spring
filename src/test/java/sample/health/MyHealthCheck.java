package sample.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Component;

@Component
public class MyHealthCheck extends HealthCheck {

	public MyHealthCheck() {
		super("my-health");
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
