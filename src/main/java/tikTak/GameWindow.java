package tikTak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 555; //высота
    private static final int WINDOW_WIDTH = 507; // ширина
    private static final int WINDOW_POSX = 400; // позиция на экране по оси Х
    private static final int WINDOW_POSY = 300; // позиция на экране по оси Y

    // создвание кнопок
    JButton btnStart = new JButton("New Game");
    JButton btnExit = new JButton("Exit");
    Map map; // Лучше бы дать приватный доступ
    SettingWindow settings; // аналогично, лучше приватный доступ

    public GameWindow() { // Дописал публичный доступ к методу
        // конструктор создающий основное  окно
        setDefaultCloseOperation(EXIT_ON_CLOSE); // определяет действие по нажатию на крестик
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("TicTakToe"); // добавляет название
        setResizable(false); // запрет на изменение размеров

        map = new Map(); // создание поля для игры


        /*
        обьявление окна настроек
        this заставляет окно настроек дать ссылку само на себя же
         */
        settings = new SettingWindow(this);


        // добавление слушателей кнопок
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);// закрытие по нажанию exit
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);// открытие окна настроек по клику New game
            }
        });

        settings.setVisible(true);

        // В компановщикем BorderLayout размещаем панель с 2-мя кнопками
        JPanel panBotton = new JPanel(new GridLayout(1, 2)); // создание панели для кнопок (первая, вторая)
        // добавление кнопок в панель
        panBotton.add(btnStart);
        panBotton.add(btnExit);
        add(panBotton, BorderLayout.SOUTH); // добавление панели с кнопками в BorderLayout
        add(map); // добавление поля в окно приложения

        setVisible(true); // делает окно видимым
    }

    // метод начала новой игры
    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        map.satrtNewGame(mode, fSzX, fSzY, wLen);
    }
}
