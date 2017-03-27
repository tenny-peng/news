package com.news.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.news.dao.IClickDao;
import com.news.pojo.Click;
import com.news.service.IClickService;

@Service("clickService")
public class ClickServiceImpl implements IClickService {

	@Resource
	private IClickDao clickDao;

	@Override
	public int addClick(Click click) {
		return clickDao.addClick(click);
	}

	@Override
	public Click findByClick(Click click) {
		return clickDao.findByClick(click);
	}

	@Override
	public void deleteClick(Click click) {
		clickDao.deleteClick(click);
	}

}
