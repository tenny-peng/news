package com.news.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.news.common.constant.Constant;
import com.news.common.utils.MessageHelper;
import com.news.pojo.Click;
import com.news.pojo.News;
import com.news.pojo.Record;
import com.news.pojo.User;
import com.news.service.IClickService;
import com.news.service.INewsService;
import com.news.service.IRecordService;
import com.news.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserControll {
	@Autowired
	private IUserService userService;

	@Autowired
	private INewsService newsService;

	@Autowired
	private IRecordService recordService;

	@Autowired
	private IClickService clickService;

	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public Map<String, Object> addUser(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null) {
			result.put("message", MessageHelper.getMessage("u0001"));
			return result;
		}
		User oldUser = userService.getOneUserByName(username);
		if (oldUser != null) {
			result.put("message", MessageHelper.getMessage("u0002", new String[] { username }));
			return result;
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCreateTime(new Date());
		user.setType(2);
		userService.addOneUser(user);
		result.put("code", 200);
		return result;
	}

	@RequestMapping(value = "/getallusers", method = RequestMethod.POST)
	public Map<String, Object> getAllUsers(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();

		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object usertypeObj = session.getAttribute("userType");
		if (userIdObj == null || usertypeObj == null || Integer.valueOf(usertypeObj.toString()) != 1) {
			result.put("message", MessageHelper.getMessage("u1001"));
			return result;
		}

		Integer type = 0;
		String typeObj = request.getParameter("type");
		if (null != typeObj) {
			type = Integer.valueOf(typeObj);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		List<User> users = userService.getAllUsers(param);
		result.put("info", users);
		return result;
	}

	@RequestMapping(value = "/deluserbyid", method = RequestMethod.POST)
	public Map<String, Object> delUserById(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object usertypeObj = session.getAttribute("userType");
		if (userIdObj == null || usertypeObj == null || Integer.valueOf(usertypeObj.toString()) != 1) {
			result.put("message", MessageHelper.getMessage("u1001"));
			return result;
		}

		String id = request.getParameter("id");
		if (id != null) {
			userService.delOneUserById(Integer.valueOf(id));
			result.put("message", MessageHelper.getMessage("u1002"));
		} else {
			result.put("message", MessageHelper.getMessage("u1003"));
		}
		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", username);
		param.put("password", password);
		User user = userService.getOneUserByNamePwd(param);
		if (user == null) {
			result.put("message", MessageHelper.getMessage("u0003"));
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute("userId", user.getId());
			session.setAttribute("userType", user.getType());
			result.put("userType", user.getType());// 登录页面以此判断跳转页面
			result.put("code", 200);
		}
		return result;
	}

	@RequestMapping(value = "/loginout", method = RequestMethod.POST)
	public Map<String, Object> loginOut(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		session.setAttribute("userId", null);
		session.setAttribute("userType", null);
		result.put("code", 200);
		return result;
	}

	@RequestMapping(value = "/isLogin", method = RequestMethod.POST)
	public Map<String, Object> isLogin(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("message", MessageHelper.getMessage("u0004"));
			return result;
		}
		result.put("code", 200);
		return result;
	}

	@RequestMapping(value = "/isAdminLogin", method = RequestMethod.POST)
	public Map<String, Object> isAdminLogin(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object userTypeObj = session.getAttribute("userType");
		if (userIdObj == null || userTypeObj == null || Integer.valueOf(userTypeObj.toString()) != 1) {
			result.put("message", MessageHelper.getMessage("u1001"));
			return result;
		}
		result.put("code", 200);
		return result;
	}

	@RequestMapping(value = "/setuserrecord", method = RequestMethod.POST)
	public Map<String, Object> setUserRecord(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		String newsId = request.getParameter("newsId");
		if (userIdObj != null && newsId != null) {
			Integer userId = Integer.valueOf(userIdObj.toString());
			News news = newsService.getOneNewsById(Integer.valueOf(newsId));
			if (null != news) {
				int newsType = news.getType();

				Click click = new Click();
				click.setUserId(userId);
				click.setNewsId(Integer.valueOf(newsId));
				click.setType(Constant.TYPE_CLICK);
				// 查询当前用户是否点击过当前新闻, 若没有，则 1,添加点击记录 ; 2,更新新闻点击量; 3,用户点击该类型新闻记录更新
				Click oldClick = clickService.findByClick(click);
				if (null == oldClick) {
					// 1)添加点击记录
					click.setCreateTime(new Date());
					clickService.addClick(click);
					// 2)更新新闻点击量
					Integer clickNum = news.getClick();
					if (clickNum == null) {
						clickNum = 1;
					} else {
						clickNum++;
					}
					news.setClick(clickNum);
					newsService.upNewsByNews(news);
					// 3)用户点击该类型新闻记录更新
					Map<String, Object> recordParam = new HashMap<String, Object>();
					recordParam.put("userId", userId);
					recordParam.put("newsType", newsType);
					Record record = recordService.findByParam(recordParam);
					if (null != record) {
						int count = record.getCount();
						record.setCount(count + 1);
						recordService.updateRecord(record);
					} else {
						record = new Record();
						record.setUserId(userId);
						record.setNewsType(newsType);
						record.setCount(1);
						recordService.addRecord(record);
					}
				}
			}
		}
		return result;
	}

	@RequestMapping(value = "/getagreeandcollectstate", method = RequestMethod.POST)
	public Map<String, Object> getAgreeAndCollectState(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		String newsId = request.getParameter("newsId");
		if (userIdObj != null && newsId != null) {
			Integer userId = Integer.valueOf(userIdObj.toString());
			// 查询当前用户是否点赞/收藏过当前新闻
			Click queryClick = new Click();
			queryClick.setUserId(userId);
			queryClick.setNewsId(Integer.valueOf(newsId));
			queryClick.setType(Constant.TYPE_AGREE);
			Click agreeClick = clickService.findByClick(queryClick);
			if (null != agreeClick) {
				result.put("agreeClick", true);
			}
			queryClick.setType(Constant.TYPE_COLLECT);
			Click collectClick = clickService.findByClick(queryClick);
			if (null != collectClick) {
				result.put("collectClick", true);
			}
		}
		return result;
	}

	@RequestMapping(value = "/changeagreeorcollectstate", method = RequestMethod.POST)
	public Map<String, Object> changeAgreeOrCollectState(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("code", 400);
			result.put("message", MessageHelper.getMessage("u0004"));
			return result;
		}
		String newsId = request.getParameter("newsId");
		String typeStr = request.getParameter("type");
		if (newsId != null && typeStr != null) {
			News news = newsService.getOneNewsById(Integer.valueOf(newsId));
			Integer userId = Integer.valueOf(userIdObj.toString());
			Integer clickType = Integer.valueOf(typeStr);
			// 查询当前用户是否点赞/收藏过当前新闻
			Click queryClick = new Click();
			queryClick.setUserId(userId);
			queryClick.setNewsId(Integer.valueOf(newsId));
			queryClick.setType(clickType);
			Click isClick = clickService.findByClick(queryClick);
			// 如果未点赞/收藏，就新增点赞/收藏记录
			if (null == isClick) {
				queryClick.setCreateTime(new Date());
				clickService.addClick(queryClick);
				// 更新点赞/收藏数
				if (clickType == Constant.TYPE_AGREE) {
					Integer agreeNum = news.getAgree();
					if (agreeNum == null) {
						agreeNum = 1;
					} else {
						agreeNum++;
					}
					news.setAgree(agreeNum);
					newsService.upNewsByNews(news);
				} else if (clickType == Constant.TYPE_COLLECT) {
					Integer collectNum = news.getCollect();
					if (collectNum == null) {
						collectNum = 1;
					} else {
						collectNum++;
					}
					news.setCollect(collectNum);
					newsService.upNewsByNews(news);
				}
			} else {
				// 否则删除之
				clickService.deleteClick(queryClick);
				// 更新点赞/收藏数
				if (clickType == Constant.TYPE_AGREE) {
					Integer agreeNum = news.getAgree();
					if (agreeNum != null && agreeNum > 0) {
						agreeNum--;
					}
					news.setAgree(agreeNum);
					newsService.upNewsByNews(news);
				} else if (clickType == Constant.TYPE_COLLECT) {
					Integer collectNum = news.getCollect();
					if (collectNum != null && collectNum > 0) {
						collectNum--;
					}
					news.setCollect(collectNum);
					newsService.upNewsByNews(news);
				}
			}
		}
		return result;
	}

}
