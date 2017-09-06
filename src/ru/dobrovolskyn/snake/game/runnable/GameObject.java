package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.util.Utils;

import java.awt.*;

public abstract class GameObject implements Runnable {
    private String name;
    private volatile Point direction;

    public abstract void run();

    public abstract void move();

    public abstract String getName();

    public abstract void setName(int num);

    public Point makeTransfer(Point location, Point direction) {
        Point newLocation = location;
        newLocation.x += direction.x;
        if (newLocation.x == Utils.getGameBoardWidth()) {
            newLocation.x = 0;
        }
        if (newLocation.x < 0) {
            newLocation.x = Utils.getGameBoardWidth() - 1;
        }

        newLocation.y += direction.y;
        if (newLocation.y == Utils.getGameBoardHeight()) {
            newLocation.y = 0;
        }
        if (newLocation.y < 0) {
            newLocation.y = Utils.getGameBoardWidth() - 1;
        }

        return newLocation;
    }

    public abstract void makeStop();

    public Point getDirection() {
        return direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }
}
