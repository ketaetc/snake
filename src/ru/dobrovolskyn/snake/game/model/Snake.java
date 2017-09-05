package ru.dobrovolskyn.snake.game.model;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.enums.SnakeRotations;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Snake extends GameObject {
    private static int MIN_SNAKE_LENGTH = 2;
    private int snakeLength;
    private List<Segment> snakeCells;
    private Point snakeDirection;
    private Random random;
    private volatile boolean running;
    private SnakeGameFrame frame;
    private SnakeGameModel model;
    private volatile String name;

    public Snake(int snakeLength) {
        this.running = true;
        this.snakeLength = snakeLength;
        this.random = new Random();
        this.snakeCells = new CopyOnWriteArrayList<Segment>();
        this.frame = SnakeGame.getSnakeGameFrame();
        this.model = SnakeGame.getSnakeGameModel();
        createSnake();
    }

    public void setName(int num) {
        this.name = "Snake:" + num;
    }

    public String getName() {
        return name;
    }

    public void createSnake() {
        this.snakeCells.clear();
        this.snakeDirection = new Point(1, 0);

        int x = snakeLength - 1;
        int y = 0;

        Segment head = new Segment();
        head.setLocation(new Point(x, y));
        head.setDirection(snakeDirection);
        snakeCells.add(head);

        for (int i = 0; i < snakeLength - 1; i++) {
            x -= snakeDirection.x;
            y -= snakeDirection.y;
            Segment segment = new Segment();
            segment.setLocation(new Point(x, y));
            segment.setDirection(snakeDirection);
            snakeCells.add(segment);
        }
    }

    public void updatePosition() {
        Segment segment = null;

        for (int i = getSnakeLength() - 2; i >= 0; i--) {
            Segment segment2 = snakeCells.get(i + 1);
            segment = snakeCells.get(i);
            Point previousDirection = segment.getDirection();
            segment2.setDirection(previousDirection);

            Point location = segment2.getLocation();
            location.x += previousDirection.x;
            location.y += previousDirection.y;
            segment2.setLocation(location);
        }

        segment.setDirection(snakeDirection);

        Point location = segment.getLocation();
        location.x += snakeDirection.x;
        location.y += snakeDirection.y;
        segment.setLocation(location);
    }

    public void addSnakeTail(boolean grow) {
        Segment segment = snakeCells.get(getSnakeLength() - 1);

        Point direction = segment.getDirection();
        Point location = segment.getLocation();
        int x = location.x - direction.x;
        int y = location.y - direction.y;

        if (grow) {
            Segment segment2 = Segment.copy(segment);
            segment2.setLocation(new Point(x, y));

            snakeCells.add(segment2);
        } else {
            if (snakeCells.size() - 1 > MIN_SNAKE_LENGTH) {
                snakeCells.remove(snakeCells.size() - 1);
            }
        }
    }

    public List<Segment> getSnakeCells() {
        return snakeCells;
    }

    public int getSnakeLength() {
        return snakeCells.size();
    }

    public Point getSnakeHeadLocation() {
        return snakeCells.get(0).getLocation();
    }

    public void setSnakeDirection(Point snakeDirection) {
        if (!checkUTurn(snakeDirection)) {
            this.snakeDirection = snakeDirection;
        }
    }

    public Point getRandomNonSnakeLocation() {
        Point p;

        do {
            int x = random.nextInt(model.getCellWidth());
            int y = random.nextInt(model.getCellHeight());
            p = new Point(x, y);
        } while (isSnakeLocation(p));

        return p;
    }

    public boolean isSnakeLocation(Point p) {
        for (Segment segment : snakeCells) {
            if (segment.getLocation().equals(p)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSnakeDead() {
        int segmentWidth = model.getCellWidth();
        int segmentHeight = model.getCellHeight();

        for (Segment segment : snakeCells) {
            Point p = segment.getLocation();
            if ((p.x < 0) || (p.x >= segmentWidth)) {
                return true;
            }
            if ((p.y < 0) || (p.y >= segmentHeight)) {
                return true;
            }
        }

        for (int i = 0; i < (getSnakeLength() - 1); i++) {
            Point s = snakeCells.get(i).getLocation();
            for (int j = (i + 1); j < getSnakeLength(); j++) {
                Point t = snakeCells.get(j).getLocation();
                if (s.equals(t)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkUTurn(Point snakeDirection) {
        if ((this.snakeDirection.getX() == (-snakeDirection.getX()))
                || (this.snakeDirection.getY() == (-snakeDirection.getY()))) {
            return true;
        }

        return false;
    }

    public Point rotate(SnakeRotations rotation) {
        if (rotation == SnakeRotations.LEFT) {
            int x = (int) this.snakeDirection.getY();
            int y = (int) this.snakeDirection.getX() * -1;
            this.snakeDirection = new Point(x, y);
        }
        if (rotation == SnakeRotations.RIGHT) {
            int x = (int) this.snakeDirection.getY() * -1;
            int y = (int) this.snakeDirection.getX();
            this.snakeDirection = new Point(x, y);
        }
        return this.snakeDirection;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        System.out.println("running:    " + getName());
        long sleepTime = model.getSleepTime();
        while (running) {
            if (model.isGameActive()) {
                this.updatePosition();

                if (this.isSnakeDead()) {
                    makeGameStopped();
                    model.setGameOver(true);
                }

                if (model.isGameStopped()) {
//                    model.setGameActive(false);
                    makeGameStopped();
                }
//                repaint();
            }

            sleep(sleepTime);
        }
    }

    private void makeGameStopped() {
        running = false;

//        model.setGameOver(true);
        model.setGameActive(false);

        frame.getControlPanel().getStartButton().setEnabled(true);
        frame.getControlPanel().getPauseButton().setEnabled(false);
        frame.getControlPanel().getStopButton().setEnabled(false);
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

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {

        }
    }
}
