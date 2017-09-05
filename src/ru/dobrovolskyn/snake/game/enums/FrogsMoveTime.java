package ru.dobrovolskyn.snake.game.enums;

import ru.dobrovolskyn.snake.game.SnakeGame;

public enum FrogsMoveTime {
    RED(SnakeGame.getSnakeSleep() * 3),
    GREEN(SnakeGame.getSnakeSleep() * 4),
    BLUE(SnakeGame.getSnakeSleep() * 2);

    private long moveTime;

    FrogsMoveTime(long moveTime) {
        this.moveTime = moveTime;
    }

    public long getMoveTime() {
        return moveTime;
    }
}
