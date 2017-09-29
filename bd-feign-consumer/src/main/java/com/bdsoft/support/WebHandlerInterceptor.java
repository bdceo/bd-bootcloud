package com.bdsoft.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/9/27 15:27
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebHandlerInterceptor implements HandlerInterceptor {

    private static Log logger = LogFactory.getLog(WebHandlerInterceptor.class);

    private String bdHeader;
    private String bdCookie;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle init");
        bdHeader = httpServletRequest.getHeader("bd-header");
        List<Cookie> ckList = Arrays.stream(httpServletRequest.getCookies()).filter(c -> c.getName().equalsIgnoreCase("bd-cookie")).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ckList)) {
            bdCookie = ckList.get(0).getValue();
        }
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("post handle ");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("fin");
    }

    public String getBdHeader() {
        return bdHeader;
    }

    public String getBdCookie() {
        return bdCookie;
    }
}
