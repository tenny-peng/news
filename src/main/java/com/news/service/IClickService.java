package com.news.service;

import com.news.pojo.Click;

public interface IClickService {

	public int addClick(Click click);

	public Click findByClick(Click click);

	public void deleteClick(Click click);

}
