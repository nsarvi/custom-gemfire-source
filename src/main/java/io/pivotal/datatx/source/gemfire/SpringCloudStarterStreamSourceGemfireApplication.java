package io.pivotal.datatx.source.gemfire;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.cloud.stream.app.gemfire.source.GemfireSourceConfiguration;

@SpringBootApplication
@Import(org.springframework.cloud.stream.app.gemfire.source.GemfireSourceConfiguration.class)
public class SpringCloudStarterStreamSourceGemfireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStarterStreamSourceGemfireApplication.class, args);
	}
}
