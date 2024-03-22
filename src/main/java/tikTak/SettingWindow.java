package tikTak;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//окно параметров игры (пока только начало новой игры)
public class SettingWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 330;
    private static final int WINDOW_WIDTH = 350;

    private int gameMode;
    private int fs;
    private int winLen;

    JButton btnStart = new JButton("Start new game");

    SettingWindow(GameWindow gameWindow) {
        setLocationRelativeTo(gameWindow); // расположение обьекта относительно другого обьекта
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(getX() - WINDOW_HEIGHT * 2, getY() - WINDOW_WIDTH / 3);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // настройка видимости окна при нажатии крестика
                gameWindow.startNewGame(gameMode, fs, fs, winLen);
            }
        });
        add(createMainPanel());
        add(btnStart, BorderLayout.SOUTH); // вызывается у обьекта JFrame, добавляет обьект
    }

    public JPanel createModePanel() {
        JPanel jp1 = new JPanel(new GridLayout(3, 1));
        JLabel jl = new JLabel("Выберите режим игры");
        JRadioButton rb1 = new JRadioButton("Человек против компьютера(PvE)");
        JRadioButton rb2 = new JRadioButton("Человек против человека(PvP)");

        ButtonGroup bg = new ButtonGroup();
        rb1.setSelected(true); // Выбор по умолчанию
        rb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode = 0;
            }
        });
        rb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode = 1;
            }
        });
        bg.add(rb1);
        bg.add(rb2);
        jp1.add(jl);
        jp1.add(rb1);
        jp1.add(rb2);
        return jp1;
    }

    public JPanel createFieldPanel() {
        JPanel jp = new JPanel(new GridLayout(3, 1));
        JLabel jl = new JLabel("Выберите размер поля");
        JSlider jSlider = new JSlider(3, 10, 3);
        JLabel jl2 = new JLabel(String.format("Установленный размер поля %d", jSlider.getValue()));

        jSlider.addChangeListener(new ChangeListener() { // Метод преопределяющий значение по движению бегунка
            @Override
            public void stateChanged(ChangeEvent e) {
                jl2.setText(String.format("Установленный размер поля %d", jSlider.getValue()));
                fs = jSlider.getValue();
            }
        });

        jp.add(jl);
        jp.add(jl2);
        jp.add(jSlider);
        return jp;
    }

    public JPanel createVictoryPanel() {
        JPanel jp = new JPanel(new GridLayout(3, 1));
        JLabel jl = new JLabel("Выберите длинну для победы");
        JSlider jslider = new JSlider(3, 10, 3);
        JLabel jl2 = new JLabel(String.format("Установленная длинна %d", jslider.getValue()));

        jslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                winLen = jslider.getValue();
                if (winLen > fs) {
                    winLen = fs;
                    jslider.setValue(winLen);
                }
                jl2.setText(String.format("Установленная длинна %d", jslider.getValue()));
            }
        });

        jp.add(jl);
        jp.add(jl2);
        jp.add(jslider);
        return jp;
    }

    public JPanel createMainPanel() {
        JPanel jp = new JPanel(new GridLayout(3, 1));
        jp.add(createModePanel());
        jp.add(createFieldPanel());
        jp.add(createVictoryPanel());
        return jp;
    }
}
