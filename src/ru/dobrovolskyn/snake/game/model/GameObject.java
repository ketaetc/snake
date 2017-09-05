package ru.dobrovolskyn.snake.game.model;

public abstract class GameObject implements Runnable {
    private String name;

    public abstract void run();

    public abstract void setName(int num);

    public abstract String getName();
}
