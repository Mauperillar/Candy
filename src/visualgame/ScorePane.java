package visualgame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class ScorePane extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private JLabel score, movements, name;
    private JProgressBar progressBar = new JProgressBar();

    ScorePane(){
        this.configPane();
        this.initComponents();
    }

    private void configPane(){
        this.setLayout(new GridBagLayout());
    }

    private void initComponents(){
        this.configScorePane();
        this.configProgressBar();

        this.name = new JLabel("Jugador: Player1");
        this.score = new JLabel("Puntaje: 0");
        this.movements = new JLabel("Movimientos: 0");

        this.add(name, new GridBagConstraints());
        this.add(score, new GridBagConstraints());
        this.add(movements, new GridBagConstraints());
        this.add(this.progressBar, new GridBagConstraints());
    }

    private void configScorePane(){
        this.setBackground(new java.awt.Color(204, 255, 255));

        java.awt.GridBagLayout scorePaneLayout = new java.awt.GridBagLayout();
        scorePaneLayout.rowHeights = new int[] {1, 2, 1};
        scorePaneLayout.columnWeights = new double[] {0.2, 0.2, 0.2};

        this.setLayout(scorePaneLayout);
    }

    private void configProgressBar(){
        //
    }

    public void setName(String newName){
        this.name.setText(newName);
    }

    public void setValueScore(int newValue){
        this.score.setText("Puntaje: "+newValue);
    }

    public void setValueMovements(int newValue){
        this.movements.setText("Movimientos: "+newValue);
    }

    public void setValueProgressBar(int newValue){
        this.progressBar.setValue(newValue);
    }
}