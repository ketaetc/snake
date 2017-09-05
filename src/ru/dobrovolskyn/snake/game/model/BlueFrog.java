package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class BlueFrog extends Frog {
    private static int POINTS = -1;

    public BlueFrog(Point location) {
        super(location, Color.BLUE, POINTS);
    }

    @Override
    public void run() {
        super.run();
    }
}
