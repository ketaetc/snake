package ru.dobrovolskyn.snake.game.view;

import ru.dobrovolskyn.snake.game.enums.Rotations;
import ru.dobrovolskyn.snake.game.runnable.Frog;
import ru.dobrovolskyn.snake.game.model.Segment;
import ru.dobrovolskyn.snake.game.runnable.Snake;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class GridPanel extends JPanel {
    private static final long serialVersionUID = 3259516267781813618L;
    private SplashImage gameOverImage;
    private SplashImage gameStoppedImage;
    private SnakeGameModel model;

    public GridPanel(final SnakeGameModel model) {
        this.model = model;
        this.gameOverImage = new SplashImage(model, "Game Over");
        this.gameOverImage.run();
        this.gameStoppedImage = new SplashImage(model, "Game Stopped");
        this.gameStoppedImage.run();

        Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
        this.setBorder(border);
        this.setPreferredSize(model.getPreferredSize());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        model.getSnake().rotate(Rotations.LEFT);
                        break;
                    case MouseEvent.BUTTON3:
                        model.getSnake().rotate(Rotations.RIGHT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//    @Override
//    public void repaint(Rectangle r) {
//        super.repaint(r);
//
//
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int cellWidth = SnakeGameModel.getCellWidth();
        int cellHeight = SnakeGameModel.getCellHeight();
        int squareWidth = SnakeGameModel.getSquareWidth();

        int x = 0;
        int y = 0;

        for (int w = 0; w < cellWidth; w++) {
            for (int h = 0; h < cellHeight; h++) {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x, y, squareWidth, squareWidth);
//                g2d.drawRect(x, y, squareWidth, squareWidth);
                y += squareWidth;
            }
            x += SnakeGameModel.getSquareWidth();
            y = 0;
        }

        drawSnake(g2d, squareWidth);
        drawFrog(g2d, squareWidth);

        if (model.isGameOver()) {
            g2d.drawImage(gameOverImage.getImage(), 0, 0, this);
        }

        if (model.isGameStopped()) {
            g2d.drawImage(gameStoppedImage.getImage(), 0, 0, this);
        }
    }

    private void drawSnake(Graphics2D g2d, int squareWidth) {
        int x;
        int y;
        Snake snake = model.getSnake();
        List<Segment> segments = snake.getSnakeCells();

        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            Point p = segment.getLocation();
            x = p.x * squareWidth;
            y = p.y * squareWidth;

//            g2d.setColor(Color.RED);
//            g2d.drawRect(x, y, squareWidth, squareWidth);

            g2d.setColor(segment.getColor());

            if (i == 0) {
                drawCircle(g2d, x, y, squareWidth, 2);
            } else if (i == segments.size() - 1) {
                drawCircle(g2d, x, y, squareWidth, 4);
            } else {
                drawCircle(g2d, x, y, squareWidth, 3);
            }
        }
    }

    private void drawCircle(Graphics2D g2d, int x, int y, int radius, int scale) {
        int halfRad = radius / 2;
        int xX = x + halfRad - halfRad / scale;
        int yY = y + halfRad - halfRad / scale;
        int rad = radius / scale;
        g2d.fillOval(xX, yY, rad, rad);
    }

    private void drawFrog(Graphics2D g2d, int squareWidth) {
//        List<? extends Frog> frogList = model.getFrogList();
//        for (Frog frog : frogList) {
//        Map<Frog, String> frogsMap = model.getFrogsMap();
        Map<Frog, Future<?>> frogsMap = model.getFrogsMap();
        for (Frog frog : frogsMap.keySet()) {
            Point p = frog.getLocation();

            int x = p.x * squareWidth;
            int y = p.y * squareWidth;

//            g2d.setColor(Color.RED);
//            g2d.drawRect(x, y, squareWidth, squareWidth);

            g2d.setColor(frog.getColor());

            drawCircle(g2d, x, y, squareWidth, 3);
        }
    }
}
