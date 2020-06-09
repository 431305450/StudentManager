package com.ischoolbar.programmer.dao;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.User;

@Repository
public interface UserDao {
	public User findByUserName(String username);
}
