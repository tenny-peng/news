package com.news.service;

import java.util.List;
import java.util.Map;

import com.news.pojo.User;

public interface IUserService {

	public int addOneUser(User user);

	public User getOneUserByName(String uname);

	public User getOneUserByNamePwd(Map<String, Object> param);

	public List<User> getAllUsers(Map<String, Object> param);

	public int delOneUserById(int id);

}
