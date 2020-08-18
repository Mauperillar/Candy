package visualgame;

import java.awt.event.*;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import logic.Game;

public class BoardPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    Game game;

    public BoardPanel(Game game) {
        this.game = game;
        this.configPane();
        this.fillBoard();
    }

    private void configPane() {
        this.setLayout(new GridLayout(this.game.board.getWidth(), this.game.board.getHeight()));
    }

    public void fillBoard(){
        for (int row = 0; row < this.game.board.getWidth(); row++) {
            for (int column = 0; column < this.game.board.getHeight(); column++) {
                this.game.board.board[row][column].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        piecePressed(evt);
                    }
                });

                this.add(this.game.board.board[row][column]);
            }
        }

    }

    private void piecePressed(ActionEvent evt) {
        int[] positionPiece = this.getIndexOfPiece((JButton) evt.getSource());
        this.game.addPositionOfSelectedPiece(positionPiece);
    }

    private int[] getIndexOfPiece(JButton buttonToCompare) {
        for (int row = 0; row < this.game.board.getWidth(); row++) {
            for (int column = 0; column < this.game.board.getWidth(); column++) {
                if (buttonToCompare == this.game.board.getPiece(row, column)) {
                    int[] positionButton = { row, column };
                    return positionButton;
                }
            }
        }
        return null;
    }

}