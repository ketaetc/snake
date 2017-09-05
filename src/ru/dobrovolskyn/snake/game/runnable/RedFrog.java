package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.enums.FrogsMoveTime;

import java.awt.*;

public class RedFrog extends Frog {
    private static int POINTS = 2;

    public RedFrog(Point location) {
        super(location, Color.RED, POINTS, FrogsMoveTime.RED.getMoveTime());
    }

    @Override
    public void run() {
        super.run();
    }
}
