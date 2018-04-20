package com.example;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class SimpleService {

	public SimpleService() {
		System.out.println("SimpleService constructor");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("SimpleService post constructor");
	}
}
