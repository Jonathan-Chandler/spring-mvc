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
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.BindingBuilder;



public class RSender 
{
    @Autowired
    private RabbitTemplate template;

    //@Autowired
    //private DirectExchange direct;

    //@Autowired
    //private Queue queue;

    //@Autowired
    //private TopicExchange topic;

    @Autowired
    private Logger logger;

    //private Queue fanoutQueue;
    //private TopicExchange fanoutExchange;
    //private FanoutExchange fanoutExchange;

	//public RSender()
	//{
	//	fanoutQueue = new Queue("fanout.queue.test");
	//	// not durable, will autodelete
	//	fanoutExchange = new FanoutExchange("amq.fanout", false, true);
	//	BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
	//	//BindingBuilder.bind(fanoutQueue).to(fanoutExchange).with("user.*");
	//}

    //@Scheduled(fixedDelay = 10000, initialDelay = 500)
    //public void send() 
	//{
	//	//logger.error("SENDING from RSENDER SEND");
	//	//String key = "player.list";
    //    //String message = "Hello World!";
	//	TictactoePlayerListDto playerList = new TictactoePlayerListDto("username", true, true);
	//	// send player list to exchange for all users (user.user_name)
    //    //this.template.convertAndSend("amq.topic", "user.*", playerList);
    //    //this.template.convertAndSend("hello", testMessage);

    //    //this.template.convertAndSend(direct.getName(), key, null, message);

    //    //System.out.println(" [x] Sent '" + playerList.toString() + "'");

    //    //logger.info(" [x] Sent '" + playerList.toString() + "'");
    //    //logger.info(" [x] Sent '" + testMessage.toString() + "'");

	//	TestDto testMessage = new TestDto("test");
	//	//String key = "hello";
	//	String key = "user.*";
	//	logger.error("sent " + testMessage.getMessage() + " topic: " + topic.getName());
	//	//template.convertAndSend("auto.exch", key, testMessage.toString());
	//	//template.convertAndSend(topic.getName(), key, testMessage);
	//	template.convertAndSend("amq.topic", key, testMessage);
    //}

    @Scheduled(fixedDelay = 10000, initialDelay = 500)
    public void send() 
	{
		TestDto testMessage = new TestDto("test");
		String topic = "amq.topic";
		String key = "playerlist";
		logger.error("sent message '" + testMessage.getMessage() + "' to topic: " + topic + " key: " + key);
		TictactoePlayerListDto playerList = new TictactoePlayerListDto("username", true, true);

		// exchange, routingkey, object
		template.convertAndSend(topic, key, testMessage);
	}
		//logger.error("SENDING from RSENDER SEND");
		//String key = "player.list";
        //String message = "Hello World!";
		// send player list to exchange for all users (user.user_name)
        //this.template.convertAndSend("amq.topic", "user.*", playerList);
        //this.template.convertAndSend("hello", testMessage);

        //this.template.convertAndSend(direct.getName(), key, null, message);

        //System.out.println(" [x] Sent '" + playerList.toString() + "'");

        //logger.info(" [x] Sent '" + playerList.toString() + "'");
        //logger.info(" [x] Sent '" + testMessage.toString() + "'");

		//String key = "hello";


		// fanout ignores routing key, all subscribers on exchange get messages
		//template.convertAndSend("auto.exch", key, testMessage.toString());
		//template.convertAndSend("amq.fanout", key, testMessage);

		// exchange, routingkey, object
		//template.convertAndSend(fanoutExchange.getName(), key, testMessage);

		// intf org.apache.activemq.broker.region.Region
		//		imp org.apache.activemq.broker.region.TopicRegion
		// org.springframework.amqp.rabbit.connection
		// org.springframework.amqp.rabbit.connection.ShutDownChannelListener
		//		com.rabbitmq.client.Channel
		//		org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory
		//	org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer 
		//	Queue.DeclareOk response = channel.queueDeclarePassive("queue-name");
		//	
		// ConnectionFactory factory = new ConnectionFactory();
		// factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");
		// // provides a custom connection name
		// Connection conn = factory.newConnection("app:audit component:event-consumer");
		//	
		// // returns the number of messages in Ready state in the queue
		// response.getMessageCount();
		// // returns the number of consumers the queue has
		// response.getConsumerCount();
		//
		// addShutdownListener(ShutdownListener listener) and

		//import com.rabbitmq.client.ShutdownSignalException;
		//import com.rabbitmq.client.ShutdownListener;

		//connection.addShutdownListener(new ShutdownListener() {
		//	public void shutdownCompleted(ShutdownSignalException cause)
		//	{
		//		...
		//	}
		//});
		//}

		// ConnectionFactory connectionFactory = new ConnectionFactory();
		// MicrometerMetricsCollector metrics = new MicrometerMetricsCollector();
		// connectionFactory.setMetricsCollector(metrics);
		// ...
		// metrics.getPublishedMessages(); // get Micrometer's Counter object
		//
		// ConnectionFactory factory = new ConnectionFactory();
		// factory.setHost("localhost");
		// factory.setPort(5671);
		//
	// amqpTemplate.convertAndSend(exchange, routingKey, messageData)
	// amqp
}
