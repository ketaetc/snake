package ru.dobrovolskyn.snake.game.runnable;

import ru.dobrovolskyn.snake.game.enums.FrogsMoveTime;

import java.awt.*;

public class BlueFrog extends Frog {
    private static int POINTS = -1;

    public BlueFrog(Point location) {
        super(location, Color.BLUE, POINTS, FrogsMoveTime.BLUE.getMoveTime());
    }

    @Override
    public void run() {
        super.run();
    }
}
