package com.bdsoft.feign;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceKeys;
import org.springframework.cloud.sleuth.Tracer;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

public class BDHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
	
	private static final String HYSTRIX_COMPONENT = "hystrix";
	private static final Log log = LogFactory
			.getLog(BDHystrixConcurrencyStrategy.class);

	private final Tracer tracer;
	private final TraceKeys traceKeys;
	private HystrixConcurrencyStrategy delegate;

	public BDHystrixConcurrencyStrategy(Tracer tracer, TraceKeys traceKeys) {
		this.tracer = tracer;
		this.traceKeys = traceKeys;
		try {
			this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
			if (this.delegate instanceof BDHystrixConcurrencyStrategy) {
				// Welcome to singleton hell...
				return;
			}
			HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
					.getInstance().getCommandExecutionHook();
			HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
					.getEventNotifier();
			HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
					.getMetricsPublisher();
			HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
					.getPropertiesStrategy();
			logCurrentStateOfHysrixPlugins(eventNotifier, metricsPublisher,
					propertiesStrategy);
			HystrixPlugins.reset();
			HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
			HystrixPlugins.getInstance()
					.registerCommandExecutionHook(commandExecutionHook);
			HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
			HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
			HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
		}
		catch (Exception e) {
			log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
		}
	}

	private void logCurrentStateOfHysrixPlugins(HystrixEventNotifier eventNotifier,
			HystrixMetricsPublisher metricsPublisher,
			HystrixPropertiesStrategy propertiesStrategy) {
		if (log.isDebugEnabled()) {
			log.debug("Current Hystrix plugins configuration is [" + "concurrencyStrategy ["
					+ this.delegate + "]," + "eventNotifier [" + eventNotifier + "],"
					+ "metricPublisher [" + metricsPublisher + "]," + "propertiesStrategy ["
					+ propertiesStrategy + "]," + "]");
			log.debug("Registering Sleuth Hystrix Concurrency Strategy.");
		}
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		if (callable instanceof HystrixTraceCallable) {
			return callable;
		}
		Callable<T> wrappedCallable = this.delegate != null
				? this.delegate.wrapCallable(callable) : callable;
		if (wrappedCallable instanceof HystrixTraceCallable) {
			return wrappedCallable;
		}
		return new HystrixTraceCallable<>(this.tracer, this.traceKeys, wrappedCallable);
	}

	// Visible for testing
	static class HystrixTraceCallable<S> implements Callable<S> {

		private static final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

		private Tracer tracer;
		private TraceKeys traceKeys;
		private Callable<S> callable;
		private Span parent;

		public HystrixTraceCallable(Tracer tracer, TraceKeys traceKeys,
				Callable<S> callable) {
			this.tracer = tracer;
			this.traceKeys = traceKeys;
			this.callable = callable;
			this.parent = tracer.getCurrentSpan();
		}

		@Override
		public S call() throws Exception {
			Span span = this.parent;
			boolean created = false;
			if (span != null) {
				span = this.tracer.continueSpan(span);
				if (log.isDebugEnabled()) {
					log.debug("Continuing span " + span);
				}
			}
			else {
				span = this.tracer.createSpan(HYSTRIX_COMPONENT);
				created = true;
				if (log.isDebugEnabled()) {
					log.debug("Creating new span " + span);
				}
			}
			if (!span.tags().containsKey(Span.SPAN_LOCAL_COMPONENT_TAG_NAME)) {
				this.tracer.addTag(Span.SPAN_LOCAL_COMPONENT_TAG_NAME, HYSTRIX_COMPONENT);
			}
			String asyncKey = this.traceKeys.getAsync().getPrefix()
					+ this.traceKeys.getAsync().getThreadNameKey();
			if (!span.tags().containsKey(asyncKey)) {
				this.tracer.addTag(asyncKey, Thread.currentThread().getName());
			}
			try {
				return this.callable.call();
			}
			finally {
				if (created) {
					if (log.isDebugEnabled()) {
						log.debug("Closing span since it was created" + span);
					}
					this.tracer.close(span);
				}
				else if(this.tracer.isTracing()) {
					if (log.isDebugEnabled()) {
						log.debug("Detaching span since it was continued " + span);
					}
					this.tracer.detach(span);
				}
			}
		}

	}
}
