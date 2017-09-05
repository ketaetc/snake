package ru.dobrovolskyn.snake.game.controller;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartButtonActionListener implements ActionListener {
    private SnakeGameFrame frame;
    private SnakeGameModel model;

    public StartButtonActionListener(SnakeGameFrame frame, SnakeGameModel model) {
        this.frame = frame;
        this.model = model;
    }

    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        if (button.getModel().isArmed()) {
            model.setGameActive(false);

            frame.getGridPanel().repaint();

            frame.getControlPanel().getStartButton().setEnabled(false);
            frame.getControlPanel().getPauseButton().setEnabled(true);
            frame.getControlPanel().getStopButton().setEnabled(true);
        } else {
            frame.getControlPanel().getStartButton().setEnabled(true);
            frame.getControlPanel().getPauseButton().setEnabled(false);
            frame.getControlPanel().getStopButton().setEnabled(false);
        }

        new Thread(new WaitToStartRunnable(frame, model)).start();
    }

    private class WaitToStartRunnable implements Runnable {
        private long sleepAmount = 500L;

        private SnakeGameFrame frame;

        private SnakeGameModel model;

        public WaitToStartRunnable(SnakeGameFrame frame, SnakeGameModel model) {
            this.frame = frame;
            this.model = model;
        }

        public void run() {
            model.setGameOver(false);
            model.setGameActive(false);
            model.setGameStopped(false);
            model.init();
            repaint();
            sleep();
            model.setGameActive(true);
        }

        public void repaint() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    frame.setScoreText();
                    frame.repaintGridPanel();
                }
            });
        }

        public void sleep() {
            try {
                Thread.sleep(sleepAmount);
            } catch (InterruptedException e) {
                assert false;
            }
        }

    }

}