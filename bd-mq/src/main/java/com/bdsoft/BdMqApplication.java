package com.bdsoft;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BdMqApplication {

    static Random random = new Random(System.currentTimeMillis());

    static String NAMESVR_ADDR = "192.168.99.42:9876";
    static String topic = "wx_template-dev";
    static String tag = "wx";

    public static void main(String[] args) throws Exception {
//		SpringApplication.run(BdMqApplication.class, args);

        // 生产者
        DefaultMQProducer producer = new DefaultMQProducer("WxMessageProducerGroup-dev");
        producer.setNamesrvAddr(NAMESVR_ADDR);
        producer.start();

        // 发送消息
        for (int i = 0; i < 1; i++) {
//            String txt = genMiniMsg();
            String txt = genOpenMsg();
            Message msg = new Message(topic, tag, txt.getBytes("UTF-8"));

            SendResult res = producer.send(msg);
            System.out.println(res);
            Thread.sleep(1000);
        }

        producer.shutdown();
    }

    static String genOpenMsg(){
        JSONObject json = new JSONObject();
        // 固定参数：phone，msg_id
        json.put("phone", "13426479431");
        json.put("msg_id", 9);
        // 业务参数 nickname,code,remark
        json.put("title", "恭喜你，拼团成功了。");
        json.put("item_name", "拼吧2018");
        json.put("creator_name", "花生好车（老丁)");
        json.put("real_user", "5");
        json.put("remark", "优惠券正在发放中，请在（关于我们-个人中心-卡券）中查看。");
        return json.toJSONString();
    }

    // 生产消息
    // 小程序：https://developers.weixin.qq.com/miniprogram/dev/api/notice.html#%E5%8F%91%E9%80%81%E6%A8%A1%E6%9D%BF%E6%B6%88%E6%81%AF
    static String genMiniMsg() {
        JSONObject json = new JSONObject();
        // 固定参数：phone，msg_id
        json.put("phone", "18811311641");
        json.put("formid", "1531386528196"); // 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id

        // 业务参数 nickname,code,remark
//        json.put("msg_id", 6); // 消息模板
//        json.put("nickname", "小不点");
//        json.put("code", "兑奖码：XXXXXXXXXX");
//        json.put("remark", "您的开奖日期是2018年07月18日16:00。祝您好运连连，中大奖。");

        // 业务参数 openid,formid,payAmount,code,payTime,orderId,title,remark
        json.put("msg_id", 7); // 消息模板
        json.put("payAmount", "10元");
        json.put("code", "兑奖码：XXXXXXXXXX");
        json.put("payTime", "8:36");
        json.put("orderId", "hd4389432324");
        json.put("title", "10元购新车活动  第二期");
        json.put("remark", "您的开奖日期是2018年07月18日16:00。祝您好运连连，中大奖。");

        return json.toJSONString();
    }

    static void consume() throws Exception {
        // 消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("WxMessageConsumerGroup-dev");
        consumer.setNamesrvAddr(NAMESVR_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe(topic, "*");
        // 监听消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                msgs.forEach(msg -> {
                    try {
                        String txt = new String(msg.getBody(), "utf-8");
                        String info = new StringBuilder(Thread.currentThread().getName())
                                .append(" got msg: ").append(txt)
                                .append(", detail： ").append(msg).toString();
                        System.out.println(info);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
//        consumer.start();
    }
}
