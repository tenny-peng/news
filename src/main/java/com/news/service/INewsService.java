package com.news.service;

import java.util.List;
import java.util.Map;

import com.news.pojo.News;

public interface INewsService {

	public int addOneNews(News news);

	public int upNewsByNews(News news);

	public News getOneNewsById(int id);

	public List<News> getNewsByLimit(Map<String, Object> param);

	public List<News> getHotNewsByLimit(Map<String, Object> param);

	public List<News> getAgreeOrCollectNewsByLimit(Map<String, Object> param);

	public List<News> getNewsByTitle(String title);

	public int delOneNewsByNewsId(int newsId);
}
