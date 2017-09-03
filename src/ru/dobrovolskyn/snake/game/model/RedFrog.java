package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class RedFrog extends Frog {
    private static int POINTS = 2;

    public RedFrog(int x, int y) {
        super(x, y, Color.RED, POINTS);
    }

    public RedFrog(Point location) {
        super(location, Color.RED, POINTS);
    }
}
