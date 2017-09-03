package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class BlueFrog extends Frog {
    private static int POINTS = -1;

    public BlueFrog(int x, int y) {
        super(x, y, Color.BLUE, POINTS);
    }

    public BlueFrog(Point location) {
        super(location, Color.BLUE, POINTS);
    }
}
