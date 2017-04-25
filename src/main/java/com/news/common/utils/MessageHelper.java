package com.news.common.utils;

import org.springframework.context.MessageSource;

public class MessageHelper {

	private static MessageSource messageSource;

	public static void setMessageSource(MessageSource messageSource) {
		MessageHelper.messageSource = messageSource;
	}

	public static String getMessage(String code) {
		return getMessage(code, null);
	}

	public static String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, null);
	}

}
