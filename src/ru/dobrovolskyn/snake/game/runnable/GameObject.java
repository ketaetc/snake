package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.SnakeGame;

import java.awt.*;

public abstract class GameObject implements Runnable {
    private String name;

    public abstract void run();

    public abstract void move();

    public abstract void setName(int num);

    public abstract String getName();

    public Point makeTransfer(Point location, Point direction) {
        Point newLocation = location;
        newLocation.x += direction.x;
        if (newLocation.x == SnakeGame.getGameBoardWidth()) {
            newLocation.x = 0;
        }
        if (newLocation.x < 0) {
            newLocation.x = SnakeGame.getGameBoardWidth() - 1;
        }

        newLocation.y += direction.y;
        if (newLocation.y == SnakeGame.getGameBoardHeight()) {
            newLocation.y = 0;
        }
        if (newLocation.y < 0) {
            newLocation.y = SnakeGame.getGameBoardWidth() - 1;
        }

        return newLocation;
    }
}
