package io.pivotal.datatx.source.gemfire;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GemfireSourceConfiguration.class)
public class SpringCloudStarterStreamSourceGemfireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStarterStreamSourceGemfireApplication.class, args);
	}
}
