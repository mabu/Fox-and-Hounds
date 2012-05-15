package foxandhounds.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JTextField;
import java.awt.GridLayout;
import foxandhounds.logic.Fox;
import foxandhounds.logic.Hounds;
import foxandhounds.logic.State;
import foxandhounds.logic.Play;

public class ControlPanel extends JPanel implements ActionListener {
    private BoardPanel boardPanel;
    private Fox fox = new Fox(0.1, 0.1, 0.95);
    private Hounds hounds = new Hounds(0.1, 0.1, 0.95);
    private Play play = new Play(fox, hounds);

    private JButton stepButton = new JButton("Next step");
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JLabel appDelayLabel = new JLabel("Delay (ms):");
    private JTextField appDelay = new JTextField("500");
    private JLabel foxExpRateLabel = new JLabel("Fox exploration rate:");
    private JTextField foxExpRate = new JTextField("0.1");
    private JLabel foxLearnLabel = new JLabel("Fox learninig rate:");
    private JTextField foxLearn = new JTextField("0.1");
    private JLabel foxDiscountLabel = new JLabel("Fox discount factor:");
    private JTextField foxDiscount = new JTextField("0.95");
    private JLabel houndExpRateLabel = new JLabel("Hound exploration rate:");
    private JTextField houndExpRate = new JTextField("0.1");
    private JLabel houndLearnLabel = new JLabel("Hound learninig rate:");
    private JTextField houndLearn = new JTextField("0.1");
    private JLabel houndDiscountLabel = new JLabel("Hound discount factor:");
    private JTextField houndDiscount = new JTextField("0.95");
    private JLabel stepLabel = new JLabel("Step number:");
    private JLabel stepText = new JLabel("0");
    private JLabel foxWinsLabel = new JLabel("Fox wins:");
    private JLabel foxWinsText = new JLabel("0");
    private JLabel houndsWinsLabel = new JLabel("Hounds wins:");
    private JLabel houndsWinsText = new JLabel("0");
    private JLabel foxStatesVisitedLabel = new JLabel("Fox states visited:");
    private JLabel foxStatesVisitedText = new JLabel("0");
    private JLabel houndsStatesVisitedLabel = new JLabel("Hounds states visited:");
    private JLabel houndsStatesVisitedText = new JLabel("0");
    private int visualisationDelay = 500; // milliseconds
    private Timer visualisationTimer = new Timer(visualisationDelay, this);

    public ControlPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;

        setLayout(new GridLayout(13, 2));
        stepButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        add(stepLabel);
        add(stepText);
        add(foxWinsLabel);
        add(foxWinsText);
        add(houndsWinsLabel);
        add(houndsWinsText);
        add(foxStatesVisitedLabel);
        add(foxStatesVisitedText);
        add(houndsStatesVisitedLabel);
        add(houndsStatesVisitedText);
        add(appDelayLabel);
        add(appDelay);
        add(foxDiscountLabel);
        add(foxDiscount);
        add(foxExpRateLabel);
        add(foxExpRate);
        add(foxLearnLabel);
        add(foxLearn);
        add(houndDiscountLabel);
        add(houndDiscount);
        add(houndExpRateLabel);
        add(houndExpRate);
        add(houndLearnLabel);
        add(houndLearn);
        add(stepButton);
        add(startButton);
        setParameters();
        update();
        
        (new Thread(play)).start();
    }

    private void setParameters() {
        fox.setDiscountFactor(Double.parseDouble(foxDiscount.getText()));
        fox.setExplorationRate(Double.parseDouble(foxExpRate.getText()));
        fox.setLearningRate(Double.parseDouble(foxLearn.getText()));
        hounds.setDiscountFactor(Double.parseDouble(houndDiscount.getText()));
        hounds.setExplorationRate(Double.parseDouble(houndExpRate.getText()));
        hounds.setLearningRate(Double.parseDouble(houndLearn.getText()));
        play.setDelay(Integer.parseInt(appDelay.getText()));
        visualisationTimer.setDelay(Math.max(100, Integer.parseInt(appDelay.getText())));
    }

    private void update() {
        stepText.setText(String.valueOf(fox.getTurns() + hounds.getTurns()));
        foxWinsText.setText(String.valueOf(fox.getWins()));
        houndsWinsText.setText(String.valueOf(hounds.getWins()));
        foxStatesVisitedText.setText(String.valueOf(fox.getStatesVisited()));
        houndsStatesVisitedText.setText(String.valueOf(hounds.getStatesVisited()));
        boardPanel.showState(play.getState());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepButton) {
            setParameters();
            play.step();
            update();
        } else if (e.getSource() == startButton) {
            remove(startButton);
            add(stopButton);
            validate();
            repaint();
            setParameters();
            play.start();
            visualisationTimer.start();
        } else if (e.getSource() == stopButton) {
            remove(stopButton);
            add(startButton);
            validate();
            repaint();
            play.stop();
            visualisationTimer.stop();
            update();
        } else if (e.getSource() == visualisationTimer) {
            update();
        }
    }
}
