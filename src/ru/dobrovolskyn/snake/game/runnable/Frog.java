package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Frog extends GameObject {
    private volatile Point location;
    private Color color;
    private int points;
    private Random random;
    private volatile boolean running;
    private SnakeGameFrame frame;
    private SnakeGameModel model;
    private volatile String name;
    private long moveTime;

    public Frog(int x, int y, Color color, int points, long moveTime) {
        this.running = true;
        setLocation(x, y);
        this.color = color;
        this.points = points;
        this.random = new Random();
        this.model = SnakeGame.getSnakeGameModel();
        this.frame = SnakeGame.getSnakeGameFrame();
        this.moveTime = moveTime;
    }

    public Frog(Point location, Color color, int points, long moveTime) {
        this.running = true;
        this.location = location;
        this.color = color;
        this.points = points;
        this.random = new Random();
        this.model = SnakeGame.getSnakeGameModel();
        this.frame = SnakeGame.getSnakeGameFrame();
        this.moveTime = moveTime;
    }

    public void setName(int num) {
        this.name = "Frog:" + num;
    }

    public String getName() {
        return name;
    }

    @Override
    public void move() {
        double randomValue = random.nextDouble();
        int generationCounter = 0;
        boolean interrupted = false;

        Point direction = generateNewPoint(randomValue);
        int x = location.x + direction.x;
        int y = location.y + direction.y;
        Point newLocation = new Point(x, y);

        while (checkCollision(newLocation)) {
            if (generationCounter >= 10) {
                interrupted = true;
                break;
            }
            randomValue = random.nextDouble();

            direction = generateNewPoint(randomValue);
            x = location.x + direction.x;
            y = location.y + direction.y;
            newLocation = new Point(x, y);

            generationCounter++;
        }

        if (!interrupted) {
            newLocation = makeTransfer(location, direction);

            setLocation(newLocation);
        }
    }

    private Point generateNewPoint(double randomValue) {
        int x = 0;
        int y = 0;

        int delta = random.nextInt(2) - 1;
        if (delta == 0) {
            delta = random.nextInt(2) - 1;
        }
        if (randomValue >= 0.5) {
            x = delta;
        } else {
            y = delta;
        }

        return new Point(x, y);
    }

    private boolean checkFrogsCollision(Point newLocation) {
        for (Frog frog : model.getFrogsMap().keySet()) {
            if (frog.location.equals(newLocation)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkCollision(Point newLocation) {
        return (model.getSnake().isSnakeLocation(newLocation) || checkFrogsCollision(newLocation));
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
        long sleepTime = model.getSleepTime();
        long startTime = 0;
        long endTime;
        while (running) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

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
//                    repaint();
//                    model.addFrog(frog, frog.getName());
                    model.addFrog(frog, SnakeGame.getPool().submit(frog));
//                    SnakeGame.getPool().submit(frog);

                    if (points == 1) {
                        model.getSnake().addSnakeTail(true);
                    } else {
                        model.getSnake().addSnakeTail(false);
                    }
                } else if (points < 0) {
                    makeGameStopped();
                    model.getSnake().setRunning(false);
                    model.setGameOver(true);
                }

                endTime = System.currentTimeMillis();

                if ((endTime - startTime) >= moveTime) {
                    move();
                    startTime = 0;
                }
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

    private void makeGameStopped() {
        running = false;

//        model.setGameOver(true);
//        model.setGameActive(false);

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
