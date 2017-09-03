package ru.dobrovolskyn.snake.game;

import ru.dobrovolskyn.snake.game.enums.Arguments;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;

public class SnakeGame {

    private static int gameBoardWidth;
    private static int gameBoardHeight;
    private static int snakeLength;
    private static int frogsCount;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Wrong arguments line. Try to use \"-help\".");
        }

        if (args.length != 0) {
            if (args[0].equals("-help")) {
                System.out.println("Please type arguments:");
                System.out.println("-height:    game board height (can't be less than 10)");
                System.out.println("-width:    game board width (can't be less than 10)");
                System.out.println("-length:    snake initial length (can't be less than 2 units)");
                System.out.println("-frogs:    count of frogs on game board (can't be less than 1)");
            } else {
                gameBoardHeight = getArgument(args, Arguments.GAME_BOARD_HEIGHT);
                gameBoardWidth = getArgument(args, Arguments.GAME_BOARD_WIDTH);
                snakeLength = getArgument(args, Arguments.SNAKE_LENGTH);
                frogsCount = getArgument(args, Arguments.FROGS_COUNT);

                if (gameBoardHeight < 10) {
                    gameBoardHeight = 10;
                    System.out.println("Illegal value of height parameter.");
                    System.out.println("Default value will be used: " + gameBoardHeight);
                }
                if (gameBoardWidth < 10) {
                    gameBoardWidth = 10;
                    System.out.println("Illegal value of width parameter.");
                    System.out.println("Default value will be used: " + gameBoardWidth);
                }
                if (snakeLength < 2) {
                    snakeLength = 2;
                    System.out.println("Illegal value of snake length parameter.");
                    System.out.println("Default value will be used: " + snakeLength);
                }
                if (frogsCount < 1) {
                    frogsCount = 1;
                    System.out.println("Illegal value of frogs count parameter.");
                    System.out.println("Default value will be used: " + frogsCount);
                }

                SnakeGameModel.setCellHeight(gameBoardHeight);
                SnakeGameModel.setCellWidth(gameBoardWidth);
                SnakeGameModel.setSnakeLength(snakeLength);
                SnakeGameModel.setFrogsCount(frogsCount);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new SnakeGameFrame(new SnakeGameModel());
                    }
                });
            }
        }

    }

    public static Integer getArgument(String[] args, Arguments argument) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argument.getArgument())) {
                return Integer.valueOf(args[i + 1]);
            }
        }
        return 0;
    }

    public static int getGameBoardWidth() {
        return gameBoardWidth;
    }

    public static int getGameBoardHeight() {
        return gameBoardHeight;
    }

    public static int getSnakeLength() {
        return snakeLength;
    }

    public static int getFrogsCount() {
        return frogsCount;
    }
}
