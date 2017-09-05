package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class GreenFrog extends Frog {
    private static int POINTS = 1;

    public GreenFrog(Point location) {
        super(location, Color.GREEN, POINTS);
    }

    @Override
    public void run() {
        super.run();
    }
}
