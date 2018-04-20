package com.example;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
	
	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	public void init() {
		
		System.out.println("SimpleService post constructor");
		
		User user = new User();
		user.setName("My Name");
		user.setEmail("info@myname.com");
		
		userService.save(user);
		
		List<User> users = userService.findAll();
		for (User u : users) {
			System.out.println( u.getName() );
			userService.deleteById( u.getId() );
		}
	}
}