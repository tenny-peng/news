package com.news.dao;

import java.util.List;
import java.util.Map;

import com.news.pojo.Comment;

public interface ICommentDao {

	public int addOneComment(Comment comment);

	public List<Map<String, Object>> getCommentsByNewsId(int newsId);

}
