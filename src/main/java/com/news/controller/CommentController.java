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

import com.news.common.utils.MessageHelper;
import com.news.pojo.Comment;
import com.news.service.ICommentService;
import com.news.service.IUserService;

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/addcomment", method = RequestMethod.POST)
	public Map<String, Object> addComment(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("message", MessageHelper.getMessage("u0004"));
			return result;
		}

		Integer userId = Integer.valueOf(userIdObj.toString());
		String content = request.getParameter("comment");
		String newsId = request.getParameter("newsId");

		Comment comment = new Comment();
		comment.setContent(content);
		comment.setNewsId(Integer.valueOf(newsId));
		comment.setUserId(userId);
		comment.setCreateTime(new Date());
		commentService.addOneComment(comment);
		result.put("code", 200);
		result.put("message", MessageHelper.getMessage("c0001"));

		return result;
	}

	@RequestMapping(value = "/getcommentsbynewsid", method = RequestMethod.POST)
	public Map<String, Object> getCommentsByNewsId(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String newsId = request.getParameter("newsId");
		if (null != newsId) {
			List<Map<String, Object>> comments = commentService.getCommentsByNewsId(Integer.parseInt(newsId));
			result.put("info", comments);
		}
		return result;
	}

}
