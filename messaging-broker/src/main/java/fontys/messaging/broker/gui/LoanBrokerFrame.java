package fontys.messaging.broker.gui;

import fontys.messaging.broker.wrapper.Banks;
import fontys.messaging.core.messaging.Queue;
import fontys.messaging.core.messaging.gateway.MessageReciever;
import fontys.messaging.core.messaging.gateway.MessageSender;
import fontys.messaging.core.messaging.listener.ObjectMessageListener;
import fontys.messaging.core.messaging.requestreply.RequestReply;
import fontys.messaging.core.model.bank.BankInterestReply;
import fontys.messaging.core.model.bank.BankInterestRequest;
import fontys.messaging.core.model.loan.LoanReply;
import fontys.messaging.core.model.loan.LoanRequest;
import net.sourceforge.jeval.EvaluationException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;


public class LoanBrokerFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<>();
    private JList<JListLine> list;

    private static Banks banks;

    private static MessageReciever<LoanRequest> clientReciever;
    private static MessageSender<LoanReply> clientSender;

    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {
        //JMS Init
        try {
            banks = new Banks();

            clientReciever = new MessageReciever<>("tcp://localhost:61616", Queue.LOAN_REQUEST);
            clientSender = new MessageSender<>("tcp://localhost:61616", Queue.LOAN_REPLY);

            clientReciever.onMessage(new ObjectMessageListener<>() {
                @Override
                public void onMessage(Message message) {
                    var clientRequest = parse(message);
                    System.out.println("Message Recieved [Client]");

                    var bankRequest = new BankInterestRequest(clientRequest);
                    int sent = 0;
                    try {
                        sent = banks.send(bankRequest);
                        add(clientRequest, sent);
                        System.out.println("Message Sent [Bank]");
                    } catch (EvaluationException e) {
                        e.printStackTrace();
                    }
                }
            });

            banks.onMessage(new ObjectMessageListener<>() {
                @Override
                public void onMessage(Message message) {
                    var bankReply = parse(message);
                    System.out.println("Message Recieved [Bank]");
                    var line = add(bankReply.getRequest().getBaseRequest(), bankReply);
                    if(line.getBankReplies().size() == line.getRequestsSent()) {
                        var lowest = line.getBankReplies().stream().min(Comparator.comparingDouble(BankInterestReply::getInterest));
                        var clientReply = new LoanReply(lowest.get());
                        clientSender.Send(clientReply);
                        System.out.println("Message Sent [Client]");
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

        setTitle("Loan Broker");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] {46, 31, 86, 30, 89, 0};
        gbl_contentPane.rowHeights = new int[] {233, 23, 0};
        gbl_contentPane.columnWeights = new double[] {1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 7;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        list = new JList<>(listModel);
        scrollPane.setViewportView(list);
    }

    private JListLine getRequestReply(LoanRequest request) {

        for (int i = listModel.getSize() - 1; i >= 0; i--) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest().equals(request)) {
                return rr;
            }
        }
        return null;
    }

    public void add(LoanRequest loanRequest, int requestsSent) {
        listModel.addElement(new JListLine(loanRequest, requestsSent));
    }

    public JListLine add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.addBankReply(bankReply);
            list.repaint();
        }
        return rr;
    }


}
