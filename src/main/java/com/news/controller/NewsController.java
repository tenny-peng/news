package com.news.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.news.pojo.News;
import com.news.pojo.Record;
import com.news.service.INewsService;
import com.news.service.IRecordService;
import com.news.service.IUserService;

@RestController
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private INewsService newsService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IRecordService recordService;

	@RequestMapping(value = "/getnewsbyid", method = RequestMethod.POST)
	public Map<String, Object> getNewsById(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String newId = request.getParameter("newsId");
		News news = newsService.getOneNewsById(Integer.parseInt(newId));
		result.put("info", news);
		return result;
	}

	@RequestMapping(value = "/addnews", method = RequestMethod.POST)
	public Map<String, Object> addNews(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		Integer type = Integer.valueOf(request.getParameter("type"));
		Integer author = Integer.valueOf(request.getParameter("author"));
		String keyword = request.getParameter("keyword");
		String content = request.getParameter("content");
		String src = request.getParameter("src");
		News news = new News();
		news.setAuthor(author);
		news.setTitle(title);
		news.setType(type);
		news.setKeyword(keyword);
		news.setContent(content);
		news.setCreateTime(new Date());
		news.setSrc(src);
		if (null == id || id.equals("")) {
			// 根据标题过滤重复新闻
			List<News> existNews = newsService.getNewsByTitle(title);
			if (existNews == null || existNews.size() == 0) {
				newsService.addOneNews(news);
			}
		} else {
			Integer newsId = Integer.valueOf(id);
			news.setId(newsId);
			newsService.upNewsByNews(news);
		}
		result.put("message", MessageHelper.getMessage("n0001"));
		return result;
	}

	@RequestMapping(value = "/getpersonalnews", method = RequestMethod.POST)
	public Map<String, Object> getPersonalNews(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<News> resultNews = new ArrayList<News>();
		Integer startNum = 0;
		Integer allNum = 30;
		String startNumstr = request.getParameter("startNum");
		if (null != startNumstr) {
			startNum = Integer.valueOf(startNumstr);
		}
		String allNumStr = request.getParameter("allNum");
		if (null != allNumStr) {
			allNum = Integer.valueOf(allNumStr);
		}
		// 返回的新闻总数30
		Integer sumNum = allNum;

		// 是否个性化，许多情况会导致否【1未登录：1.1，用户名或密码为空；1.2，用户名或密码不对; 2登录但没有点击过新闻，没有用户记录】
		boolean isPersonal = false;
		// 如果有登录，则按其历史浏览记录查询对应的新闻，否则按时间排序取靠前即可
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (null != userIdObj) {
			Integer userId = Integer.valueOf(userIdObj.toString());
			// 个性化新闻总数27
			Integer personalSumNum = sumNum / 10 * 9;
			// 历史浏览总数,用来计算各类型新闻比例
			Integer sumCount = 0;
			List<Record> records = recordService.findByUserId(userId);
			if (null != records && records.size() >= 0) {
				// 用户登录了且有过点击记录才可以做个性化删选
				isPersonal = true;
				for (Record record : records) {
					Integer count = record.getCount();
					sumCount = sumCount + count;
				}
				for (Record record : records) {
					Integer newsType = record.getNewsType();
					Integer count = record.getCount();
					double percent = count.doubleValue() / sumCount.doubleValue();
					Integer num = (int) Math.round(percent * personalSumNum);
					Map<String, Object> newsParam = new HashMap<String, Object>();
					newsParam.put("type", newsType);
					newsParam.put("startNum", startNum);
					newsParam.put("allNum", num);
					List<News> news = newsService.getNewsByLimit(newsParam);
					resultNews.addAll(news);
				}
				// 实际剩于非个性化的新闻数，用最热新闻填充
				Integer remainNum = sumNum - resultNews.size();
				List<Integer> newsIds = new ArrayList<Integer>();
				for (News news : resultNews) {
					Integer newsId = news.getId();
					if (!newsIds.contains(newsId)) {
						newsIds.add(newsId);
					}
				}
				Map<String, Object> hotNewsParam = new HashMap<String, Object>();
				hotNewsParam.put("idList", newsIds);
				hotNewsParam.put("startNum", startNum);
				hotNewsParam.put("allNum", remainNum);
				List<News> news = newsService.getHotNewsByLimit(hotNewsParam);
				resultNews.addAll(news);
			}
		}
		// 如果不个性化，就按时间排序取靠前即可
		if (!isPersonal) {
			Map<String, Object> newsParam = new HashMap<String, Object>();
			newsParam.put("startNum", startNum);
			newsParam.put("allNum", allNum);
			resultNews = newsService.getNewsByLimit(newsParam);

		}
		if (null != resultNews && resultNews.size() > 0) {
			result.put("info", resultNews);
		}
		return result;
	}

	@RequestMapping(value = "/getnewsbylimit", method = RequestMethod.POST)
	public Map<String, Object> getNewsLimit(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer startNum = 0;
		Integer allNum = 30;
		String startNumstr = request.getParameter("startNum");
		if (null != startNumstr) {
			startNum = Integer.valueOf(startNumstr);
		}
		String allNumStr = request.getParameter("allNum");
		if (null != allNumStr) {
			allNum = Integer.valueOf(allNumStr);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startNum", startNum);
		param.put("allNum", allNum);

		String type = request.getParameter("type");
		if (null != type && !type.equals("")) {
			param.put("type", type);
		}
		String queryWord = request.getParameter("queryWord");
		if (null != queryWord && !queryWord.equals("")) {
			param.put("queryWord", queryWord);
		}
		String startTime = request.getParameter("startTime");
		if (null != startTime && !startTime.equals("")) {
			param.put("startTime", startTime);
		}
		String endTime = request.getParameter("endTime");
		if (null != startTime && !startTime.equals("")) {
			param.put("endTime", endTime);
		}
		List<News> news = newsService.getNewsByLimit(param);
		if (null != news && news.size() > 0) {
			result.put("info", news);
		}
		return result;
	}

	@RequestMapping(value = "/getagreeorcollectnews", method = RequestMethod.POST)
	public Map<String, Object> getAgreeOrCollectNews(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("message", MessageHelper.getMessage("u0004"));
			return result;
		}
		String type = request.getParameter("type");
		if (type == null || type.trim().equals("")) {
			result.put("message", MessageHelper.getMessage("u1003"));
			return result;
		}

		Integer startNum = 0;
		Integer allNum = 15;
		String startNumstr = request.getParameter("startNum");
		if (null != startNumstr) {
			startNum = Integer.valueOf(startNumstr);
		}
		String allNumStr = request.getParameter("allNum");
		if (null != allNumStr) {
			allNum = Integer.valueOf(allNumStr);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userIdObj);
		param.put("type", type);
		param.put("startNum", startNum);
		param.put("allNum", allNum);

		List<News> news = newsService.getAgreeOrCollectNewsByLimit(param);
		if (null != news && news.size() > 0) {
			result.put("info", news);
		}
		return result;
	}

	@RequestMapping(value = "/delnewsbyid", method = RequestMethod.POST)
	public Map<String, Object> delNewsById(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("message", MessageHelper.getMessage("u0004"));
		} else {
			String id = request.getParameter("id");
			if (null == id) {
				result.put("message", MessageHelper.getMessage("u1003"));
			} else {
				Integer newsId = Integer.valueOf(id);
				newsService.delOneNewsByNewsId(newsId);
				result.put("message", MessageHelper.getMessage("u1002"));
			}
		}
		return result;
	}

	@RequestMapping(value = "/getrecommendnews", method = RequestMethod.POST)
	public Map<String, Object> getRecommendNews(HttpServletRequest request, Model model) {
		// 总的返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 根据前置条件找出的新闻集合
		List<News> newsList = new ArrayList<News>();
		// 最后筛选出的新闻集合
		List<News> resultNews = new ArrayList<News>();
		// 根据前置条件找出的新闻集合的大小
		int size = 0;
		// 最后筛选出的新闻集合的大小
		int returnSize = 3;
		// 剩余未定的新闻数量
		int restSize = returnSize - size;
		// 已存在于newsList中的newsId集合,防止重复添加
		List<Integer> existNewsId = new ArrayList<Integer>();

		String newId = request.getParameter("newsId");
		News news = newsService.getOneNewsById(Integer.parseInt(newId));
		Integer type = news.getType();
		String keyword = news.getKeyword();// keyword = [深圳，台风];
		String[] keywordList = null;
		if (keyword != null) {
			keywordList = keyword.split("，");
		}

		// 查询过滤条件
		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(Integer.valueOf(newId));
		param.put("idList", idList);

		if (keywordList != null && keywordList.length > 0) {
			// 查询最近两周的所有数据【根据所有关键词过滤后再合并】
			for (int i = 0; i < keywordList.length; i++) {
				String singleKeyword = keywordList[i];
				param.put("singleKeyword", singleKeyword);
				// 根据单个关键词查找出来的数据
				List<News> tempList = newsService.getNewsByLimit(param);
				for (News tempNews : tempList) {
					Integer newsId = tempNews.getId();
					if (!existNewsId.contains(newsId)) {
						existNewsId.add(newsId);
						newsList.add(tempNews);
					}
				}
			}
		}
		size = newsList.size();
		// 给每个新闻结果一个权重等级
		// 1,关键词完全一样的【深圳，台风】
		// 2,包含关键词【深圳，台风，男子，吹飞】,【深圳1，台风】
		// 3,单个匹配【深圳】，【台风】
		for (News tempNews : newsList) {
			String tempKeyword = tempNews.getKeyword();
			if (keyword.equals(tempKeyword)) {
				tempNews.setLevel(Constant.HIGHLEVEL);
			} else {
				// 集合中新闻包含当前新闻关键词的个数
				Integer matchNum = 0;
				String[] tempKeywordList = tempKeyword.split("，");
				for (String singleKeyword : keywordList) {
					for (String tempSingleKeyword : tempKeywordList) {
						if (tempSingleKeyword.contains(singleKeyword)) {
							matchNum++;
						}
					}
				}
				tempNews.setLevel(matchNum);
			}
		}
		// 根据level倒序排序，取level最高的三个，相同level的取时间最新
		Collections.sort(newsList, new Comparator<News>() {
			@Override
			public int compare(News news1, News news2) {
				if (news1.getLevel() > news2.getLevel()) {
					return -1;
				} else if (news1.getLevel() < news2.getLevel()) {
					return 1;
				} else if (news1.getLevel() == news2.getLevel()) {
					return news1.getCreateTime().getTime() > news2.getCreateTime().getTime() ? -1 : 1;
				}
				return 0;
			}
		});
		// 如果本身有3个及其以上的新闻结果，直接给返回结果集赋值即可
		if (size >= returnSize) {
			resultNews = newsList.subList(0, returnSize);
		} else {
			// 如果原结果集不足3个，则将原结果集赋予返回结果集，然后找寻类型相同的新闻
			restSize = returnSize - size;
			resultNews.addAll(newsList);
			for (News tempNews : resultNews) {
				if (!idList.contains(tempNews.getId())) {
					idList.add(tempNews.getId());
				}
			}
			param.remove("singleKeyword");
			param.put("type", type);
			param.put("startNum", 0);
			param.put("allNum", restSize);
			List<News> typeList = newsService.getNewsByLimit(param);
			int typeSize = typeList.size();
			// 全部放入返回结果集,如果相同类型的还不能补足3个新闻，再查最热新闻
			resultNews.addAll(typeList);
			if (typeSize < restSize) {
				for (News tempNews : resultNews) {
					if (!idList.contains(tempNews.getId())) {
						idList.add(tempNews.getId());
					}
				}
				restSize = restSize - typeSize;
				param.remove("type");
				param.put("allNum", restSize);
				// 查询restSize个两周内的最热新闻
				List<News> hotList = newsService.getHotNewsByLimit(param);
				// 最热新闻不可能没有值了，到这里至少一个，多则3个。三个新闻都没有还算新闻网站么，还能愉快的玩耍嘛~
				if (null != hotList && hotList.size() > 0) {
					resultNews.addAll(hotList);
				}
			}
		}
		result.put("info", resultNews);
		return result;
	}
}
