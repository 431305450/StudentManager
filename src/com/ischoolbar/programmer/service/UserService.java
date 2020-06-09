package com.ischoolbar.programmer.service;

import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.entity.User;

@Service
public interface UserService {
	public User findByUserName(String username);

}
