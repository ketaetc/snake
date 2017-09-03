package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class GreenFrog extends Frog {
    private static int POINTS = 1;

    public GreenFrog(int x, int y) {
        super(x, y, Color.GREEN, POINTS);
    }

    public GreenFrog(Point location) {
        super(location, Color.GREEN, POINTS);
    }
}
