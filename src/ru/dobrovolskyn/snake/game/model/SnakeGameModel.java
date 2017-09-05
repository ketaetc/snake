package ru.dobrovolskyn.snake.game.model;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.runnable.Frog;
import ru.dobrovolskyn.snake.game.runnable.Snake;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class SnakeGameModel {
    private static final int SQUARE_WIDTH = 32;
    private static int CELL_WIDTH;
    private static int CELL_HEIGHT;
    private static int SNAKE_LENGTH;
    private static int FROGS_COUNT;
    private static long SNAKE_SLEEP;

    private volatile boolean firstTimeSwitch;
    private volatile boolean gameActive;
    private volatile boolean gameOver;
    private volatile boolean gameStopped;
    private volatile int score;

    private java.util.List<Frog> frogList = new ArrayList<Frog>();
    private Map<Frog, Future<?>> frogsMap = new ConcurrentHashMap<Frog, Future<?>>();
    private volatile Snake snake;
    private Random random;

    public SnakeGameModel() {
        this.score = 0;
        this.firstTimeSwitch = false;
        this.gameActive = false;
        this.gameOver = false;
        this.random = new Random();
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
            snake.setRunning(false);

            try {
                SnakeGame.getSnakeFuture().cancel(true);
            } catch (Exception e){
                e.printStackTrace();
            }

            snake = SnakeGame.createSnake(SNAKE_LENGTH);
            snake.setName(SnakeGame.getSnakeThreadsCounter().addAndGet(1));
            setSnake(snake);
            SnakeGame.getPool().submit(snake);

            deactivateFrogs();
            cancelFrogsThreads();
            clearFrogsMap();

            for (int i =0; i < SnakeGame.getFrogsCount(); i++) {
                double chance = random.nextDouble();
                Point point = snake.getRandomNonSnakeLocation();
                Frog frog = Frog.createRandomFrog(point, chance);
                frog.setName(SnakeGame.getFrogsThreadsCounter().addAndGet(1));
                addFrog(frog, SnakeGame.getPool().submit(frog));
            }
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
        if (gameOver) {
            setGameActive(false);
        }
        this.gameOver = gameOver;
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

    public void addScore(int score) {
        this.score += score;
    }

    public long getSleepTime() {
        return SNAKE_SLEEP;
    }

    public Dimension getPreferredSize() {
        int width = SQUARE_WIDTH * CELL_WIDTH;
        int height = SQUARE_WIDTH * CELL_HEIGHT;

        return new Dimension(width, height);
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void addFrog(Frog frog, Future<?> future) {
        this.frogsMap.put(frog, future);
    }

    public void removeFrog(Frog frog) {
        this.frogsMap.remove(frog);
    }

    public Map<Frog, Future<?>> getFrogsMap() {
        return frogsMap;
    }

    public void clearFrogsMap() {
        frogsMap.clear();
    }

    public static void setSnakeSleep(long snakeSleep) {
        SNAKE_SLEEP = snakeSleep;
    }

    public static long getSnakeSleep() {
        return SNAKE_SLEEP;
    }

    private void deactivateFrogs() {
        for (Frog frog : getFrogsMap().keySet()) {
            frog.setRunning(false);
        }
    }

    public void cancelFrogsThreads() {
        for (Future<?> future : getFrogsMap().values()) {
            try {
                future.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
