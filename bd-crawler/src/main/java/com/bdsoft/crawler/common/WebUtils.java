package com.bdsoft.crawler.common;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * web工具类
 */
public class WebUtils {

    /**
     * 提取url参数
     *
     * @param url  带参数的链接
     * @param name 参数名称
     * @return
     */
    public static String getUrlParam(String url, String name) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url无效");
        }

        String paramFlag = "?";
        if (url.indexOf(paramFlag) > 0) {
            Optional<String> param = Arrays.stream(url.split("\\?")[1].split("&"))
                    .filter(p -> p.split("=")[0].equals(name))
                    .findFirst();
            if (param.isPresent()) {
                return param.get().split("=")[1];
            }
        }
        throw new RuntimeException("参数[" + name + "]不存在");
    }

}
