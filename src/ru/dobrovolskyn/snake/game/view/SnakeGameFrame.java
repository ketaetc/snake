package ru.dobrovolskyn.snake.game.view;

import ru.dobrovolskyn.snake.game.SnakeGame;
import ru.dobrovolskyn.snake.game.controller.ArrowAction;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.runnable.GameRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

public class SnakeGameFrame {
    private static final NumberFormat NF = NumberFormat.getInstance();
    private ControlPanel controlPanel;
    private GameRunnable gameRunnable;
    private GridPanel gridPanel;
    private JFrame frame;
    private SnakeGameModel model;

    public SnakeGameFrame() {
        this.model = SnakeGame.getSnakeGameModel();

        createPartControl();
    }

    private void createPartControl() {
        frame = new JFrame();
        frame.setTitle("Snake");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));

        gridPanel = new GridPanel(model);
        mainPanel.add(gridPanel);

        controlPanel = new ControlPanel(this, model);
        mainPanel.add(controlPanel.getPanel());

        frame.add(mainPanel);
        frame.pack();

        setKeyBindings(gridPanel);

        frame.setLocationByPlatform(true);
        frame.getRootPane().setDefaultButton(controlPanel.getStartButton());
        frame.setVisible(true);

        gameRunnable = new GameRunnable(this, model);
        new Thread(gameRunnable).start();
    }

    private void setKeyBindings(JPanel gridPanel) {
        InputMap inputMap = gridPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("W"), "up arrow");
        inputMap.put(KeyStroke.getKeyStroke("S"), "down arrow");
        inputMap.put(KeyStroke.getKeyStroke("A"), "left arrow");
        inputMap.put(KeyStroke.getKeyStroke("D"), "right arrow");

        inputMap.put(KeyStroke.getKeyStroke("UP"), "up arrow");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down arrow");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left arrow");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right arrow");

        inputMap = gridPanel.getInputMap(JPanel.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke("UP"), "up arrow");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down arrow");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left arrow");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right arrow");

        gridPanel.getActionMap().put("up arrow",
                new ArrowAction(model, new Point(0, -1)));
        gridPanel.getActionMap().put("down arrow",
                new ArrowAction(model, new Point(0, 1)));
        gridPanel.getActionMap().put("left arrow",
                new ArrowAction(model, new Point(-1, 0)));
        gridPanel.getActionMap().put("right arrow",
                new ArrowAction(model, new Point(1, 0)));
    }

    private void exitProcedure() {
        model.getSnake().setRunning(false);
        frame.dispose();
        System.exit(0);
    }

    public void repaintGridPanel() {
        gridPanel.repaint();
    }

    public void setScoreText() {
        controlPanel.setScoreText(NF.format(model.getScore()));
    }

    public void setPauseButton() {
        controlPanel.setPauseButton(model.isGameActive());
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public SnakeGameModel getModel() {
        return model;
    }

    public GridPanel getGridPanel() {
        return gridPanel;
    }
}
