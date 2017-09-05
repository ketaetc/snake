package ru.dobrovolskyn.snake.game.controller;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.model.Frog;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class StopButtonActionListener implements ActionListener {
    private SnakeGameFrame frame;
    private SnakeGameModel model;

    public StopButtonActionListener(SnakeGameFrame frame, SnakeGameModel model) {
        this.frame = frame;
        this.model = model;
    }

    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        if (button.getModel().isArmed()) {
            model.setGameActive(false);
            model.setGameStopped(true);

            model.getSnake().setRunning(false);
            for (Frog frog : model.getFrogsMap().keySet()) {
                frog.setRunning(false);
            }


            frame.getControlPanel().getStartButton().setEnabled(true);
            frame.getControlPanel().getPauseButton().setSelected(false);
            frame.getControlPanel().getPauseButton().setEnabled(false);
            frame.getControlPanel().getStopButton().setEnabled(false);
        }

        new Thread(new StopButtonActionListener.WaitToStartRunnable(frame, model)).start();
    }

    private class WaitToStartRunnable implements Runnable {
        private SnakeGameFrame frame;

        private SnakeGameModel model;

        public WaitToStartRunnable(SnakeGameFrame frame, SnakeGameModel model) {
            this.frame = frame;
            this.model = model;
        }

        public void run() {
            model.setGameStopped(true);
            model.setGameActive(false);
            repaint();
        }

        public void repaint() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    frame.repaintGridPanel();
                }
            });
        }
    }
}