package com.ischoolbar.programmer.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.dao.UserDao;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.service.UserService;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	@Override
	public User findByUserName(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUserName(username);
	}


}
