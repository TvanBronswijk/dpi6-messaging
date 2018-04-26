package fontys.messaging.core.messaging.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

public abstract class ObjectMessageListener<T extends Serializable> implements MessageListener {
    public T parse(Message message){
        try {
            return (T)((ObjectMessage)message).getObject();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }
}
