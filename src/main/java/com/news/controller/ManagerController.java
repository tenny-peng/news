package com.news.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.news.common.utils.HttpRequest;
import com.news.common.utils.MessageHelper;
import com.news.pojo.News;
import com.news.service.INewsService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private INewsService newsService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getinfo", method = RequestMethod.POST)
	public Map<String, Object> getInfo(HttpServletRequest request, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			result.put("message", MessageHelper.getMessage("u0004"));
			return result;
		}

		Map<Integer, String> newsTypeMap = new HashMap<Integer, String>();
		newsTypeMap.put(0, "news_tech");
		newsTypeMap.put(1, "internet");
		newsTypeMap.put(2, "software");
		newsTypeMap.put(3, "%E6%99%BA%E8%83%BD%E5%AE%B6%E5%B1%85");

		Integer newsType = 0;
		String url = "http://www.toutiao.com/api/pc/feed/";
		String param = "";
		String tempCode = "&as=A19588B0A095153&cp=5800B54185C3AE1";

		for (Entry<Integer, String> entry : newsTypeMap.entrySet()) {
			// 新闻数字类型
			newsType = entry.getKey();
			param = "category=" + entry.getValue() + "&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&" + tempCode;
			// 从今日头条获取新闻数据
			String info = HttpRequest.sendGet(url, param);

			JSONObject jasonObject = JSONObject.parseObject(info);
			Map<String, Object> infoMap = jasonObject;
			List<Map<String, String>> infoList = (List<Map<String, String>>) infoMap.get("data");
			String tempTitle = "";
			String source_url = "";
			String newsId = "";
			String src = "";

			for (int i = 0; i < infoList.size(); i++) {
				Map<String, String> newsMap = infoList.get(i);
				// 过滤重复标题的新闻
				if (tempTitle != null && !tempTitle.equals(newsMap.get("title"))) {
					String ad_label = newsMap.get("ad_label");
					// 过滤广告
					if (ad_label == null || !ad_label.equals("广告")) {
						source_url = newsMap.get("source_url");
						newsId = source_url.substring(7, source_url.length() - 1);
						src = newsMap.get("image_url") != null ? newsMap.get("image_url") : newsMap.get("media_avatar_url");

						String newsInfo = HttpRequest.sendGet("http://www.toutiao.com/group/" + newsId, null);
						int titleStartIndex = newsInfo.indexOf("article-title") + 15;
						if (titleStartIndex < 15) {
							continue;
						}
						int titleEndIndex = newsInfo.indexOf("</h1>", titleStartIndex);
						int contentStartIndex = newsInfo.indexOf("article-content") + 17;
						int contentEndIndex = newsInfo.indexOf("</div>", contentStartIndex);
						String title = newsInfo.substring(titleStartIndex, titleEndIndex);
						String content = newsInfo.substring(contentStartIndex, contentEndIndex);
						if (null == title || null == content) {
							continue;
						}
						if (content.startsWith("<div>")) {
							content = content + "</div>";
						}

						String keywordStr = "";
						List<String> keywordList = new ArrayList<String>();

						int keywordListIndex = newsInfo.indexOf("labelList", contentEndIndex);
						if (keywordListIndex > 0) {
							int keyword1StartIndex = newsInfo.indexOf("keyword", keywordListIndex) + 8;
							int keyword1EndIndex = newsInfo.indexOf("target", keyword1StartIndex) - 2;
							String keyword1 = newsInfo.substring(keyword1StartIndex, keyword1EndIndex);
							if (keyword1StartIndex > 8) {
								keywordList.add(keyword1);
							}

							int keyword2StartIndex = newsInfo.indexOf("keyword", keyword1EndIndex) + 8;
							int keyword2EndIndex = newsInfo.indexOf("target", keyword2StartIndex) - 2;
							String keyword2 = newsInfo.substring(keyword2StartIndex, keyword2EndIndex);
							if (keyword2StartIndex > 8) {
								keywordList.add(keyword2);
							}

							int keyword3StartIndex = newsInfo.indexOf("keyword", keyword2EndIndex) + 8;
							int keyword3EndIndex = newsInfo.indexOf("target", keyword3StartIndex) - 2;
							String keyword3 = newsInfo.substring(keyword3StartIndex, keyword3EndIndex);
							if (keyword3StartIndex > 8) {
								keywordList.add(keyword3);
							}

							int keyword4StartIndex = newsInfo.indexOf("keyword", keyword3EndIndex) + 8;
							int keyword4EndIndex = newsInfo.indexOf("target", keyword4StartIndex) - 2;
							String keyword4 = newsInfo.substring(keyword4StartIndex, keyword4EndIndex);
							if (keyword4StartIndex > 8) {
								keywordList.add(keyword4);
							}
						}

						if (keywordList.size() > 0) {
							keywordStr = keywordList.toString();
							keywordStr = keywordStr.substring(1, keywordStr.length() - 1);
						}

						News news = new News();
						news.setAuthor(1);
						news.setTitle(title);
						news.setType(newsType);
						news.setKeyword(keywordStr);
						news.setContent(content);
						news.setCreateTime(new Date());
						news.setSrc(src);
						tempTitle = title;
						// 去除标题重复新闻
						List<News> existNews = newsService.getNewsByTitle(title);
						if (existNews == null || existNews.size() == 0) {
							newsService.addOneNews(news);
						}
					}
				}
			}
		}
		result.put("code", 200);
		return result;
	}

}
