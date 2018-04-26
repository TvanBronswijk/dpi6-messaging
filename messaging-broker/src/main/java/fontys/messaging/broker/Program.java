package fontys.messaging.broker;

import fontys.messaging.broker.gui.LoanBrokerFrame;

import java.awt.*;

public class Program
{
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoanBrokerFrame frame = new LoanBrokerFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}