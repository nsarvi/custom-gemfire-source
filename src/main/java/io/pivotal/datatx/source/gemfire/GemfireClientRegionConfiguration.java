package io.pivotal.datatx.source.gemfire;

import java.util.List;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.app.gemfire.config.GemfireClientCacheConfiguration;
import org.springframework.cloud.stream.app.gemfire.config.GemfireRegionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.Interest;
import org.springframework.util.CollectionUtils;

@Configuration
@Import(GemfireClientCacheConfiguration.class)
@EnableConfigurationProperties(GemfireRegionProperties.class)
public class GemfireClientRegionConfiguration {

	@Autowired
	private GemfireRegionProperties config;

	@Autowired
	private ClientCacheFactoryBean clientCache;


	@Autowired(required = false)
	private List<Interest> keyInterests;

	@Bean(name = "clientRegion")
	@SuppressWarnings({"rawtype", "unchecked"})
	public ClientRegionFactoryBean clientRegionFactoryBean() {
		ClientRegionFactoryBean clientRegionFactoryBean = new ClientRegionFactoryBean();
		clientRegionFactoryBean.setRegionName(this.config.getRegionName());
		clientRegionFactoryBean.setDataPolicy(DataPolicy.EMPTY);
		if (!CollectionUtils.isEmpty(this.keyInterests)) {
			clientRegionFactoryBean.setInterests(this.keyInterests.toArray(new Interest[this.keyInterests.size()]));
		}

		try {
			clientCache.setPdxSerializer(new  ReflectionBasedAutoSerializer(".*"));
			clientRegionFactoryBean.setCache(clientCache.getObject());
		} catch (Exception e) {
			throw new BeanCreationException(e.getMessage(), e);
		}
		return clientRegionFactoryBean;
	}

}
