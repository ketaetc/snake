package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class RedFrog extends Frog {
    private static int POINTS = 2;

    public RedFrog(Point location) {
        super(location, Color.RED, POINTS);
    }

    @Override
    public void run() {
        super.run();
    }
}
