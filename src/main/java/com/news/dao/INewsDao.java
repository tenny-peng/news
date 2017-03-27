package com.news.dao;

import java.util.List;
import java.util.Map;

import com.news.pojo.News;

public interface INewsDao {

	/**
	 * 
	 * 通过新闻实体添加新闻
	 *
	 * @param news
	 * @return
	 */
	public int addOneNews(News news);

	/**
	 * 
	 * 更新新闻【自改版后已废弃】
	 *
	 * @param news
	 * @return
	 */
	public int upNewsByNews(News news);

	/**
	 * 
	 * 通过id获取新闻详情
	 *
	 * @param newsId
	 * @return
	 */
	public News getOneNewsById(int newsId);

	/**
	 * 
	 * ORDER BY createtime DESC, 通用查询指定数目的新闻
	 *
	 * @param param
	 * @return
	 */
	public List<News> getNewsByLimit(Map<String, Object> param);

	/**
	 * 
	 * ORDER BY click DESC, 按条件查点击量最多的新闻
	 *
	 * @param param
	 * @return
	 */
	public List<News> getHotNewsByLimit(Map<String, Object> param);

	/**
	 * 
	 * 查询用户点赞或收藏新闻
	 *
	 * @param param
	 * @return
	 */
	public List<News> getAgreeOrCollectNewsByLimit(Map<String, Object> param);

	/**
	 * 
	 * 从今日头条插入数据库新闻用，防止重复标题新闻重复插入
	 *
	 * @param title
	 * @return
	 */
	public List<News> getNewsByTitle(String title);

	/**
	 * 
	 * 通过新闻id删除新闻【自改版后已废弃】
	 *
	 * @param newsId
	 * @return
	 */
	public int delOneNewsByNewsId(int newsId);

}
