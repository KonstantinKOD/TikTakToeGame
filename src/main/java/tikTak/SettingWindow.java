 package tikTak;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//окно параметров игры (пока только начало новой игры)
public class SettingWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 230;
    private static final int WINDOW_WIDTH =350;
    private static final int WINDOW_POSX = 400;
    private static final int WINDOW_POSY = 300;

    JButton btnStart = new JButton("Start new game");
    SettingWindow(GameWindow gameWindow) {
        setLocationRelativeTo(gameWindow);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame(0, 3, 3, 3);
                setVisible(false);
            }
        });

        add(btnStart);
    }
}
