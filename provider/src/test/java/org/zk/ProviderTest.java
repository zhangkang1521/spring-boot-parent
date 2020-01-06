package org.zk;

import static org.junit.Assert.assertTrue;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ProviderTest
{

    @Test
    public void send() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
        producer.setNamesrvAddr("192.168.127.128:9876");
        producer.start();
        Message message = new Message("TopicTest", "push", "hello2".getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.send(message);
    }
}
