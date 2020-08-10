import java.awt.event.*;
import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

public class VisuallyGame extends JFrame{
    private JPanel scorePanel, boardGamePanel;
    private Game game;
    private JButton[][] pieceButtons;

    public VisuallyGame(Game game) {
        this.game = game;
        this.configFrame();
        this.initComponents();
    }

    private void configFrame(){
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(1,2));
    }

    private void initComponents(){
        System.out.println(this.game.getBoard().getHeight());
        

        this.scorePanel = new JPanel();
        this.scorePanel.setMinimumSize(new Dimension(150,500));
        this.scorePanel.setSize(150, 500);
        this.scorePanel.setBackground(Color.CYAN);
        this.getContentPane().add(this.scorePanel);

        this.boardGamePanel = new JPanel();
        this.fillBoardGamePanel();
        this.getContentPane().add(this.boardGamePanel);
        

        this.setVisible(true);
    }

    private void fillBoardGamePanel(){
        Board boardGame = this.game.getBoard(); 
        //Change position the following statement
        this.boardGamePanel.setLayout(new GridLayout(boardGame.getWidth(), boardGame.getHeight()));

        this.pieceButtons = new JButton[boardGame.getWidth()][ boardGame.getHeight()];
        for(int row = 0; row< boardGame.getWidth(); row++){
            for(int column = 0; column<boardGame.getHeight(); column++){
                this.pieceButtons[row][column] = new JButton(boardGame.getPiece(row, column).toString());
                this.pieceButtons[row][column].addMouseListener( new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        piecePressed(evt);
                    }
                });
                this.boardGamePanel.add(this.pieceButtons[row][column]);
            }
        }
    }

    private void piecePressed(MouseEvent evt){
        Dimension d = this.getIndexOfPiece((JButton) evt.getSource());
        System.out.println(d.getWidth()+" "+d.getHeight());
    }

    private Dimension getIndexOfPiece(JButton buttonToCompare){
        for(int row = 0; row< this.pieceButtons.length; row++){
            for(int column = 0; column<this.pieceButtons[0].length; column++){
                if(buttonToCompare == this.pieceButtons[row][column]){
                    return new Dimension(row,column);
                }
            }
        }
        return null;
    }
}