package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.util.Random;

public class GameRunnable implements Runnable {
    private volatile boolean running;
    private SnakeGameFrame frame;
    private SnakeGameModel model;
    private Random random;

    public GameRunnable(SnakeGameFrame frame, SnakeGameModel model) {
        this.frame = frame;
        this.model = model;
        this.running = true;
        this.random = new Random();
    }

    public void run() {
        while (running) {
            if (model.isGameActive()) {
                repaint();
            }
            sleep(100L);
        }
    }

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {

        }
    }

    private void repaint() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.repaintGridPanel();
            }
        });
    }
}
