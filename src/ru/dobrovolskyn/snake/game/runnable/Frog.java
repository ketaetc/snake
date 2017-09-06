package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.enums.Directions;
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

    public static Frog createRandomFrog(Point randomNonSnakeLocation, double chance) {
        if (chance >= 0.8) {
            return new RedFrog(randomNonSnakeLocation);
        } else if (chance <= 0.03) {
            return new BlueFrog(randomNonSnakeLocation);
        } else {
            return new GreenFrog(randomNonSnakeLocation);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(int num) {
        this.name = "Frog:" + num;
    }

    @Override
    public void move() {
        double randomValue = random.nextDouble();
        int generationCounter = 0;
        boolean interrupted = false;
        Point newLocation;
        Point direction = getDirection();

        if (getDistanceTo(model.getSnake().getSnakeHeadLocation()) > 4) {
            direction = generateNewPoint(randomValue);
            int x = location.x + direction.x;
            int y = location.y + direction.y;
            newLocation = new Point(x, y);

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
                setDirection(direction);

                setLocation(newLocation);
            }
        } else {
            direction = model.getSnake().getDirection();
            setDirection(direction);
            if (randomValue < 0.8) {
                newLocation = makeTransfer(location, direction);
            } else {
                newLocation = getFarPoint(model.getSnake().getSnakeHeadLocation());
            }
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
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
                    model.cancelFrogThread(this);
                    model.removeFrog(this);
                    model.addFrog(frog, SnakeGame.getPool().submit(frog));

                    if (points == 1) {
                        model.getSnake().addSnakeTail(true);
                    } else {
                        model.getSnake().addSnakeTail(false);
                    }
                } else if (points < 0) {
                    model.setGameOver(true);
                    frame.repaintGridPanel();
                    makeStop();
                    model.getSnake().setRunning(false);
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

    private double getDistanceTo(Point point) {
        double distance = Math.sqrt(Math.pow(this.location.getX() - point.getX(), 2) + Math.pow(this.location.getY() -
                point.getY(), 2));

        return Math.abs(distance);
    }

    private Point getFarPoint(Point point) {
        Point locationNorth = makeTransfer(this.location, Directions.UP.getDirection());
        Point locationSouth = makeTransfer(this.location, Directions.DOWN.getDirection());
        Point locationWest = makeTransfer(this.location, Directions.LEFT.getDirection());
        Point locationEast = makeTransfer(this.location, Directions.RIGHT.getDirection());

        double distanceNorth = Math.sqrt(Math.pow(locationNorth.getX() - point.getX(), 2) + Math.pow(locationNorth.getY() - point.getY(), 2));
        double distanceSouth = Math.sqrt(Math.pow(locationSouth.getX() - point.getX(), 2) + Math.pow(locationSouth.getY() - point.getY(), 2));
        double distanceWest = Math.sqrt(Math.pow(locationWest.getX() - point.getX(), 2) + Math.pow(locationWest.getY() - point.getY(), 2));
        double distanceEast = Math.sqrt(Math.pow(locationEast.getX() - point.getX(), 2) + Math.pow(locationEast.getY() - point.getY(), 2));

        double maxDistance = Math.max(distanceNorth, Math.max(distanceSouth, Math.max(distanceWest, distanceEast)));

        if (maxDistance == distanceNorth) {
            return locationNorth;
        }
        if (maxDistance == distanceSouth) {
            return locationSouth;
        }
        if (maxDistance == distanceWest) {
            return locationWest;
        }
        if (maxDistance == distanceEast) {
            return locationEast;
        }

        return null;
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void makeStop() {
        running = false;

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
