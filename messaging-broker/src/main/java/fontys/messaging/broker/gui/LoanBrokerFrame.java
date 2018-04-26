package fontys.messaging.broker.gui;

import fontys.messaging.core.messaging.Queue;
import fontys.messaging.core.messaging.gateway.MessageReciever;
import fontys.messaging.core.messaging.gateway.MessageSender;
import fontys.messaging.core.messaging.listener.ObjectMessageListener;
import fontys.messaging.core.messaging.requestreply.RequestReply;
import fontys.messaging.core.model.bank.BankInterestReply;
import fontys.messaging.core.model.bank.BankInterestRequest;
import fontys.messaging.core.model.loan.LoanReply;
import fontys.messaging.core.model.loan.LoanRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class LoanBrokerFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<>();
    private JList<JListLine> list;

    private static MessageSender<BankInterestRequest> bankSender;
    private static MessageReciever<BankInterestReply> bankReciever;

    private static MessageReciever<LoanRequest> clientReciever;
    private static MessageSender<LoanReply> clientSender;



    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {
        //JMS Init
        try {
            bankSender = new MessageSender<>("tcp://localhost:61616", Queue.INTEREST_REQUEST);
            bankReciever = new MessageReciever<>("tcp://localhost:61616", Queue.INTEREST_REPLY);

            clientReciever = new MessageReciever<>("tcp://localhost:61616", Queue.LOAN_REQUEST);
            clientSender = new MessageSender<>("tcp://localhost:61616", Queue.LOAN_REPLY);

            clientReciever.onMessage(new ObjectMessageListener<>() {
                @Override
                public void onMessage(Message message) {
                    var clientRequest = parse(message);
                    add(clientRequest);
                    System.out.println("Message Recieved [Client]");

                    var bankRequest = new BankInterestRequest(clientRequest);
                    bankSender.Send(bankRequest);
                    add(clientRequest, bankRequest);
                    System.out.println("Message Sent [Bank]");
                }
            });

            bankReciever.onMessage(new ObjectMessageListener<>() {
                @Override
                public void onMessage(Message message) {
                    var bankReply = parse(message);
                    System.out.println("Message Recieved [Bank]");
                    var clientReply = new LoanReply(bankReply);
                    clientSender.Send(clientReply);
                    System.out.println("Message Sent [Client]");
                    add(bankReply.getRequest().getBaseRequest(), bankReply);

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

        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest().equals(request)) {
                return rr;
            }
        }

        return null;
    }

    public void add(LoanRequest loanRequest) {
        listModel.addElement(new JListLine(loanRequest));
    }


    public void add(LoanRequest loanRequest, BankInterestRequest bankRequest) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankRequest != null) {
            rr.setBankRequest(bankRequest);
            list.repaint();
        }
    }

    public void add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.setBankReply(bankReply);
            ;
            list.repaint();
        }
    }


}
