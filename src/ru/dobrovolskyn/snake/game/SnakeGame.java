package ru.dobrovolskyn.snake.game;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.runnable.Frog;
import ru.dobrovolskyn.snake.game.runnable.Snake;
import ru.dobrovolskyn.snake.game.util.Utils;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SnakeGame {
    private static volatile SnakeGameFrame snakeGameFrame;
    private static volatile SnakeGameModel snakeGameModel;

    private static volatile AtomicInteger snakeThreadsCounter = new AtomicInteger(0);
    private static volatile AtomicInteger frogsThreadsCounter = new AtomicInteger(0);

    private static volatile ExecutorService pool;
    private static Random random;
    private static volatile Object lock = new Object();


    public static void main(String[] args) throws Exception {
        if (Utils.parseArguments(args)) {
            random = new Random();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    snakeGameModel = new SnakeGameModel();
                    snakeGameFrame = new SnakeGameFrame();
                    Snake snake = createSnake(Utils.getSnakeLength());
                    snakeThreadsCounter.addAndGet(1);
                    snake.setName(snakeThreadsCounter.get());

                    snakeGameModel.setSnake(snake);

                    pool = Executors.newFixedThreadPool(Utils.getFrogsCount() + 1);
                    Utils.setSnakeFuture(pool.submit(snake));

                    for (int i = 0; i < Utils.getFrogsCount(); i++) {
                        double chance = random.nextDouble();
                        Frog frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
                        frog.setName(frogsThreadsCounter.addAndGet(1));
                        snakeGameModel.addFrog(frog, pool.submit(frog));
                    }
                }
            });
        }


    }

    public static Snake createSnake(int snakeLength) {
        return new Snake(snakeLength);
    }

    public static SnakeGameFrame getSnakeGameFrame() {
        return snakeGameFrame;
    }

    public static SnakeGameModel getSnakeGameModel() {
        return snakeGameModel;
    }

    public static AtomicInteger getSnakeThreadsCounter() {
        return snakeThreadsCounter;
    }

    public static AtomicInteger getFrogsThreadsCounter() {
        return frogsThreadsCounter;
    }

    public static ExecutorService getPool() {
        synchronized (lock) {
            return pool;
        }
    }
}
