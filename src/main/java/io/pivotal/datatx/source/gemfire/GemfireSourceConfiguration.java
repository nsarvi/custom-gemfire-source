package io.pivotal.datatx.source.gemfire;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.gemfire.JsonObjectTransformer;
import org.springframework.cloud.stream.app.gemfire.config.GemfirePoolConfiguration;
import org.springframework.cloud.stream.app.gemfire.source.GemfireSourceProperties;
import org.springframework.cloud.stream.app.gemfire.source.KeyInterestConfiguration;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.MessageChannel;
import io.pivotal.datatx.source.gemfire.GemfireClientRegionConfiguration;

@EnableBinding(Source.class)
@Import({ KeyInterestConfiguration.class,
		GemfirePoolConfiguration.class,
		GemfireClientRegionConfiguration.class,
})
@EnableConfigurationProperties(GemfireSourceProperties.class)
@PropertySource("classpath:gemfire-source.properties")
public class GemfireSourceConfiguration {

	@Autowired
	private GemfireSourceProperties config;

	@Resource(name = "clientRegion")
	private Region<String, ?> region;

	@Autowired
	@Qualifier(Source.OUTPUT)
	private MessageChannel output;

	@Bean
	public MessageChannel convertToStringChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel routerChannel() {
		return new DirectChannel();
	}

	@Bean
	PayloadTypeRouter payloadTypeRouter() {
		PayloadTypeRouter router = new PayloadTypeRouter();
		router.setDefaultOutputChannel(output);
		router.setChannelMapping(PdxInstance.class.getName(), "convertToStringChannel");

		return router;
	}

	@Bean
	public IntegrationFlow startFlow() {
		return IntegrationFlows.from(routerChannel())
				.route(payloadTypeRouter())
				.get();
	}

	@Bean
	JsonObjectTransformer transformer() {
		return new JsonObjectTransformer();
	}

	@Bean
	IntegrationFlow convertToString() {
		
		return IntegrationFlows.from(convertToStringChannel())
				.transform(transformer(), "toString")
				.channel(output)
				.get();
	}

	@Bean
	public CacheListeningMessageProducer cacheListeningMessageProducer() {
		
		CacheListeningMessageProducer cacheListeningMessageProducer = new
				CacheListeningMessageProducer(region);
		cacheListeningMessageProducer.setOutputChannel(routerChannel());
		cacheListeningMessageProducer.setPayloadExpression(
				config.getCacheEventExpression());
		return cacheListeningMessageProducer;
	}
}

