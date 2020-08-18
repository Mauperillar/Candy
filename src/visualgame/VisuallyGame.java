package visualgame;

import javax.swing.*;

import logic.Game;

import java.awt.BorderLayout;
public class VisuallyGame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public BoardPanel boardGamePanel;
    private ScorePane scorePanel;
    private Game game;
    public int selectedButtons = 0;

    public VisuallyGame(Game game) {
        this.game = game;
        this.configFrame();
        this.initComponents();
        this.pack();
    }

    private void configFrame() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {

        this.scorePanel = new ScorePane();
        this.getContentPane().add(this.scorePanel, BorderLayout.PAGE_START);
        this.scorePanel.setValueScore(game.getPoints());
        this.scorePanel.setValueMovements(this.game.getAvailableMovements());
        this.scorePanel.setValueProgressBar(game.getPercentScore());

        this.boardGamePanel = new BoardPanel(this.game);
        this.getContentPane().add(this.boardGamePanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public ScorePane getScorePanel(){
        return this.scorePanel;
    }

    public boolean getWantToPlay(){
        int respuesta = JOptionPane.showConfirmDialog(null,"¿Deseas jugar otra partida?","",JOptionPane.YES_NO_OPTION);
        boolean wantToPlay = respuesta == 0?true:false;
        return wantToPlay;
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public BoardPanel getBoardGamePanel(){
        return this.boardGamePanel;
    }
}