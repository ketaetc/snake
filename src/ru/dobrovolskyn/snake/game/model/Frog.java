package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class Frog extends GameObject {
    private Point location;
    private Color color;
    private int points;

    public Frog(int x, int y, Color color, int points) {
        setLocation(x, y);
        this.color = color;
        this.points = points;
    }

    public Frog(Point location, Color color, int points) {
        this.location = location;
        this.color = color;
        this.points = points;
    }

    public static Frog createRandomFrog(Point randomNonSnakeLocation, double chance) {
        if (chance >= 0.8) {
            return new RedFrog(randomNonSnakeLocation);
        } else if (chance <= 0.03) {
            return new BlueFrog(randomNonSnakeLocation);
        } else {
            return new GreenFrog(randomNonSnakeLocation);
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        this.location = new Point(x, y);
    }

    public int frogEaten(Point p) {
        if (p.equals(location)) {
            return points;
        } else {
            return 0;
        }
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void run() {
        super.run();


    }
}
