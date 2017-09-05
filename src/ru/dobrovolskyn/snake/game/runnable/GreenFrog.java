package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.enums.FrogsMoveTime;

import java.awt.*;

public class GreenFrog extends Frog {
    private static int POINTS = 1;

    public GreenFrog(Point location) {
        super(location, Color.GREEN, POINTS, FrogsMoveTime.GREEN.getMoveTime());
    }

    @Override
    public void run() {
        super.run();
    }
}
