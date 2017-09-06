package ru.dobrovolskyn.snake.game.util;

import ru.dobrovolskyn.snake.game.enums.Arguments;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;

import java.util.concurrent.Future;

public class Utils {
    private static volatile int gameBoardWidth;
    private static volatile int gameBoardHeight;
    private static volatile int snakeLength;
    private static volatile int frogsCount;
    private static volatile long snakeSleep;

    private static volatile Future<?> snakeFuture;

    public static Integer getArgument(String[] args, Arguments argument) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argument.getArgument())) {
                return Integer.valueOf(args[i + 1]);
            }
        }
        return 0;
    }

    public static boolean parseArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Wrong arguments line. Try to use \"-help\".");
            System.out.println("Example:    java -jar Snake.jar \"-help\".");
            System.out.println("Program will run with default arguments");

            gameBoardHeight = 10;
            gameBoardWidth = 10;
            snakeLength = 2;
            frogsCount = 1;
            snakeSleep = 500L;

            SnakeGameModel.setBoardHeight(gameBoardHeight);
            SnakeGameModel.setBoardWidth(gameBoardWidth);
            SnakeGameModel.setSnakeLength(snakeLength);
            SnakeGameModel.setFrogsCount(frogsCount);
            SnakeGameModel.setSnakeSleep(snakeSleep);

            return true;
        }
        if (args.length != 0) {
            if (args[0].equals("-help")) {
                System.out.println("Please type arguments:");
                System.out.println("-height:    game board height (can't be less than 10)");
                System.out.println("-width:     game board width (can't be less than 10)");
                System.out.println("-length:    snake initial length (can't be less than 2 units)");
                System.out.println("-frogs:     count of frogs on game board (can't be less than 1)");
                System.out.println("-sleep:     snake sleeping time in milliseconds (can't be less than 500)");
                System.out.println();
                System.out.println("Example:    java -jar Snake.jar -height 11 -width 11 -length 3 -frogs 2");

                return false;
            } else {
                gameBoardHeight = getArgument(args, Arguments.GAME_BOARD_HEIGHT);
                gameBoardWidth = getArgument(args, Arguments.GAME_BOARD_WIDTH);
                snakeLength = getArgument(args, Arguments.SNAKE_LENGTH);
                frogsCount = getArgument(args, Arguments.FROGS_COUNT);
                snakeSleep = getArgument(args, Arguments.SNAKE_SLEEP);

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
                if (snakeSleep < 500L) {
                    snakeSleep = 500;
                    System.out.println("Illegal value of frogs count parameter.");
                    System.out.println("Default value will be used: " + snakeSleep);
                }

                SnakeGameModel.setBoardHeight(gameBoardHeight);
                SnakeGameModel.setBoardWidth(gameBoardWidth);
                SnakeGameModel.setSnakeLength(snakeLength);
                SnakeGameModel.setFrogsCount(frogsCount);
                SnakeGameModel.setSnakeSleep(snakeSleep);

                return true;
            }
        }

        return false;
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

    public static long getSnakeSleep() {
        return snakeSleep;
    }

    public static Future getSnakeFuture() {
        return snakeFuture;
    }

    public static void setSnakeFuture(Future<?> snakeFuture) {
        Utils.snakeFuture = snakeFuture;
    }
}
