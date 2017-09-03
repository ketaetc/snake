package ru.dobrovolskyn.snake.game.controller;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ArrowAction extends AbstractAction {
    private static final long serialVersionUID = 2023424583090620226L;
    private Point direction;
    private SnakeGameModel model;

    public ArrowAction(SnakeGameModel model, Point direction) {
        this.model = model;
        this.direction = direction;
    }

    public void actionPerformed(ActionEvent event) {
        if (model.isGameActive()) {
            model.getSnake().setSnakeDirection(direction);
        }
    }

}
