package ru.dobrovolskyn.snake.game.view;

import ru.dobrovolskyn.snake.game.controller.PauseButtonActionListener;
import ru.dobrovolskyn.snake.game.controller.StartButtonActionListener;
import ru.dobrovolskyn.snake.game.controller.StopButtonActionListener;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import javax.swing.*;
import java.awt.*;

public class ControlPanel {
    private static final Insets normalInsets = new Insets(10, 10, 0, 10);
    private JButton startButton;
    private JToggleButton pauseButton;
    private JButton stopButton;
    private JPanel panel;
    private JTextField scoreField;
    private SnakeGameFrame frame;
    private SnakeGameModel model;

    public ControlPanel(SnakeGameFrame frame, SnakeGameModel model) {
        this.frame = frame;
        this.model = model;
        createPartControl();
    }

    private void createPartControl() {
        panel = new JPanel();

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout());

        int gridY = 0;

        JLabel scoreLabel = new JLabel("Score");
        Font labelFont = innerPanel.getFont().deriveFont(32.0F);
        scoreLabel.setFont(labelFont);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        addComponent(innerPanel, scoreLabel, 0, gridY++, 1, 1, normalInsets,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        scoreField = new JTextField(6);
        scoreField.setEditable(false);
        Font textFont = innerPanel.getFont().deriveFont(32.0F);
        scoreField.setFont(textFont);
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        scoreField.setText(model.getFormattedScore());
        addComponent(innerPanel, scoreField, 0, gridY++, 1, 1, normalInsets,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonActionListener(frame, model));
        addComponent(innerPanel, startButton, 0, gridY++, 1, 1, normalInsets,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        pauseButton = new JToggleButton("Pause");
        pauseButton.addActionListener(new PauseButtonActionListener(frame, model));
        addComponent(innerPanel, pauseButton, 0, gridY++, 1, 1, normalInsets,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonActionListener(frame, model));
        addComponent(innerPanel, stopButton, 0, gridY++, 1, 1, normalInsets,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        panel.add(innerPanel);
    }

    private void addComponent(Container container, Component component, int gridX, int gridY, int gridWidth,
                              int gridHeight, Insets insets, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, 1.0D, 1.0D,
                anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }

    public JToggleButton getPauseButton() {
        return pauseButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setScoreText(String scoreText) {
        this.scoreField.setText(scoreText);
    }

    public JButton getStopButton() {
        return stopButton;
    }
}
