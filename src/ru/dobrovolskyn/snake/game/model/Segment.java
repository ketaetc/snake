package ru.dobrovolskyn.snake.game.model;

import java.awt.*;

public class Segment {
    private final Color color;
    private Point direction;
    private volatile Point location;

    public Segment() {
        this.color = Color.YELLOW;
    }

    public Segment(Color color) {
        this.color = color;
    }

    public static Segment copy(Segment segment) {
        Segment segment2 = new Segment(segment.getColor());
        Point direction = segment.getDirection();
        segment2.setDirection(new Point(direction.x, direction.y));
        Point location = segment.getLocation();
        segment2.setLocation(new Point(location.x, location.y));

        return segment2;
    }

    public Point getDirection() {
        return direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Segment [color=");
        builder.append(color);
        builder.append(", direction=");
        builder.append(direction);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }
}
