package ru.dobrovolskyn.snake.game.controller;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseButtonActionListener implements ActionListener {
    private SnakeGameFrame frame;
    private SnakeGameModel model;

    public PauseButtonActionListener(SnakeGameFrame frame, SnakeGameModel model) {
        this.frame = frame;
        this.model = model;
    }

    public void actionPerformed(ActionEvent event) {
        JToggleButton button = (JToggleButton) event.getSource();
        if (button.isSelected()) {
            model.setGameActive(false);

            frame.getControlPanel().getStartButton().setEnabled(false);
            button.setEnabled(true);
            frame.getControlPanel().getStopButton().setEnabled(true);
        } else {
            model.setGameActive(true);

            frame.getControlPanel().getStartButton().setEnabled(false);
            frame.getControlPanel().getStopButton().setEnabled(true);
        }
    }

}