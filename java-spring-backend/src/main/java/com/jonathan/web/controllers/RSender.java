package com.jonathan.web.controllers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.amqp.core.DirectExchange;
import com.jonathan.web.resources.TictactoePlayerListDto;
import org.slf4j.Logger;
import com.jonathan.web.resources.TestDto;
import org.springframework.amqp.core.TopicExchange;



public class RSender 
{
    @Autowired
    private RabbitTemplate template;

    //@Autowired
    //private DirectExchange direct;

    //@Autowired
    //private Queue queue;

    @Autowired
    private TopicExchange topic;

    @Autowired
    private Logger logger;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() 
	{
		//logger.error("SENDING from RSENDER SEND");
		//String key = "player.list";
        //String message = "Hello World!";
		TictactoePlayerListDto playerList = new TictactoePlayerListDto("username", true, true);
		// send player list to exchange for all users (user.user_name)
        //this.template.convertAndSend("amq.topic", "user.*", playerList);
        //this.template.convertAndSend("hello", testMessage);

        //this.template.convertAndSend(direct.getName(), key, null, message);

        //System.out.println(" [x] Sent '" + playerList.toString() + "'");

        //logger.info(" [x] Sent '" + playerList.toString() + "'");
        //logger.info(" [x] Sent '" + testMessage.toString() + "'");

		TestDto testMessage = new TestDto("test");
		//String key = "hello";
		String key = "user.test_user123";
		logger.error("sent " + testMessage.getMessage() + " topic: " + topic.getName());
		//template.convertAndSend("auto.exch", key, testMessage.toString());
		//template.convertAndSend(topic.getName(), key, testMessage);
		template.convertAndSend("amq.topic", key, testMessage);
    }

	// amqpTemplate.convertAndSend(exchange, routingKey, messageData)
	// amqp
}
