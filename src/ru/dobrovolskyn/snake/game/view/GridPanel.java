package ru.dobrovolskyn.snake.game.view;

import ru.dobrovolskyn.snake.game.enums.SnakeRotations;
import ru.dobrovolskyn.snake.game.model.Frog;
import ru.dobrovolskyn.snake.game.model.Segment;
import ru.dobrovolskyn.snake.game.model.Snake;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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
                        model.getSnake().rotate(SnakeRotations.LEFT);
                        break;
                    case MouseEvent.BUTTON3:
                        model.getSnake().rotate(SnakeRotations.RIGHT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

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

            g2d.setColor(Color.RED);
            g2d.drawRect(x, y, squareWidth, squareWidth);

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
        g2d.fillOval(x + radius / 2, y + radius / 2, radius / scale, radius / scale);
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r, int scale) {
        x += r / 2;
        y += r / 2;

        r /= scale;

        g.fillOval(x, y, r, r);
    }

    private void drawFrog(Graphics2D g2d, int squareWidth) {
        List<? extends Frog> frogList = model.getFrogList();
        for (Frog frog : frogList) {
            Point p = frog.getLocation();

            int x = p.x * squareWidth;
            int y = p.y * squareWidth;

            g2d.setColor(Color.RED);
            g2d.drawRect(x, y, squareWidth, squareWidth);

            g2d.setColor(frog.getColor());

            drawCircle(g2d, x, y, squareWidth, 3);
        }
    }
}
