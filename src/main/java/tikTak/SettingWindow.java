 package tikTak;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//окно параметров игры (пока только начало новой игры)
public class SettingWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 230;
    private static final int WINDOW_WIDTH =350;


    JButton btnStart = new JButton("Start new game");
    SettingWindow(GameWindow gameWindow) {
        setLocationRelativeTo(gameWindow); // расположение обьекта относительно другого обьекта
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(getX() - WINDOW_HEIGHT*2, getY() - WINDOW_WIDTH/3);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // настройка видимости окна при нажатии крестика?
                gameWindow.startNewGame(0, 3, 3, 3);
            }
        });

        add(btnStart); // вызывается у обьекта JFrame, добавляет обьект
    }
}
