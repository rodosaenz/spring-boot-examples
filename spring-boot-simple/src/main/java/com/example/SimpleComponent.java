package com.example;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class SimpleComponent {

	public SimpleComponent() {
		System.out.println("SimpleComponent constructor");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("SimpleComponent post constructor");
	}
}
