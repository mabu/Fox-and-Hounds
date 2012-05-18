package foxandhounds.gui;

import foxandhounds.logic.LearningSystem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;

/**
 * Displays information about learning system.
 */
public class LearningSystemInfo extends JPanel {
    private LearningSystem learningSystem;
    private JLabel explorationRateLabel = new JLabel("Exploration rate:");
    private JTextField explorationRate;
    private JLabel learningRateLabel = new JLabel("Learninig rate:");
    private JTextField learningRate;
    private JLabel discountFactorLabel = new JLabel("Discount factor:");
    private JTextField discountFactor;
    private JLabel turnsLabel = new JLabel("Turns made:");
    private JLabel turns;
    private JLabel winsLabel = new JLabel("Wins:");
    private JLabel wins;
    private JLabel lossesLabel = new JLabel("Losses:");
    private JLabel losses;
    private JLabel statesVisitedLabel = new JLabel("States visited:");
    private JLabel statesVisited;

    public LearningSystemInfo(LearningSystem learningSystem) {
        this.learningSystem = learningSystem;
        explorationRate = new JTextField(String.valueOf(learningSystem.getExplorationRate()));
        learningRate = new JTextField(String.valueOf(learningSystem.getLearningRate()));
        discountFactor = new JTextField("0.95");
        turns = new JLabel(String.valueOf(learningSystem.getTurns()));
        wins = new JLabel(String.valueOf(learningSystem.getWins()));
        losses = new JLabel(String.valueOf(learningSystem.getLosses()));
        statesVisited = new JLabel(String.valueOf(learningSystem.getStatesVisited()));

        setLayout(new GridLayout(7, 2));
        add(explorationRateLabel);
        add(explorationRate);
        add(learningRateLabel);
        add(learningRate);
        add(discountFactorLabel);
        add(discountFactor);
        add(turnsLabel);
        add(turns);
        add(winsLabel);
        add(wins);
        add(lossesLabel);
        add(losses);
        add(statesVisitedLabel);
        add(statesVisited);
    }

    public void update() {
        turns.setText(String.valueOf(learningSystem.getTurns()));
        wins.setText(String.valueOf(learningSystem.getWins()));
        losses.setText(String.valueOf(learningSystem.getLosses()));
        statesVisited.setText(String.valueOf(learningSystem.getStatesVisited()));
    }

    public double getExplorationRate() {
        return Double.parseDouble(explorationRate.getText());
    }

    public double getLearningRate() {
        return Double.parseDouble(learningRate.getText());
    }

    public double getDiscountFactor() {
        return Double.parseDouble(discountFactor.getText());
    }

    public void setLearningSystem(LearningSystem learningSystem) {
        this.learningSystem = learningSystem;
        explorationRate.setText(String.valueOf(learningSystem.getExplorationRate()));
        learningRate.setText(String.valueOf(learningSystem.getLearningRate()));
        discountFactor.setText(String.valueOf(learningSystem.getDiscountFactor()));
    }
}
