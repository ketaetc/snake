package ru.dobrovolskyn.snake.game;

import ru.dobrovolskyn.snake.game.enums.Arguments;
import ru.dobrovolskyn.snake.game.model.Frog;
import ru.dobrovolskyn.snake.game.model.Snake;
import ru.dobrovolskyn.snake.game.model.SnakeGameModel;
import ru.dobrovolskyn.snake.game.view.SnakeGameFrame;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SnakeGame {

    private static int gameBoardWidth;
    private static int gameBoardHeight;
    private static int snakeLength;
    private static int frogsCount;

    private static SnakeGameFrame snakeGameFrame;
    private static SnakeGameModel snakeGameModel;

    private static volatile AtomicInteger snakeThreadsCounter = new AtomicInteger(0);
    private static volatile AtomicInteger frogsThreadsCounter = new AtomicInteger(0);

    private static ExecutorService pool;
    private static Random random;

    public static void main(String[] args) throws Exception {
        if (parseArguments(args)) {
            random = new Random();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    snakeGameModel = new SnakeGameModel();
                    snakeGameFrame = new SnakeGameFrame();
                    Snake snake = createSnakeInstance(snakeLength);
                    snakeThreadsCounter.addAndGet(1);
                    snake.setName(snakeThreadsCounter.get());

                    snakeGameModel.setSnake(snake);

                    pool = Executors.newFixedThreadPool(frogsCount + 1);
                    pool.execute(snake);

//                    Thread thread = new Thread(snake, "Snake:" + snakeThreadsCounter.get());
//                    thread.start();

                    for (int i =0; i < frogsCount; i++) {
                        double chance = random.nextDouble();
                        Frog frog = Frog.createRandomFrog(snake.getRandomNonSnakeLocation(), chance);
                        frog.setName(frogsThreadsCounter.addAndGet(1));
                        snakeGameModel.addFrog(frog, frog.getName());

                        pool.execute(frog);
                    }
                }
            });
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

    public static Snake createSnakeInstance(int snakeLength) {
        return new Snake(snakeLength);
    }

    public static SnakeGameFrame getSnakeGameFrame() {
        return snakeGameFrame;
    }

    public static SnakeGameModel getSnakeGameModel() {
        return snakeGameModel;
    }

    public static AtomicInteger getSnakeThreadsCounter() {
        return snakeThreadsCounter;
    }

    public static AtomicInteger getFrogsThreadsCounter() {
        return frogsThreadsCounter;
    }

    private static boolean parseArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Wrong arguments line. Try to use \"-help\".");
            System.out.println("Example:    java -jar Snake.jar \"-help\".");
            System.out.println("Program will run with default arguments");

            gameBoardHeight = 10;
            gameBoardWidth = 10;
            snakeLength = 3;
            frogsCount = 2;

            SnakeGameModel.setCellHeight(gameBoardHeight);
            SnakeGameModel.setCellWidth(gameBoardWidth);
            SnakeGameModel.setSnakeLength(snakeLength);
            SnakeGameModel.setFrogsCount(frogsCount);

//            return false;
            return true;
        }
        if (args.length != 0) {
            if (args[0].equals("-help")) {
                System.out.println("Please type arguments:");
                System.out.println("-height:    game board height (can't be less than 10)");
                System.out.println("-width:     game board width (can't be less than 10)");
                System.out.println("-length:    snake initial length (can't be less than 2 units)");
                System.out.println("-frogs:     count of frogs on game board (can't be less than 1)");
                System.out.println();
                System.out.println("Example:    java -jar Snake.jar -height 11 -width 11 -length 3 -frogs 2");

                return false;
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

                return true;
            }
        }

        return false;
    }

    public static ExecutorService getPool() {
        synchronized (pool) {
            return pool;
        }
    }
}
