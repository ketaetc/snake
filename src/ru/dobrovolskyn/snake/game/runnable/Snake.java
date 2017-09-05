package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.enums.Rotations;
import ru.dobrovolskyn.snake.game.model.Segment;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

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

    @Override
    public void move() {
        Segment segment = null;

        for (int i = getSnakeLength() - 2; i >= 0; i--) {
            Segment segment2 = snakeCells.get(i + 1);
            segment = snakeCells.get(i);
            Point previousDirection = segment.getDirection();
            segment2.setDirection(previousDirection);

            Point location = makeTransfer(segment2.getLocation(), previousDirection);

            segment2.setLocation(location);
        }

        segment.setDirection(snakeDirection);

        Point location = makeTransfer(segment.getLocation(), snakeDirection);

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
            int x = random.nextInt(SnakeGameModel.getCellWidth());
            int y = random.nextInt(SnakeGameModel.getCellHeight());
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
        for (int i = 1; i < (getSnakeLength() - 1); i++) {
            Point s = snakeCells.get(i).getLocation();
            if (s.equals(getSnakeHeadLocation())) {
                return true;
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

    public Point rotate(Rotations rotation) {
        if (rotation == Rotations.LEFT) {
            int x = (int) this.snakeDirection.getY();
            int y = (int) this.snakeDirection.getX() * -1;
            this.snakeDirection = new Point(x, y);
        }
        if (rotation == Rotations.RIGHT) {
            int x = (int) this.snakeDirection.getY() * -1;
            int y = (int) this.snakeDirection.getX();
            this.snakeDirection = new Point(x, y);
        }
        return this.snakeDirection;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            if (model.isGameActive()) {
                move();

                if (this.isSnakeDead()) {
                    makeStop();
                    model.setGameOver(true);
                }

                if (model.isGameStopped()) {
                    model.setGameActive(false);
                    makeStop();
                }
            }

            sleep(SnakeGame.getSnakeSleep());
        }
    }

    @Override
    public void makeStop() {
        running = false;

        frame.getControlPanel().getStartButton().setEnabled(true);
        frame.getControlPanel().getPauseButton().setEnabled(false);
        frame.getControlPanel().getStopButton().setEnabled(false);
    }

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {

        }
    }
}
