package com.news.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.news.dao.ICommentDao;
import com.news.pojo.Comment;
import com.news.service.ICommentService;

@Service("commentService")
public class CommentServiceImpl implements ICommentService {

	@Resource
	private ICommentDao commentDao;

	@Override
	public int addOneComment(Comment comment) {
		return commentDao.addOneComment(comment);
	}

	@Override
	public List<Map<String, Object>> getCommentsByNewsId(int newsId) {
		return this.commentDao.getCommentsByNewsId(newsId);
	}

}
