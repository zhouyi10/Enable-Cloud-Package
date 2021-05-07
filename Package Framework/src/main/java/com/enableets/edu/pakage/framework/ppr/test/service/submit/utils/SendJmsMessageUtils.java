package com.enableets.edu.pakage.framework.ppr.test.service.submit.utils;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import com.enableets.edu.framework.core.util.SpringBeanUtils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;

/**
 * send jms message tool class
 * @author duffy_ding
 * @since 2018/08/08
 */
public class SendJmsMessageUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SendJmsMessageUtils.class);

	/** mq virtual theme prefix */
	private static final String PREFIX_VIRTUAL_TOPIC = "VirtualTopic.";

	/** 
	 * Send jms message
	 * @param destinationName
	 * @param payload
	 * @return
	 */
	public static boolean sendMessage(String destinationName, Object payload) {
		if (StringUtils.isEmpty(destinationName)) {
			logger.warn("no queue name jms message is not sent");
			return false;
		}
		JmsMessagingTemplate template = getJmsMessagingTemplate();
		if (template == null) {
			logger.warn("The jms template does not exist, and the jms message is not sent");
			return false;
		}
		template.convertAndSend(destinationName, payload);
		return true;
	}

	/**
	 * 发送jms virtual topic message topic
	 * @param destinationName
	 * @param payload
	 * @return
	 */
	public static boolean sendVirtualTopicMessage(String destinationName, Object payload) {
		if (StringUtils.isEmpty(destinationName)) {
			logger.warn("No queue name, jms message is not sent");
			return false;
		}
		if (!destinationName.startsWith(PREFIX_VIRTUAL_TOPIC)) {
			destinationName = PREFIX_VIRTUAL_TOPIC + destinationName;
		}
		JmsMessagingTemplate template = getJmsMessagingTemplate();
		if (template == null) {
			logger.warn("The jms template does not exist, and the jms message is not sent");
			return false;
		}
		template.convertAndSend(new ActiveMQTopic(destinationName), payload);
		return true;
	}

	/** 
	 * @return jms message sending template
	 */
	private static JmsMessagingTemplate getJmsMessagingTemplate() {
		return SpringBeanUtils.getBean(JmsMessagingTemplate.class);
	}

	private static void sendMessage(String ip, int port, String userName, String password, String queueName, String msg) throws JMSException {
		//1.创建ConnectionFactory
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, "tcp://" + ip + ":" + port);
		//2.创建Connection
		Connection connection = connectionFactory.createConnection();
		//3.启动连接
		connection.start();
		//4.创建会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个目标
		Destination destination = session.createQueue(queueName);
		//6.创建一个生产者
		MessageProducer producer = session.createProducer(destination);
		//7.创建消息
		TextMessage message = session.createTextMessage(msg);
		//8.发送消息
		producer.send(message);
		//9.关闭连接
		connection.close();
	}

	public static void main(String[] args) throws JMSException {
		String msg = "";
		try {
			JSONObject obj = new JSONObject().put("parentActivityId", "zvlx0lg0rbfojk2w2pcthxwjxod8jox2")
					.put("activityId", null).put("stepId", "zvlx0lg0rbfojk2w2pcthxwjxod8jox2")
					.put("contentId", "user-694667717826945024").put("paperId", "800656395039969280")
					.put("userId", "694662698981498880").put("stepInstanceId", "76qxo20wwnnqyhr3adf4w7r1wf3gupvm");
			msg = new JSONObject().put("activityInfo", obj).put("type", "ANSWER").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String queueName = "Activity.30.Step.Answer.Publish.yh";
//		msg = "{\"activityId\":\"xgxz939d5p9urrsezmt6ticteu3bbtff\",\"typeName\":\"学习活动\",\"hasPaper\":false,\"type\":\"3\"}";
		sendMessage("192.168.116.190", 61516, "edu", "123456q", queueName, msg);
	}
}
