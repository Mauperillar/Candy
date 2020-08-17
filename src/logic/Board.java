package logic;

import javax.swing.*;

public class Board {
    private int width = 9;
    private int height = 9;
    public JButton[][] board;

    Board() {
        this.board = new JButton[this.width][this.height];
    }

    Board(int height, int width) {
        this.height = height;
        this.width = width;
        this.board = new JButton[this.width][this.height];
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void changePositionBetweenPieces(int[] positionPiece1, int positionPiece2[]) {
        Icon iconOfPiece1 = this.getPieceIcon(positionPiece1[0], positionPiece1[1]);
        Icon iconOfPiece2 = this.getPieceIcon(positionPiece2[0], positionPiece2[1]);

        this.putPieceIcon(positionPiece1[0], positionPiece1[1], iconOfPiece2);
        this.putPieceIcon(positionPiece2[0], positionPiece2[1], iconOfPiece1);
    }

    public void putPiece(int row, int colum, JButton button) {
        this.board[row][colum] = button;
    }

    public void putPieceIcon(int row, int colum, Icon icon) {
        this.board[row][colum].setIcon(icon);
    }

    public void removePieceIcon(int row, int colum) {
        this.board[row][colum].setIcon(null);
    }

    public JButton getPiece(int row, int column) {
        return this.board[row][column];
    }

    public Icon getPieceIcon(int row, int column) {
        return this.board[row][column].getIcon();
    }

    public JButton[] getRow(int index) {
        JButton[] row = new JButton[this.width];

        for (int i = 0; i < this.height; i++) {
            row[i] = this.board[index][i];
        }
        return row;
    }

    public JButton[] getColumn(int index) {
        JButton[] column = new JButton[this.height];

        for (int i = 0; i < this.height; i++) {
            column[i] = this.board[i][index];
        }
        return column;
    }

    public boolean T_linedPiecesTogether(int[] positionCandy1, int[] positionCandy2) {
        boolean aligned = false;

        if (Math.abs(positionCandy1[0] - positionCandy2[0]) == 1 && Math.abs(positionCandy1[1] - positionCandy2[1]) == 0
                || Math.abs(positionCandy1[0] - positionCandy2[0]) == 0
                        && Math.abs(positionCandy1[1] - positionCandy2[1]) == 1) {
            aligned = true;
        } else {
            aligned = false;
        }
        return aligned;
    }

}