package fontys.messaging.broker.wrapper;

import fontys.messaging.core.messaging.Queue;
import fontys.messaging.core.messaging.gateway.MessageReciever;
import fontys.messaging.core.messaging.gateway.MessageSender;
import fontys.messaging.core.messaging.listener.ObjectMessageListener;
import fontys.messaging.core.model.bank.BankInterestReply;
import fontys.messaging.core.model.bank.BankInterestRequest;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import javax.jms.JMSException;

public class Banks {

        private static String ING = "#{amount} <= 100000 && #{time} <= 10";
        private static String ABN = "#{amount} >= 200000 && #{amount} <= 300000  && #{time} <= 20";
        private static String RABO = "#{amount} <= 250000 && #{time} <= 15";


    private static MessageSender<BankInterestRequest> raboSender;
    private static MessageSender<BankInterestRequest> ingSender;
    private static MessageSender<BankInterestRequest> abnSender;

    private static MessageReciever<BankInterestReply> reciever;

    public Banks() throws JMSException {
        raboSender = new MessageSender<>("tcp://localhost:61616", Queue.RABO_REQUEST);
        ingSender = new MessageSender<>("tcp://localhost:61616", Queue.ING_REQUEST);
        abnSender = new MessageSender<>("tcp://localhost:61616", Queue.ABN_REQUEST);

        reciever = new MessageReciever<>("tcp://localhost:61616", Queue.INTEREST_REPLY);
    }

    public int send(BankInterestRequest request) throws EvaluationException {
        var eval = new Evaluator();
        eval.putVariable("amount", String.valueOf(request.getAmount()));
        eval.putVariable("time", String.valueOf(request.getTime()));

        int amount = 0;
        if (eval.evaluate(RABO).equals("1.0")) {
            raboSender.Send(request);
            amount++;
        }
        if (eval.evaluate(ING).equals("1.0")) {
            ingSender.Send(request);
            amount++;
        }
        if (eval.evaluate(ABN).equals("1.0")) {
            abnSender.Send(request);
            amount++;
        }
        return amount;
    }

    public void onMessage(ObjectMessageListener<BankInterestReply> listener) {
        reciever.onMessage(listener);
    }
}
