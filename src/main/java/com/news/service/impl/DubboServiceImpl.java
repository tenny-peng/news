package com.news.service.impl;

import com.news.service.IDubboService;

/**
 * TODO
 * 
 * @author tenny.peng
 */
public class DubboServiceImpl implements IDubboService {

	@Override
	public void sayHello(String name) {
		System.out.println("hello: " + name);
	}

}
