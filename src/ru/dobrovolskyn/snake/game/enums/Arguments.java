package ru.dobrovolskyn.snake.game.enums;

public enum Arguments {
    GAME_BOARD_HEIGHT("-height"),
    GAME_BOARD_WIDTH("-width"),
    SNAKE_LENGTH("-length"),
    FROGS_COUNT("-frogs");

    private String name;

    private Arguments(String name) {
        this.name = name;
    }

    public String getArgument() {
        return name;
    }
}
