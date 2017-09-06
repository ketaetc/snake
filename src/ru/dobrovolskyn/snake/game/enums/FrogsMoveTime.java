package ru.dobrovolskyn.snake.game.enums;

import ru.dobrovolskyn.snake.game.util.Utils;

public enum FrogsMoveTime {
    RED(Utils.getSnakeSleep() * 3),
    GREEN(Utils.getSnakeSleep() * 4),
    BLUE(Utils.getSnakeSleep() * 2);

    private long moveTime;

    FrogsMoveTime(long moveTime) {
        this.moveTime = moveTime;
    }

    public long getMoveTime() {
        return moveTime;
    }
}
