package visualgame;

import java.awt.event.*;
import javax.swing.*;

import logic.Game;

import java.awt.Color;
import java.awt.GridLayout;

public class VisuallyGame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel scorePanel, boardGamePanel;
    private Game game;

    public VisuallyGame(Game game) {
        this.game = game;
        this.configFrame();
        this.initComponents();
    }

    private void configFrame() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(1, 2));
    }

    private void initComponents() {
        System.out.println(this.game.getBoard().getHeight());

        this.scorePanel = new JPanel();
        this.scorePanel.setSize(150, 500);
        this.scorePanel.setBackground(Color.CYAN);
        this.getContentPane().add(this.scorePanel);

        this.boardGamePanel = new JPanel();
        this.boardGamePanel.setLayout(new GridLayout(9, 9));

        for (int row = 0; row < this.game.board.getWidth(); row++) {
            for (int column = 0; column < this.game.board.getHeight(); column++) {
                this.game.board.board[row][column].addMouseListener( new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        piecePressed(evt);
                    }
                });
                this.boardGamePanel.add(this.game.board.board[row][column]);
            }
        }
        
        this.getContentPane().add(this.boardGamePanel);

        this.setVisible(true);
    }


    private void piecePressed(MouseEvent evt) {
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