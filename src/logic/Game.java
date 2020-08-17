package logic;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.JButton;

import logic.candies.Candy;
import logic.candies.ListCandies;
import visualgame.*;

public class Game {
    public Board board = new Board();
    private ListCandies candies = new ListCandies();
    private Player player;
    private int availableMovements = 50;
    private int points = 0;
    HashMap<String, int[]> positionOfSelectedCandies = new HashMap<String, int[]>();

    public VisuallyGame visuallyGame;

    Scanner scanner = new Scanner(System.in);

    Game() {
        System.out.print("Escriba su nombre: ");
        this.player = new Player(this.scanner.nextLine());
        this.fillAllBoard();
        this.visuallyGame = new VisuallyGame(this);
        this.startGame();
    }

    public void addPositionOfSelectedPiece(int[] position) {

        if (this.positionOfSelectedCandies.containsKey("candy1")) {

            if (Arrays.equals(this.positionOfSelectedCandies.get("candy1"), position)) {
                this.positionOfSelectedCandies.clear();
            } else {
                this.positionOfSelectedCandies.put("candy2", position);
            }

        } else {
            this.positionOfSelectedCandies.put("candy1", position);
        }

    }

    private void startGame() {
        boolean canToPlay = this.canToPlay();
        boolean wantToPlay = true;

        while (canToPlay && wantToPlay) {
            this.rewievRepeatedCandiesOnBoard();
            while (this.availableMovements > 0 && this.points < 1000) {
                if (!this.canMakeMoreMovements()) {
                    this.fillAllBoard();
                }
                if (positionOfSelectedCandies.containsKey("candy2")) {
                    this.exchangeSelectedCandy();
                    this.positionOfSelectedCandies.clear();
                }
            }

            if (this.points < 1000) {
                this.player.decreaseLife(1);
            } else {
                this.player.levelUp();
            }

            canToPlay = this.canToPlay();
            if (canToPlay) {
                wantToPlay = this.scanner.nextBoolean();
            }
            this.fillAllBoard();
        }

    }

    private boolean canToPlay() {
        boolean can = this.player.getLifes() > 0;
        if (!can) {
            System.out.println("Oh no! No tienes más vidas para jugar");
        }
        return can;
    }

    private final void fillAllBoard() {
        for (int row = 0; row < this.board.getWidth(); row++) {
            for (int column = 0; column < this.board.getHeight(); column++) {
                Candy newCandy = this.candies.getRandomCandy();
                JButton button = new JButton(newCandy.getIcon());
                button.setMaximumSize(new Dimension(70, 70));
                button.setMinimumSize(new Dimension(70, 70));
                this.board.putPiece(row, column, button);
            }
        }
    }

    private void sumPoints(int numberRepeatedCandys) {
        switch (numberRepeatedCandys) {
            case 3:
                this.points += 50;
                break;
            case 4:
                this.points += 100;
                break;
            case 5:
                this.points += 200;
                break;
            case 6:
                this.points += 400;
                break;
        }
    }

    private boolean rewievRepeatedCandiesOnBoard() {
        boolean haveRepeatedCandies = false;
        boolean hasTheBoardChanged;
        int[] intervalRepeat = new int[2];
        Character[] axes = { 'x', 'y' };

        do {
            hasTheBoardChanged = false;

            for (Character axis : axes) {
                int heigth;
                if (axis.equals('x')) {
                    heigth = this.board.getWidth();
                } else {
                    heigth = this.board.getHeight();
                }

                for (int indexAxis = heigth - 1; indexAxis >= 0; indexAxis--) {
                    intervalRepeat = null;

                    if (axis.equals('x')) {
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getRow(indexAxis), 3);
                    } else {
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getColumn(indexAxis), 3);
                    }

                    if (intervalRepeat != null) {
                        int numberRepeatedCandys = intervalRepeat[1] - intervalRepeat[0] + 1;
                        this.sumPoints(numberRepeatedCandys);

                        this.removeCandyOnline(intervalRepeat[0], intervalRepeat[1], axis, indexAxis);
                        this.descendCandy(intervalRepeat, axis, indexAxis);

                        hasTheBoardChanged = true;
                        haveRepeatedCandies = true;
                    }

                }
            }
        } while (hasTheBoardChanged);

        return haveRepeatedCandies;
    }

    private int[] intervalCandiesRepeated(JButton[] line, int minRepeat) {
        int[] interval = this.intervalCandiesRepeated(line, minRepeat, 0, line.length - 1);
        return interval;
    }

    private int[] intervalCandiesRepeated(JButton[] line, int minRepeat, int initIndex, int endIndex) {
        int countRepeatCandy = 1;
        Icon currentCandy, nextCandy;
        int[] interval = new int[2];

        for (int index = initIndex; index < endIndex; index++) {
            currentCandy = line[index].getIcon();
            nextCandy = line[index + 1].getIcon();

            if (countRepeatCandy == 1) {
                interval[0] = index;
            }

            if (nextCandy.equals(currentCandy)) {
                countRepeatCandy++;
                interval[1] = index + 1;
            } else {
                if (countRepeatCandy >= minRepeat) {
                    break;
                } else {
                    countRepeatCandy = 1;
                }
            }
        }

        if (countRepeatCandy < minRepeat) {
            return null;
        } else {
            return interval;
        }

    }

    private void removeCandyOnline(int from, int to, Character eje, int indexAxis) {
        System.out.println("/// Removiendo dulces repetidos ///");
        System.out.println();
        for (int i = from; i <= to; i++) {
            if (eje.equals('x')) {
                this.board.removePieceIcon(indexAxis, i);
            } else if (eje.equals('y')) {
                this.board.removePieceIcon(i, indexAxis);
            }
            this.sleep();
        }
    }

    private void descendCandy(int[] rangeEmpty, Character eje, int indexAxis) {

        if (eje.equals('x')) {
            this.descendCandyXAxis(rangeEmpty, indexAxis);
        } else if (eje.equals('y')) {
            this.descendCandyYAxis(rangeEmpty, indexAxis);
        }

        System.out.println("\n" + "################################");
        System.out.println("/// Bajando nuevos dulces ///" + "\n");
    }

    private void descendCandyXAxis(int[] rangeEmpty, int indexRow) {
        Icon previousPiece;
        for (int column = rangeEmpty[0]; column <= rangeEmpty[1]; column++) {
            for (int row = indexRow; row > 0; row--) {
                previousPiece = this.board.getPieceIcon(row - 1, column);
                if (previousPiece == null) {
                    Candy newCandy = this.candies.getRandomCandy();
                    previousPiece = newCandy.getIcon();
                }
                this.board.putPieceIcon(row, column, previousPiece);
            }
            Candy newCandy = this.candies.getRandomCandy();
            this.board.putPieceIcon(0, column, newCandy.getIcon());
        }
        this.sleep();
    }

    private void descendCandyYAxis(int[] rangeEmpty, int indexColumn) {
        Icon previousPiece;
        for (int row = rangeEmpty[1]; row > 0; row--) {
            previousPiece = this.board.getPieceIcon(row - 1, indexColumn);
            if (previousPiece == null) {
                Candy newCandy = this.candies.getRandomCandy();
                previousPiece = newCandy.getIcon();
            }

            this.board.putPieceIcon(row, indexColumn, previousPiece);
        }
        Candy newCandy = this.candies.getRandomCandy();
        this.board.putPieceIcon(0, indexColumn, newCandy.getIcon());
    }

    public void exchangeSelectedCandy() {
        boolean canExchange = false;
        boolean hadRepeated = false;

        if (positionOfSelectedCandies.containsKey("candy1") && positionOfSelectedCandies.containsKey("candy2")) {
            canExchange = this.board.T_linedPiecesTogether(positionOfSelectedCandies.get("candy1"),
                    positionOfSelectedCandies.get("candy2"));
        }

        if (canExchange) {

            this.board.changePositionBetweenPieces(positionOfSelectedCandies.get("candy1"),
                    positionOfSelectedCandies.get("candy2"));

            this.visuallyGame.repaint();

            hadRepeated = this.rewievRepeatedCandiesOnBoard();

            if (!hadRepeated) {
                this.board.changePositionBetweenPieces(positionOfSelectedCandies.get("candy1"),
                        positionOfSelectedCandies.get("candy2"));

            } else {
                this.availableMovements--;
            }
        }
    }

    private boolean canMakeMoreMovements() {
        int[] intervalRepeat = new int[2];
        Character[] axes = { 'x', 'y' };

        for (Character axis : axes) {
            int heigth;
            if (axis.equals('x')) {
                heigth = this.board.getWidth();
            } else {
                heigth = this.board.getHeight();
            }

            JButton[] lineToCheck = new JButton[heigth];

            for (int indexAxis = heigth - 1; indexAxis >= 0; indexAxis--) {
                intervalRepeat = null;

                if (axis.equals('x')) {
                    lineToCheck = this.board.getRow(indexAxis);
                } else {
                    lineToCheck = this.board.getColumn(indexAxis);
                }

                intervalRepeat = this.intervalCandiesRepeated(lineToCheck, 2);
                int initIndex = 0;
                int endIndex = heigth - 1;
                while (intervalRepeat != null) {
                    // Verificar moviendo dulces horizontalmente
                    if (intervalRepeat[0] > 1) {
                        if (axis.equals('x')) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(indexAxis, intervalRepeat[0] - 2);
                        } else {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(intervalRepeat[0] - 2, indexAxis);
                        }
                    }

                    if (intervalRepeat[1] < heigth - 2) {
                        if (axis.equals('x')) {
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(indexAxis, intervalRepeat[0] + 2);
                        } else {
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(intervalRepeat[0] + 2, indexAxis);
                        }
                    }
                    if (this.intervalCandiesRepeated(lineToCheck, 2, initIndex, endIndex) == null) {
                        return true;
                    }

                    if (axis.equals('x')) {
                        if (intervalRepeat[0] > 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(indexAxis, intervalRepeat[0] - 2);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if (intervalRepeat[1] < heigth - 2) {
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(indexAxis, intervalRepeat[0] + 2);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if (indexAxis > 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(indexAxis - 1, intervalRepeat[0]);
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(indexAxis - 1, intervalRepeat[1]);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if (indexAxis < this.board.getHeight() - 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(indexAxis + 1, intervalRepeat[0]);
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(indexAxis + 1, intervalRepeat[0]);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }
                    } else if (axis.equals('y')) {
                        if (intervalRepeat[0] > 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(intervalRepeat[0] - 2, indexAxis);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }

                        if (intervalRepeat[1] < heigth - 2) {
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(intervalRepeat[0] + 2, indexAxis);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                        // Cambiar horizontalemnte la columna 'y' por dulces de la izquierda
                        if (indexAxis > 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(intervalRepeat[0], indexAxis - 1);
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(intervalRepeat[1], indexAxis - 1);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                        // Cambiar horizontalemnte la columna 'y' por dulces de la derecha
                        if (indexAxis < this.board.getWidth() - 1) {
                            lineToCheck[intervalRepeat[0] - 1] = this.board.getPiece(intervalRepeat[0], indexAxis + 1);
                            lineToCheck[intervalRepeat[1] + 1] = this.board.getPiece(intervalRepeat[1], indexAxis + 1);
                            if (this.intervalCandiesRepeated(lineToCheck, 3, initIndex, endIndex) == null) {
                                return true;
                            } else {
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                    }

                    initIndex = intervalRepeat[1] + 1;
                }
            }
        }
        return false;
    }

    private void sleep() {

        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}