package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	public UserService() {
		System.out.println("SimpleService constructor");
	}
	
	public User save(User user) {
		userDao.save(user);
		return user;
	}
	
	public void delete(User user) {
		userDao.delete(user);
	}
	
	public void deleteById(Long id) {
		userDao.deleteById(id);
	}
	
	public List<User> findAll() {
		List<User> users = userDao.findAll();
		return users;
	}
}
