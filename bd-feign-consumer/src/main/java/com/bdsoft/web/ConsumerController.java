package com.bdsoft.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bdsoft.service.ProviderClient;

@RestController
public class ConsumerController {

	@Autowired
	ProviderClient client;

	@RequestMapping(value = "/append", method = RequestMethod.GET)
	public String append() {
		return client.append("xx", "oo");
	}
}