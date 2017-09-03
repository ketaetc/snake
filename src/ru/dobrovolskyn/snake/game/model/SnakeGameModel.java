package ru.dobrovolskyn.snake.game.model;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private int score;

    private java.util.List<Frog> frogList = new ArrayList<Frog>();
    private Snake snake;
    private Random random;

    public SnakeGameModel() {
        this.score = 0;
        this.firstTimeSwitch = false;
        this.gameActive = false;
        this.gameOver = false;
        this.snake = new Snake(SNAKE_LENGTH);
        this.random = new Random();

        for (int i = 0; i < FROGS_COUNT; i++) {
            double chance = random.nextDouble();
            Frog frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
            this.frogList.add(frog);
        }
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
            snake.createSnake();

            setFrogsLocation();
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
}
