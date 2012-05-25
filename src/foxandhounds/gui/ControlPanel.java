package foxandhounds.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Button;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import foxandhounds.logic.Fox;
import foxandhounds.logic.Hounds;
import foxandhounds.logic.LearningSystem;
import foxandhounds.logic.State;
import foxandhounds.logic.Play;

public class ControlPanel extends JPanel implements ActionListener {
    static private final double defaultExplRate = 0.1;
    static private final double defaultLearningRate = 0.1;
    static private final double defaultDiscountFactor = 0.99;

    private BoardPanel boardPanel;
    private Fox fox = new Fox(defaultExplRate, defaultLearningRate,
                              defaultDiscountFactor);
    private Hounds hounds = new Hounds(defaultExplRate, defaultLearningRate,
                                       defaultDiscountFactor);
    private Play play = new Play(fox, hounds);
    private JButton stepButton = new JButton("Next step");
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JLabel appDelayLabel = new JLabel("Delay (ms):");
    private JTextField appDelay = new JTextField("500");
    private int visualisationDelay = 500; // milliseconds
    private Timer visualisationTimer = new Timer(visualisationDelay, this);
    private JButton reset = new JButton("Reset");
    private JButton openFile = new JButton("Open File");
    private JButton saveFox = new JButton("Save Fox");
    private JButton saveHounds = new JButton("Save Hounds");
    private JFileChooser fileChooser = new JFileChooser();
    private LearningSystemInfo foxInfo = new LearningSystemInfo(fox);
    private LearningSystemInfo houndsInfo = new LearningSystemInfo(hounds);
    private static GridBagConstraints startStopConstraints = constraints(1, 5,
                                                                             1);

    public ControlPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        setLayout(new GridBagLayout());
        stepButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        reset.addActionListener(this);
        openFile.addActionListener(this);
        saveFox.addActionListener(this);
        saveHounds.addActionListener(this);
        fileChooser.setCurrentDirectory(new File ("."));
        add(new JLabel("Fox"), constraints(0, 0, 2));
        add(foxInfo, constraints(0, 1, 2));
        add(new JLabel("Hounds"), constraints(0, 2, 2));
        add(houndsInfo, constraints(0, 3, 2));
        add(appDelayLabel, constraints(0, 4, 1));
        add(appDelay, constraints(1, 4, 1));
        add(stepButton, constraints(0, 5, 1));
        add(startButton, startStopConstraints);
        add(reset, constraints(0, 6, 1));
        add(openFile,constraints(1, 6, 1));
        add(saveFox,constraints(0, 7, 1));
        add(saveHounds,constraints(1, 7, 1));
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
        visualisationTimer.setDelay(Math.max(100,
                                        Integer.parseInt(appDelay.getText())));
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
        } else if (e.getSource() == reset) {
            if (play.isRunning()) {
                visualisationTimer.stop();
                remove(stopButton);
                add(startButton, startStopConstraints);
                validate();
                repaint();
            }
            play.terminate();
            fox = new Fox(defaultExplRate, defaultLearningRate,
                          defaultDiscountFactor);
            foxInfo.setLearningSystem(fox);
            hounds = new Hounds(defaultExplRate, defaultLearningRate,
                                defaultDiscountFactor);
            houndsInfo.setLearningSystem(hounds);
            play = new Play(fox, hounds);
            (new Thread(play)).start();
            update();
        } else if (e.getSource() == openFile) {
            fileChooser.showOpenDialog(this);
            String fileName = fileChooser.getCurrentDirectory().getPath()
                              + File.separator
                              + fileChooser.getSelectedFile().getName();
            LearningSystem learningSystem = LearningSystem.load(fileName);
            if (learningSystem instanceof Fox) {
                fox = (Fox) learningSystem;
                foxInfo.setLearningSystem(fox);
            } else {
                hounds = (Hounds) learningSystem;
                houndsInfo.setLearningSystem(hounds);
            }
            play.terminate();
            play = new Play(fox, hounds);
            (new Thread(play)).start();
            update();
        } else if (e.getSource() == saveFox) {
            fileChooser.showSaveDialog(this);
            String fileName = fileChooser.getCurrentDirectory().getPath()
                              + File.separator
                              + fileChooser.getSelectedFile().getName();
            fox.save(fileName);
        } else if (e.getSource() == saveHounds) {
            fileChooser.showSaveDialog(this);
            String fileName = fileChooser.getCurrentDirectory().getPath()
                              + File.separator
                              + fileChooser.getSelectedFile().getName();
            hounds.save(fileName);
        } else if (e.getSource() == visualisationTimer) {
            update();
        }
    }

    static private GridBagConstraints constraints(int x, int y, int width) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        if (width == 1) gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }
}
