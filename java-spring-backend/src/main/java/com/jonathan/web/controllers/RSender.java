package com.jonathan.web.controllers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.amqp.core.DirectExchange;
import com.jonathan.web.resources.TictactoePlayerListDto;

public class RSender 
{
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() 
	{
		String key = "orange";
        String message = "Hello World!";
		TictactoePlayerListDto playerList = new TictactoePlayerListDto("username", true, true);
        this.template.convertAndSend("spring-boot-exchange", "foo.bar.baz", playerList);
        //this.template.convertAndSend(direct.getName(), key, null, message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
