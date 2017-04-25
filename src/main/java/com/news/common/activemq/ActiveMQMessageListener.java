package com.news.common.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 
 * activemq消息监听
 * 
 * @author Tenny.Peng
 */
public class ActiveMQMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMsg = (TextMessage) message;
		try {
			// 处理消息
			System.out.println("receive message from " + textMsg.getJMSDestination() + ": " + textMsg.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
