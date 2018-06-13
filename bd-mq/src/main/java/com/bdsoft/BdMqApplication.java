package com.bdsoft;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BdMqApplication {

	static String NAMESVR_ADDR="192.168.99.42:9876";

	public static void main(String[] args) throws Exception{
//		SpringApplication.run(BdMqApplication.class, args);

		String topic="bdceo-topic";
		String tag = "bdceo-tag";

		// 生产者
		DefaultMQProducer producer = new DefaultMQProducer("bdceo-producer");
		producer.setNamesrvAddr(NAMESVR_ADDR);
		producer.start();

		// 消费者
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("bdceo-consumer");
		consumer.setNamesrvAddr(NAMESVR_ADDR);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe(topic, "*");
		// 监听消息
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
															ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getName()+" got msg: " + msgs);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();

		// 发送消息
		for(int i=0;i<10;i++){
			String txt = new StringBuilder("bd msg of num=").append(i).toString();
			Message msg = new Message(topic, tag, txt.getBytes("UTF-8"));

			SendResult res = producer.send(msg);
			System.out.println(res);
			Thread.sleep(1000);
		}

		producer.shutdown();
	}
}
