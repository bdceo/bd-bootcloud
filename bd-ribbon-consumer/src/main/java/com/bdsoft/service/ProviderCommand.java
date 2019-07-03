package com.bdsoft.service;

import com.bdsoft.web.ConsumerController;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

/**
 * 功能
 *
 * @author 丁辰叶
 * @version 1.0
 * @date 2018/5/21 9:57
 */
public class ProviderCommand extends HystrixCommand<String> {

    private static final HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("providerCommandKey");
    private static final HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("providerGroup");

    private RestTemplate template;

    public ProviderCommand(RestTemplate template) {
        // 指定组标识，命令标识
        super(Setter.withGroupKey(groupKey).andCommandKey(commandKey));
        this.template = template;
    }

    // 开启请求缓存
    @Override
    protected String getCacheKey() {
        return super.getCacheKey();
    }

    // 清除无效缓存：HystrixRequestCache
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(commandKey, HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }

    @Override
    protected String run() throws Exception {
        String res = template.getForEntity("unirest://" + ConsumerController.PROVIDER_SERVICE_ID + "/v1/provider/append?a=To&b=From", String.class).getBody();
        return res;
    }
}
