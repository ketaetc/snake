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
//        long sleepTime = model.getSleepTime();
        long sleepTime = 100L;
        while (running) {
            if (model.isGameActive()) {
//                Snake snake = model.getSnake();
//                List<Frog> frogList = model.getFrogList();
//                snake.move();
//
//                if (snake.isSnakeDead()) {
//                    makeGameOver();
//                }
//
//                if (model.isGameStopped()) {
//                    model.setGameActive(false);
//                }
//
//                for (int i = 0; i < frogList.size(); i++) {
//                    int points = frogList.get(i).frogEaten(snake.getSnakeHeadLocation());
//
//                    model.addScore(points);
//                    setScoreText();
//
//                    double chance = random.nextDouble();
//                    Frog frog;
//                    if (points > 0) {
//                        frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
//
//                        frogList.remove(i);
//                        frogList.add(i, frog);
//
//                        if (points == 1) {
//                            snake.addSnakeTail(true);
//                        } else {
//                            snake.addSnakeTail(false);
//                        }
//                    } else if (points < 0) {
//                        makeGameOver();
//                    }
//                }
                repaint();
            }

            sleep(sleepTime);
        }
    }

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {

        }
    }

    private void setScoreText() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setScoreText();
            }
        });
    }

    private void repaint() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.repaintGridPanel();
            }
        });
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    private void makeGameOver() {
        model.setGameOver(true);
        model.setGameActive(false);

        frame.getControlPanel().getStartButton().setEnabled(true);
        frame.getControlPanel().getPauseButton().setEnabled(false);
        frame.getControlPanel().getStopButton().setEnabled(false);
    }
}