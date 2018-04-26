package fontys.messaging.client;

import fontys.messaging.client.gui.LoanClientFrame;

import java.awt.*;

public class Program {

    public static void main(String [] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoanClientFrame frame = new LoanClientFrame();

                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}