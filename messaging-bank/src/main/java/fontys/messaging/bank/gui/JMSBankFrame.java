package fontys.messaging.bank.gui;

import fontys.messaging.core.messaging.Queue;
import fontys.messaging.core.messaging.gateway.MessageReciever;
import fontys.messaging.core.messaging.gateway.MessageSender;
import fontys.messaging.core.messaging.listener.ObjectMessageListener;
import fontys.messaging.core.messaging.requestreply.RequestReply;
import fontys.messaging.core.model.bank.BankInterestReply;
import fontys.messaging.core.model.bank.BankInterestRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JMSBankFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfReply;
    private DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>> listModel = new DefaultListModel<>();

    private static MessageSender<BankInterestReply> sender;
    private static MessageReciever<BankInterestRequest> reciever;

    /**
     * Create the frame.
     */
    public JMSBankFrame(String queue) {
        //JMS Init
        try {
            sender = new MessageSender<>("tcp://localhost:61616", Queue.INTEREST_REPLY);
            reciever = new MessageReciever<>("tcp://localhost:61616", queue);

            reciever.onMessage(new ObjectMessageListener<>() {
                @Override
                public void onMessage(Message message) {
                    var request = parse(message);
                    listModel.addElement(new RequestReply<>(request,null));
                    System.out.println("Message Recieved");
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

        setTitle("JMS Bank - ABN AMRO");
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
        gbc_scrollPane.gridwidth = 5;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);
        JList<RequestReply<BankInterestRequest, BankInterestReply>> list = new JList<>(listModel);
        scrollPane.setViewportView(list);
        JLabel lblNewLabel = new JLabel("type reply");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);
        tfReply = new JTextField();
        GridBagConstraints gbc_tfReply = new GridBagConstraints();
        gbc_tfReply.gridwidth = 2;
        gbc_tfReply.insets = new Insets(0, 0, 0, 5);
        gbc_tfReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfReply.gridx = 1;
        gbc_tfReply.gridy = 1;
        contentPane.add(tfReply, gbc_tfReply);
        tfReply.setColumns(10);
        JButton btnSendReply = new JButton("send reply");

        btnSendReply.addActionListener(e -> {
            RequestReply<BankInterestRequest, BankInterestReply> rr = list.getSelectedValue();
            double interest = Double.parseDouble((tfReply.getText()));
            BankInterestReply reply = new BankInterestReply(rr.getRequest(), interest, "ABN AMRO");
            if (rr != null && reply != null) {
                rr.setReply(reply);
                list.repaint();
                sender.Send(reply);
                System.out.println("Message Sent");
            }
        });

        GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
        gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnSendReply.gridx = 4;
        gbc_btnSendReply.gridy = 1;
        contentPane.add(btnSendReply, gbc_btnSendReply);
    }

}
