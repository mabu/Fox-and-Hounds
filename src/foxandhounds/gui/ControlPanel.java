package foxandhounds.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JTextField;
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
    private int visualisationDelay = 500; // milliseconds
    private Timer visualisationTimer = new Timer(visualisationDelay, this);
    private LearningSystemInfo foxInfo = new LearningSystemInfo(fox);
    private LearningSystemInfo houndsInfo = new LearningSystemInfo(hounds);
    private static GridBagConstraints startStopConstraints = constraints(1, 5, 1);

    public ControlPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;

        setLayout(new GridBagLayout());
        stepButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        add(new JLabel("Fox"), constraints(0, 0, 2));
        add(foxInfo, constraints(0, 1, 2));
        add(new JLabel("Hounds"), constraints(0, 2, 2));
        add(houndsInfo, constraints(0, 3, 2));
        add(appDelayLabel, constraints(0, 4, 1));
        add(appDelay, constraints(1, 4, 1));
        add(stepButton, constraints(0, 5, 1));
        add(startButton, startStopConstraints);
        setParameters();
        update();
        
        (new Thread(play)).start();
    }

    private void setParameters() {
        fox.setDiscountFactor(foxInfo.getDiscountFactor());
        fox.setExplorationRate(foxInfo.getExplorationRate());
        fox.setLearningRate(foxInfo.getLearningRate());
        hounds.setDiscountFactor(houndsInfo.getDiscountFactor());
        hounds.setExplorationRate(houndsInfo.getExplorationRate());
        hounds.setLearningRate(houndsInfo.getLearningRate());
        play.setDelay(Integer.parseInt(appDelay.getText()));
        visualisationTimer.setDelay(Math.max(100, Integer.parseInt(appDelay.getText())));
    }

    private void update() {
        foxInfo.update();
        houndsInfo.update();
        boardPanel.showState(play.getState());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepButton) {
            setParameters();
            play.step();
            update();
        } else if (e.getSource() == startButton) {
            remove(startButton);
            add(stopButton, startStopConstraints);
            validate();
            repaint();
            setParameters();
            play.start();
            visualisationTimer.start();
        } else if (e.getSource() == stopButton) {
            remove(stopButton);
            add(startButton, startStopConstraints);
            validate();
            repaint();
            play.stop();
            visualisationTimer.stop();
            update();
        } else if (e.getSource() == visualisationTimer) {
            update();
        }
    }

    static private GridBagConstraints constraints(int x, int y, int width) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        return gbc;
    }
}
