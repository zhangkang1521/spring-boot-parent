package org.zk;

import static org.junit.Assert.assertTrue;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;

public class ConsumerTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void receive() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consume");
        consumer.setNamesrvAddr("192.168.127.128:9876");
        consumer.subscribe("TopicTest", "push");
        consumer.registerMessageListener(new MyMessageListener());
        consumer.start();
        System.in.read();
    }
}
