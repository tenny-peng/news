package com.news.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.news.dao.IUserDao;
import com.news.pojo.User;
import com.news.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Resource
	private IUserDao userDao;

	@Override
	public int addOneUser(User user) {
		return userDao.addOneUser(user);
	}

	@Override
	public User getOneUserByName(String uname) {
		return userDao.getOneUserByName(uname);
	}

	@Override
	public User getOneUserByNamePwd(Map<String, Object> param) {
		return userDao.getOneUserByNamePwd(param);
	}

	@Override
	public List<User> getAllUsers(Map<String, Object> param) {
		return userDao.getAllUsers(param);
	}

	@Override
	public int delOneUserById(int id) {
		return userDao.delOneUserById(id);
	}
}
