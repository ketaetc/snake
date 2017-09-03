package ru.dobrovolskyn.snake.game.view;

import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SplashImage implements Runnable {
    private BufferedImage image;
    private SnakeGameModel model;
    private String splashString;

    public SplashImage(SnakeGameModel model, String splashString) {
        this.model = model;
        this.splashString = splashString;
    }

    public void run() {
        Dimension d = model.getPreferredSize();
        image = new BufferedImage(d.width, d.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        Color c = new Color(0.753f, 0.753f, 0.753f, 0.15f);
        g.setColor(c);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.BLUE);
        Font font = g.getFont();
        Font largeFont = font.deriveFont(36.0F);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r = largeFont.getStringBounds(splashString, frc);
        int rWidth = (int) Math.round(r.getWidth());
        int rHeight = (int) Math.round(r.getHeight());
        int rX = (int) Math.round(r.getX());
        int rY = (int) Math.round(r.getY());

        int x = (d.width / 2) - (rWidth / 2) - rX;
        int y = (d.height / 2) - (rHeight / 2) - rY;

        g.setFont(largeFont);
        g.drawString(splashString, x, y);

        g.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }

}