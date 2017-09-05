package ru.dobrovolskyn.snake.game.model;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Frog extends GameObject {
    private Point location;
    private Color color;
    private int points;
    private Random random;
    private volatile boolean running;
    private SnakeGameFrame frame;
    private SnakeGameModel model;
    private volatile String name;

    public Frog(int x, int y, Color color, int points) {
        this.running = true;
        setLocation(x, y);
        this.color = color;
        this.points = points;
        this.random = new Random();
        this.model = SnakeGame.getSnakeGameModel();
        this.frame = SnakeGame.getSnakeGameFrame();
    }

    public Frog(Point location, Color color, int points) {
        this.running = true;
        this.location = location;
        this.color = color;
        this.points = points;
        this.random = new Random();
        this.model = SnakeGame.getSnakeGameModel();
        this.frame = SnakeGame.getSnakeGameFrame();
    }

    public void setName(int num) {
        this.name = "Frog:" + num;
    }

    public String getName() {
        return name;
    }

    public static Frog createRandomFrog(Point randomNonSnakeLocation, double chance) {
        if (chance >= 0.8) {
            return new RedFrog(randomNonSnakeLocation);
        } else if (chance <= 0.03) {
            return new BlueFrog(randomNonSnakeLocation);
        } else {
            return new GreenFrog(randomNonSnakeLocation);
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        this.location = new Point(x, y);
    }

    public int frogEaten(Point p) {
        if (p.equals(location)) {
            return points;
        } else {
            return 0;
        }
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void run() {
        System.out.println("running:    " + getName());
        long sleepTime = model.getSleepTime();
        while (running) {
            if (model.isGameActive()) {
                int points = this.frogEaten(model.getSnake().getSnakeHeadLocation());

                model.addScore(points);
                setScoreText();

                double chance = random.nextDouble();
                if (points > 0) {
                    Frog frog = createRandomFrog(model.getSnake().getRandomNonSnakeLocation(), chance);
                    frog.setName(SnakeGame.getFrogsThreadsCounter().addAndGet(1));

                    setRunning(false);

                    model.removeFrog(this);
                    repaint();
                    model.addFrog(frog, frog.getName());
                    SnakeGame.getPool().execute(frog);

                    if (points == 1) {
                        model.getSnake().addSnakeTail(true);
                    } else {
                        model.getSnake().addSnakeTail(false);
                    }
                } else if (points < 0) {
                    makeGameOver();
                }
//                repaint();
            }

            sleep(sleepTime);
        }
    }

    private void setScoreText() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setScoreText();
            }
        });
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    private void makeGameOver() {
        running = false;

        model.setGameOver(true);
        model.setGameActive(false);

        frame.getControlPanel().getStartButton().setEnabled(true);
        frame.getControlPanel().getPauseButton().setEnabled(false);
        frame.getControlPanel().getStopButton().setEnabled(false);
    }

    @Override
    public String toString() {
        return "Frog:   " + this.name
                + "[ location: " + location + ","
                + " color: " + color + ","
                + " points: " + points + ","
                + " running: " + running
                + "]";
    }
}
