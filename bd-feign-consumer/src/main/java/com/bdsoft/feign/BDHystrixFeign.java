package com.bdsoft.feign;

import com.netflix.hystrix.HystrixCommand;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.hystrix.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/9/27 18:25
 */
public class BDHystrixFeign {

    public static BDHystrixFeign.Builder builder() {
        return new BDHystrixFeign.Builder();
    }

    public static final class Builder extends Feign.Builder {

        private Contract contract = new Contract.Default();
        private SetterFactory setterFactory = new SetterFactory.Default();

        /**
         * Allows you to override hystrix properties such as thread pools and command keys.
         */
        public BDHystrixFeign.Builder setterFactory(SetterFactory setterFactory) {
            this.setterFactory = setterFactory;
            return this;
        }

        /**
         * @see #target(Class, String, Object)
         */
        public <T> T target(Target<T> target, T fallback) {
            return build(fallback != null ? new FallbackFactory.Default<T>(fallback) : null)
                    .newInstance(target);
        }

        /**
         * @see #target(Class, String, FallbackFactory)
         */
        public <T> T target(Target<T> target, FallbackFactory<? extends T> fallbackFactory) {
            return build(fallbackFactory).newInstance(target);
        }

        /**
         * Like {@link Feign#newInstance(Target)}, except with {@link HystrixCommand#getFallback()
         * fallback} support.
         *
         * <p>Fallbacks are known values, which you return when there's an error invoking an unirest
         * method. For example, you can return a cached result as opposed to raising an error to the
         * caller. To use this feature, pass a safe implementation of your target interface as the last
         * parameter.
         *
         * Here's an example:
         * <pre>
         * {@code
         *
         * // When dealing with fallbacks, it is less tedious to keep interfaces small.
         * interface GitHub {
         *   @RequestLine("GET /repos/{owner}/{repo}/contributors")
         *   List<String> contributors(@Param("owner") String owner, @Param("repo") String repo);
         * }
         *
         * // This instance will be invoked if there are errors of any kind.
         * GitHub fallback = (owner, repo) -> {
         *   if (owner.equals("Netflix") && repo.equals("feign")) {
         *     return Arrays.asList("stuarthendren"); // inspired this approach!
         *   } else {
         *     return Collections.emptyList();
         *   }
         * };
         *
         * GitHub github = HystrixFeign.builder()
         *                             ...
         *                             .target(GitHub.class, "https://api.github.com", fallback);
         * }</pre>
         *
         * @see #target(Target, Object)
         */
        public <T> T target(Class<T> apiType, String url, T fallback) {
            return target(new Target.HardCodedTarget<T>(apiType, url), fallback);
        }

        /**
         * Same as {@link #target(Class, String, T)}, except you can inspect a source exception before
         * creating a fallback object.
         */
        public <T> T target(Class<T> apiType, String url, FallbackFactory<? extends T> fallbackFactory) {
            return target(new Target.HardCodedTarget<T>(apiType, url), fallbackFactory);
        }

        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BDHystrixFeign.Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        @Override
        public Feign build() {
            return build(null);
        }

        /** Configures components needed for hystrix integration. */
        Feign build(final FallbackFactory<?> nullableFallbackFactory) {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @Override public InvocationHandler create(Target target,
                                                          Map<Method, MethodHandler> dispatch) {
                    return new BDHystrixInvocationHandler(target, dispatch, setterFactory, nullableFallbackFactory);
                }
            });
            super.contract(new HystrixDelegatingContract(contract));
            return super.build();
        }

        // Covariant overrides to support chaining to new fallback method.
        @Override
        public BDHystrixFeign.Builder logLevel(Logger.Level logLevel) {
            return (BDHystrixFeign.Builder) super.logLevel(logLevel);
        }

        @Override
        public BDHystrixFeign.Builder client(Client client) {
            return (BDHystrixFeign.Builder) super.client(client);
        }

        @Override
        public BDHystrixFeign.Builder retryer(Retryer retryer) {
            return (BDHystrixFeign.Builder) super.retryer(retryer);
        }

        @Override
        public BDHystrixFeign.Builder logger(Logger logger) {
            return (BDHystrixFeign.Builder) super.logger(logger);
        }

        @Override
        public HystrixFeign.Builder encoder(Encoder encoder) {
            return (HystrixFeign.Builder) super.encoder(encoder);
        }

        @Override
        public BDHystrixFeign.Builder decoder(Decoder decoder) {
            return (BDHystrixFeign.Builder) super.decoder(decoder);
        }

        @Override
        public BDHystrixFeign.Builder mapAndDecode(ResponseMapper mapper, Decoder decoder) {
            return (BDHystrixFeign.Builder) super.mapAndDecode(mapper, decoder);
        }

        @Override
        public BDHystrixFeign.Builder decode404() {
            return (BDHystrixFeign.Builder) super.decode404();
        }

        @Override
        public BDHystrixFeign.Builder errorDecoder(ErrorDecoder errorDecoder) {
            return (BDHystrixFeign.Builder) super.errorDecoder(errorDecoder);
        }

        @Override
        public BDHystrixFeign.Builder options(Request.Options options) {
            return (BDHystrixFeign.Builder) super.options(options);
        }

        @Override
        public BDHystrixFeign.Builder requestInterceptor(RequestInterceptor requestInterceptor) {
            return (BDHystrixFeign.Builder) super.requestInterceptor(requestInterceptor);
        }

        @Override
        public BDHystrixFeign.Builder requestInterceptors(Iterable<RequestInterceptor> requestInterceptors) {
            return (BDHystrixFeign.Builder) super.requestInterceptors(requestInterceptors);
        }
    }
}
