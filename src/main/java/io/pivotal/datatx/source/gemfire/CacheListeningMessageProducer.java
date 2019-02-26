package io.pivotal.datatx.source.gemfire;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geode.cache.CacheClosedException;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.util.CacheListenerAdapter;
import org.springframework.integration.endpoint.ExpressionMessageProducerSupport;
import org.springframework.integration.gemfire.inbound.EventType;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CacheListeningMessageProducer extends ExpressionMessageProducerSupport {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final Region region;

	private final CacheListener<?, ?> listener;

	private volatile Set<EventType> supportedEventTypes =
			new HashSet<EventType>(Arrays.asList(EventType.CREATED, EventType.UPDATED));


	public CacheListeningMessageProducer(Region<?, ?> region) {
		Assert.notNull(region, "region must not be null");
		this.region = region;
		this.listener = new MessageProducingCacheListener();
	}


	public void setSupportedEventTypes(EventType... eventTypes) {
		Assert.notEmpty(eventTypes, "eventTypes must not be empty");
		this.supportedEventTypes = new HashSet<EventType>(Arrays.asList(eventTypes));
	}

	@Override
	public String getComponentType() {
		return "gemfire:inbound-channel-adapter";
	}

	@Override
	protected void doStart() {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("adding MessageProducingCacheListener to GemFire Region '" + this.region.getName() + "'");
		}
		this.region.getAttributesMutator().addCacheListener(this.listener);
	}

	@Override
	protected void doStop() {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("removing MessageProducingCacheListener from GemFire Region '" + this.region.getName() + "'");
		}
		try {
			this.region.getAttributesMutator().removeCacheListener(this.listener);
		}
		catch (CacheClosedException e) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug(e.getMessage(), e);
			}
		}

	}

	private class MessageProducingCacheListener extends CacheListenerAdapter {

		@Override
		public void afterCreate(EntryEvent event) {
			if (CacheListeningMessageProducer.this.supportedEventTypes.contains(EventType.CREATED)) {
				processEvent(event);
			}
		}

		@Override
		public void afterUpdate(EntryEvent event) {
			if (CacheListeningMessageProducer.this.supportedEventTypes.contains(EventType.UPDATED)) {
				processEvent(event);
			}
		}

		@Override
		public void afterInvalidate(EntryEvent event) {
			if (CacheListeningMessageProducer.this.supportedEventTypes.contains(EventType.INVALIDATED)) {
				processEvent(event);
			}
		}

		@Override
		public void afterDestroy(EntryEvent event) {
			if (CacheListeningMessageProducer.this.supportedEventTypes.contains(EventType.DESTROYED)) {
				processEvent(event);
			}
		}

		private void processEvent(EntryEvent event) {
			
			publish(evaluatePayloadExpression(event));

		}

		private void publish(Object object) {
			Message<?> message = null;
			if (object instanceof Message) {
				message = (Message<?>) object;
			}
			else {
				message = getMessageBuilderFactory().withPayload(object).build();
			}
			sendMessage(message);
		}
	}

}
