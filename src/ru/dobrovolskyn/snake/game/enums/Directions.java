package ru.dobrovolskyn.snake.game.enums;

import java.awt.*;

public enum Directions {
    UP(new Point(0, -1), "UP", "W"),
    DOWN(new Point(0, 1), "DOWN", "S"),
    LEFT(new Point(-1, 0), "LEFT", "A"),
    RIGHT(new Point(1, 0), "RIGHT", "D");

    private Point direction;
    private String name;
    private String character;

    Directions(Point direction, String name, String character) {
        this.direction = direction;
        this.name = name;
        this.character = character;
    }

    public Point getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }
}
