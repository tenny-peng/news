package com.news.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.news.dao.INewsDao;
import com.news.pojo.News;
import com.news.service.INewsService;

@Service("newsService")
public class NewsServiceImpl implements INewsService {

	@Resource
	private INewsDao newsDao;

	@Override
	public int addOneNews(News news) {
		return newsDao.addOneNews(news);
	}

	@Override
	public int upNewsByNews(News news) {
		return newsDao.upNewsByNews(news);
	}

	@Override
	public News getOneNewsById(int newId) {
		return newsDao.getOneNewsById(newId);
	}

	@Override
	public List<News> getNewsByLimit(Map<String, Object> param) {
		return newsDao.getNewsByLimit(param);
	}

	@Override
	public List<News> getHotNewsByLimit(Map<String, Object> param) {
		return newsDao.getHotNewsByLimit(param);
	}

	@Override
	public List<News> getAgreeOrCollectNewsByLimit(Map<String, Object> param) {
		return newsDao.getAgreeOrCollectNewsByLimit(param);
	}

	@Override
	public List<News> getNewsByTitle(String title) {
		return newsDao.getNewsByTitle(title);
	}

	@Override
	public int delOneNewsByNewsId(int newsId) {
		return newsDao.delOneNewsByNewsId(newsId);
	}
}
