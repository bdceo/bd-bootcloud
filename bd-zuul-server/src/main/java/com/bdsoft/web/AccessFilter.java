package com.bdsoft.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public Object run() {
		RequestContext rc = RequestContext.getCurrentContext();
		HttpServletRequest req = rc.getRequest();
		log.info("{} request to {}", req.getMethod(), req.getRequestURL().toString());

//		Object accessToken = req.getParameter("accessToken");
//		if (accessToken == null) {
//			log.warn("access token is empty");
//			rc.setSendZuulResponse(false);
//			rc.setResponseStatusCode(401);
//			return null;
//		}
//		log.info("access token ok");
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * <pre>
	 * pre：可以在请求被路由之前调用
	routing：在路由请求时候被调用
	post：在routing和error过滤器之后被调用
	error：处理请求时发生错误时被调用
	 * </pre>
	 */
	@Override
	public String filterType() {
		return "pre";
	}

}
