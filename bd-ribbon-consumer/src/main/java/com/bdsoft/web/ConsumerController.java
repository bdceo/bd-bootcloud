package com.bdsoft.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bdsoft.service.ProviderService;

@RestController
public class ConsumerController {

	@Autowired
	RestTemplate template;
	@Autowired
	ProviderService service;

	@RequestMapping(value = "/append1", method = RequestMethod.GET)
	public String appendByTemplate() {
		return template.getForEntity("http://BD-PROVIDER/append?a=xx&b=oo", String.class).getBody();
	}

	@RequestMapping(value = "/append2", method = RequestMethod.GET)
	public String appendWithError() {
		return service.append();
	}

}
