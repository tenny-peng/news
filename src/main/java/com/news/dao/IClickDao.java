package com.news.dao;

import com.news.pojo.Click;

public interface IClickDao {

	public int addClick(Click click);

	public Click findByClick(Click click);

	public void deleteClick(Click click);

}
