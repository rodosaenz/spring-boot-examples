package com.example;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class SimpleRepository {

	public SimpleRepository() {
		System.out.println("SimpleRepository constructor");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("SimpleRepository post constructor");
	}
}
