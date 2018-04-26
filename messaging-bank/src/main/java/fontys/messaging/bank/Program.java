package fontys.messaging.bank;

import fontys.messaging.bank.gui.JMSBankFrame;

import java.awt.*;

public class Program
{
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JMSBankFrame frame = new JMSBankFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}