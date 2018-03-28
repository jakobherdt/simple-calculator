package org.retest.server;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {
	
	Map<String, User> userList;
	
	public UserRepo() {
		userList = new HashMap<>();
	}
	
	public User getUser(String name, String password) {
		return userList.computeIfAbsent(name, n -> new User(name, password));
	}
	
	public void registerUser(User user) {
		userList.put(user.getName(), user);
	}
	
	public UserRepo init() {
		registerUser(createUser("Max", "retest"));
		return this;
	}
	
	private User createUser(String name, String password) {
		User customer = new User(name, password);
		return customer;
	}
	
}
