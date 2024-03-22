package tikTak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

// Игровое поле
public class Map extends JPanel {
    private int panelWidth, panelHeight, cellHeight, cellWidth; // высота ширина панели, высота ширина ячейки поля
    private static final Random RANDOM = new Random(); // Для выбора случайной ячейки ИИ
    private final int HUMAN_DOT = 1; // число для игрока
    private final int AI_DOT = 2; // число для ИИ
    private final int EMPTY_DOT = 0; // пустая ячейка
    private int fieldSizeY; // колличество ячеек по Х
    private int fieldSizeX; // колличество ячеек по Y
    private char[][] field; // двумерный массив игрового поля, вставляются читла 1, 2, 0 (HUMAN_DOT, AI_DOT, EMPTY_DOT)
    private static final int DOT_PADDING = 10; // Отступ от края ячейки для фигуры
    private int gameOverType;
    // статусы игры
    private static final int STATE_GAME = 0; // игра идет
    private static final int STATE_WIN_HUMAN = 1; // победил
    private static final int STATE_WIN_AI = 2; // победил комп

    private static final String MSG_WIN_HUMAN = "Победил игрок!";
    private static final String MSG_WIN_AI = "Пробедил компьютер!";
    private static final String MSG_DRAW = "Ничья";
    private boolean isGameOver; // завершина ли игра
    private boolean isInitialized; // инициализированно ли поле, или еще идет ли игра
    private int winLen;

    // инициализация игрового поля
    private void initMap() {
        field = new char[fieldSizeY][fieldSizeX];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }

    public Map() {
        // переопределение для того чтоб отрисовка производилась когда отпускаешь мышку
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // можно добавить if (gameWork(флаг работает ли игра(еще нужна переменная "gameWork")))
                update(e);
            }
        });
        isInitialized = false;
    }

    // метод для проставки символа
    private void update(MouseEvent e) { // метод определения где был щелчек мышью
        if (isGameOver || !isInitialized) return;
        int cellX = e.getX() / cellWidth; // координаты щелчка мыши по Х
        int cellY = e.getY() / cellHeight; // координаты щелчка мыши по Y
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return; // проверка попал ли щелчек в ячейку
        field[cellY][cellX] = HUMAN_DOT; // если все хорошо записываем как ход игрока
        System.out.printf(" x=%d, y=%d\n", cellX, cellY);

        repaint();

        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return; // проверка не привел ли ход к окончанию игры
        aiTurn();
        repaint();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
    }

    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin((char) dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_GAME;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    // Метод начинающий игру с компьютером
    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        this.winLen = wLen;
        this.fieldSizeX = fSzX;
        this.fieldSizeY = fSzY;
        System.out.printf("Mode: %d;\nSize: x=%d, y=%d;\nWin Length: %d\n",
                mode, fSzX, fSzY, wLen);
        initMap();
        isGameOver = false;
        isInitialized = true; // инициализированно ли поле, или еще идет ли игра
        repaint(); // метод перерисовки виджитов(в данном случа поля)
    }

    // метод repaint вызывает этот метод при новой игре
    // необходимо переопределять именно здесь отрисовку, передавая "Graphics g"
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    // Метод для рисования разметки поля
    private void render(Graphics g) {
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        // высоту и ширину делим на 3, получаем 9 ячеек
        cellHeight = panelHeight / fieldSizeX;
        cellWidth = panelWidth / fieldSizeY;

        // рисуем клетки
        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y); // .drawline отрисовывает графику
        }
        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;

                // Цвет и фигура хода игрока
                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    drawCross(g, x, y);
//                    g.fillRect(x * cellWidth + DOT_PADDING + 15, // g.fillOval отвечает за фигуру (g.fill(нужно прописать нужную))
//                            y * cellHeight + DOT_PADDING + 15,
//                            cellWidth - DOT_PADDING * 5,
//                            cellHeight - DOT_PADDING * 5);

                    // Цвет и фигура хода ИИ
                } else if (field[y][x] == AI_DOT) {
                    g.setColor(Color.RED);
                    g.fillOval(x * cellWidth + DOT_PADDING + 15, // g.fillOval отвечает за фигуру
                            y * cellHeight + DOT_PADDING + 15,
                            cellWidth - DOT_PADDING * 5,
                            cellHeight - DOT_PADDING * 5);
                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] +
                            " in cell: x=" + x + " y=" + y);
                }
            }
        }
        if (isGameOver) showMessageGameOver(g);
    }

    // Метот рисует крестик
    private void drawCross(Graphics g, int x, int y) {
        g.drawLine(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                (x + 1) * cellWidth - DOT_PADDING, (y + 1) * cellHeight - DOT_PADDING);
        g.drawLine(x * cellWidth + DOT_PADDING, (y + 1) * cellHeight - DOT_PADDING,
                (x + 1) * cellWidth - DOT_PADDING, y * cellHeight + DOT_PADDING);
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.ORANGE);

        if (gameOverType == STATE_WIN_AI) {
            g.setFont(new Font("Times new roman", Font.BOLD, 48));
        } else {
            g.setFont(new Font("Times new roman", Font.BOLD, 48));
        }

        //
        switch (gameOverType) {
            case STATE_GAME:
                g.drawString(MSG_DRAW, 170, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 3, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 80, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state " + gameOverType);
        }
    }

    // Проверка на НЕ пустоту в ячейке по клику
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    // Проверка на пустоту в ячейке по клику
    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    // Простая логика ИИ
    private void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean checkWin(char dot) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLen, dot)) return true;
                if (checkLine(i, j, 1, 1, winLen, dot)) return true;
                if (checkLine(i, j, 0, 1, winLen, dot)) return true;
                if (checkLine(i, j, 1, -1, winLen, dot)) return true;
            }
        }

//        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
//        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
//        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;
//
//        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
//        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
//        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot) return true;
//
//        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
//        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, char dot) {
        final int far_x = x + (len - 1) * vx;
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != dot) return false;
        }
        return true;
    }

    // Проверка на ничью
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false; // если хоть одна ячейка пустая, вернет false
            }
        }
        return true; // если не false возврат true
    }
}
