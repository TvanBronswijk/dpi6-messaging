package fontys.messaging.core.messaging.gateway;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public abstract class Gateway {

    protected Connection connection;
    protected Session session;
    protected Destination destination;

    public Gateway(String brokerUrl, String queue) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        connectionFactory.setTrustAllPackages(true);

        connection = connectionFactory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        destination = session.createQueue(String.format("Bank.%s", queue));
    }
}
