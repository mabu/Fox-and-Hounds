package foxandhounds.gui;

import foxandhounds.logic.Coordinates;
import foxandhounds.logic.Fox;
import foxandhounds.logic.Hounds;
import foxandhounds.logic.State;
import foxandhounds.logic.Play;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Javad
 */
public class MainFrame extends JFrame implements ActionListener {
    private Fox fox = new Fox(0.1, 0.1, 0.95);
    private Piece F;
    private Piece[] H = new Piece[4];
    private Hounds hounds = new Hounds(0.1, 0.1, 0.95);
    private Board board = new Board();
    private Play play = new Play(fox, hounds);
    private JPanel controlPanel = new JPanel();
    private JButton stepButton = new JButton("Next Step");
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JLabel appDelayLabel = new JLabel("Delay (ms):");
    private TextField appDelay = new TextField("500");
    private JLabel foxExpRateLabel = new JLabel("Fox Exploration Rate:");
    private TextField foxExpRate = new TextField("0.1");
    private JLabel foxLearnLabel = new JLabel("Fox Learninig Rate:");
    private TextField foxLearn = new TextField("0.1");
    private JLabel foxDiscountLabel = new JLabel("Fox Discount Factor:");
    private TextField foxDiscount = new TextField("0.95");
    private JLabel houndExpRateLabel = new JLabel("Hound Exploration Rate:");
    private TextField houndExpRate = new TextField("0.1");
    private JLabel houndLearnLabel = new JLabel("Hound Learninig Rate:");
    private TextField houndLearn = new TextField("0.1");
    private JLabel houndDiscountLabel = new JLabel("Hound Discount Factor:");
    private TextField houndDiscount = new TextField("0.95");
    private JLabel stepLabel = new JLabel("Step Number:");
    private JLabel stepText = new JLabel("0");
    private JLabel foxWinsLabel = new JLabel("Fox wins:");
    private JLabel foxWinsText = new JLabel("0");
    private JLabel houndsWinsLabel = new JLabel("Hounds wins:");
    private JLabel houndsWinsText = new JLabel("0");
    private int visualisationDelay = 500; // milliseconds
    private Timer visualisationTimer = new Timer(visualisationDelay, this);

    public MainFrame() {
        JPanel boardPanel = new JPanel();
        setTitle("Board");
        setSize(850, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlPanel.setLayout(new GridLayout(11, 2));
        F = new Piece("src/foxandhounds/Image/fox-icon.jpg");
        for (int i = 0; i < 4; ++i) {
            H[i] = new Piece("src/foxandhounds/Image/dog-icon.jpg");
        }

        boardPanel.add(board);
        
        stepButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        controlPanel.add(stepLabel);
        controlPanel.add(stepText);
        controlPanel.add(foxWinsLabel);
        controlPanel.add(foxWinsText);
        controlPanel.add(houndsWinsLabel);
        controlPanel.add(houndsWinsText);
        controlPanel.add(appDelayLabel);
        controlPanel.add(appDelay);
        controlPanel.add(foxDiscountLabel);
        controlPanel.add(foxDiscount);
        controlPanel.add(foxExpRateLabel);
        controlPanel.add(foxExpRate);
        controlPanel.add(foxLearnLabel);
        controlPanel.add(foxLearn);
        controlPanel.add(houndDiscountLabel);
        controlPanel.add(houndDiscount);
        controlPanel.add(houndExpRateLabel);
        controlPanel.add(houndExpRate);
        controlPanel.add(houndLearnLabel);
        controlPanel.add(houndLearn);
        controlPanel.add(stepButton);
        controlPanel.add(startButton);
        setLayout(new FlowLayout());
        add(boardPanel);
        add(controlPanel);
        repaint();
        setParameters();
        showState();
        
        (new Thread(play)).start();

        setVisible(true);
    }

    private void setParameters(){
        fox.setDiscountFactor(Double.parseDouble(foxDiscount.getText()));
        fox.setExplorationRate(Double.parseDouble(foxExpRate.getText()));
        fox.setLearningRate(Double.parseDouble(foxLearn.getText()));
        hounds.setDiscountFactor(Double.parseDouble(houndDiscount.getText()));
        hounds.setExplorationRate(Double.parseDouble(houndExpRate.getText()));
        hounds.setLearningRate(Double.parseDouble(houndLearn.getText()));
        play.setDelay(Integer.parseInt(appDelay.getText()));
        visualisationTimer.setDelay(Integer.parseInt(appDelay.getText()));
    }
    private int idByCoordinates(Coordinates coordinates) {
        int row = coordinates.getRow();
        return (7 - row) * 8 + coordinates.getColumn() * 2 + row % 2;
    }

    private void showState() {
        stepText.setText(String.valueOf(play.getSteps()));
        foxWinsText.setText(String.valueOf(play.getFoxWins()));
        houndsWinsText.setText(String.valueOf(play.getHoundsWins()));
        State state = play.getState();
        board.addPiece(F, idByCoordinates(state.getFox()));
        Coordinates[] houndsCoordinates = state.getHounds();
        for (int i = 0; i < 4; ++i) {
            board.addPiece(H[i], idByCoordinates(houndsCoordinates[i]));
        }
        board.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepButton) {
            play.step();
            showState();
        } else if (e.getSource() == startButton) {
            controlPanel.remove(startButton);
            controlPanel.add(stopButton);
            controlPanel.validate();
            controlPanel.repaint();
            setParameters();
            play.start();
            visualisationTimer.start();
        } else if (e.getSource() == stopButton) {
            controlPanel.remove(stopButton);
            controlPanel.add(startButton);
            controlPanel.validate();
            controlPanel.repaint();
            play.stop();
            visualisationTimer.stop();
            showState();
        } else if (e.getSource() == visualisationTimer) {
            showState();
        }
    }
}
