package com.bdsoft.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/")
	String hello() {
		System.out.println("dddddd");
		return "hello boot";
	}
}
