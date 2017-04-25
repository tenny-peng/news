package com.news.dao;

import java.util.List;
import java.util.Map;

import com.news.pojo.User;

public interface IUserDao {
	public User getOneUserByNamePwd(Map<String, Object> param);

	public User getOneUserById(int id);

	public User getOneUserByName(String uname);

	public int addOneUser(User user);

	public int addOneUser(Map<String, Object> param);

	public int upUserById(User user);

	public int delOneUserById(int id);

	public List<User> getAllUsers(Map<String, Object> param);
}