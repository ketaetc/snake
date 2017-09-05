package ru.dobrovolskyn.snake.game.model;

import ru.dobrovolskyn.snake.game.SnakeGame;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SnakeGameModel {
    private static final int SQUARE_WIDTH = 32;
    private static int CELL_WIDTH;
    private static int CELL_HEIGHT;
    private static int SNAKE_LENGTH;
    private static int FROGS_COUNT;
    private final long SLEEP_TIME = 300L;

    private boolean firstTimeSwitch;
    private boolean gameActive;
    private boolean gameOver;
    private boolean gameStopped;
    private volatile int score;

    private java.util.List<Frog> frogList = new ArrayList<Frog>();
    private Map<Frog, String> frogsMap = new ConcurrentHashMap<Frog, String>();
    private volatile Snake snake;
    private Random random;

    public SnakeGameModel() {
        this.score = 0;
        this.firstTimeSwitch = false;
        this.gameActive = false;
        this.gameOver = false;
//        this.snake = new Snake(SNAKE_LENGTH);
//        this.snake = set;
        this.random = new Random();

//        for (int i = 0; i < FROGS_COUNT; i++) {
//            double chance = random.nextDouble();
//            Frog frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
//            this.frogList.add(frog);
//        }
    }

    public static int getSquareWidth() {
        return SQUARE_WIDTH;
    }

    public static int getCellWidth() {
        return CELL_WIDTH;
    }

    public static void setCellWidth(int cellWidth) {
        CELL_WIDTH = cellWidth;
    }

    public static int getCellHeight() {
        return CELL_HEIGHT;
    }

    public static void setCellHeight(int cellHeight) {
        CELL_HEIGHT = cellHeight;
    }

    public static void setSnakeLength(int snakeLength) {
        SNAKE_LENGTH = snakeLength;
    }

    public static void setFrogsCount(int frogsCount) {
        FROGS_COUNT = frogsCount;
    }

    public void init() {
        if (firstTimeSwitch) {
            this.score = 0;
            snake = SnakeGame.createSnakeInstance(SnakeGame.getSnakeLength());
            snake.setName(SnakeGame.getSnakeThreadsCounter().addAndGet(1));
            SnakeGame.getSnakeGameModel().setSnake(snake);
            SnakeGame.getPool().execute(snake);

            for (int i =0; i < SnakeGame.getFrogsCount(); i++) {
                double chance = random.nextDouble();
                Point point = snake.getRandomNonSnakeLocation();
                Frog frog = Frog.createRandomFrog(point, chance);
                frog.setName(SnakeGame.getFrogsThreadsCounter().addAndGet(1));
                SnakeGame.getSnakeGameModel().addFrog(frog, frog.getName());

                SnakeGame.getPool().execute(frog);
            }

//            new Thread(snake, "Snake:" + SnakeGame.getSnakeThreadsCounter().get()).start();
//            snake.createSnake();

//            setFrogsLocation();
            firstTimeSwitch = false;
        } else {
            firstTimeSwitch = true;
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            setGameActive(false);
        }
    }

    public boolean isGameStopped() {
        return gameStopped;
    }

    public void setGameStopped(boolean gameStopped) {
        this.gameStopped = gameStopped;
    }

    public String getFormattedScore() {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(score);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public long getSleepTime() {
        return SLEEP_TIME;
    }

    public List<Frog> getFrogList() {
        return frogList;
    }

    public void setFrogList(List<Frog> frogList) {
        this.frogList = frogList;
    }

    public void setFrogsLocation() {
        for (Frog frog : frogList) {
            frog.setLocation(snake.getRandomNonSnakeLocation());
        }
    }

    public Dimension getPreferredSize() {
        int width = SQUARE_WIDTH * CELL_WIDTH;
        int height = SQUARE_WIDTH * CELL_HEIGHT;

        return new Dimension(width, height);
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void setFrogs() {
        for (int i = 0; i < FROGS_COUNT; i++) {
            double chance = random.nextDouble();
            Frog frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
            this.frogList.add(frog);
        }
    }

    public void addFrog(Frog frog, String name) {
        this.frogsMap.put(frog, name);
    }

    public void removeFrog(Frog frog) {
        this.frogsMap.remove(frog);
    }

    public Map<Frog, String> getFrogsMap() {
        return frogsMap;
    }
}
