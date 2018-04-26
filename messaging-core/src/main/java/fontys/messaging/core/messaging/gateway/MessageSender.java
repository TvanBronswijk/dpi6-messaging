package fontys.messaging.core.messaging.gateway;

import javax.jms.*;
import java.io.Serializable;

public class MessageSender<T extends Serializable> extends Gateway {

    private MessageProducer producer;

    public MessageSender(String brokerUrl, String queue) throws JMSException {
        super(brokerUrl, queue);
        producer = session.createProducer(destination);
    }

    public Message createMessage(T object){
        try {
            var message = session.createObjectMessage(object);
            return message;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void Send(T object){
        try {
            producer.send(createMessage(object));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void Send(Message message){
        try {
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
