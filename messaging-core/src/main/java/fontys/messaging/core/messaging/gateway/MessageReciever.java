package fontys.messaging.core.messaging.gateway;

import fontys.messaging.core.messaging.listener.ObjectMessageListener;

import javax.jms.*;
import java.io.Serializable;

public class MessageReciever<T extends Serializable> extends Gateway{

    protected MessageConsumer consumer;

    public MessageReciever(String brokerUrl, String queue) throws JMSException {
        super(brokerUrl, queue);
        consumer = session.createConsumer(destination);
    }

    public void onMessage(ObjectMessageListener<T> listener){
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
