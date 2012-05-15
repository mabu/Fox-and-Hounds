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
import foxandhounds.logic.LearningSystem;
import foxandhounds.logic.State;
import foxandhounds.logic.Play;
import java.awt.Button;
import java.io.*;
import javax.swing.*;

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
    private JButton openFile = new JButton("Open File");
    private JButton saveFox = new JButton("Save Fox");
    private JButton saveHounds = new JButton("Save Hounds");
    private JFileChooser fc = new JFileChooser();
    private MainFrame frame;
    private LearningSystemInfo foxInfo = new LearningSystemInfo(fox);
    private LearningSystemInfo houndsInfo = new LearningSystemInfo(hounds);
    private static GridBagConstraints startStopConstraints = constraints(1, 5, 1);

    public ControlPanel(BoardPanel boardPanel, MainFrame frame) {
        this.boardPanel = boardPanel;
        this.frame = frame;
        setLayout(new GridBagLayout());
        stepButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        openFile.addActionListener(this);
        saveFox.addActionListener(this);
        saveHounds.addActionListener(this);
        fc.setCurrentDirectory(new File ("."));
        add(new JLabel("Fox"), constraints(0, 0, 2));
        add(foxInfo, constraints(0, 1, 2));
        add(new JLabel("Hounds"), constraints(0, 2, 2));
        add(houndsInfo, constraints(0, 3, 2));
        add(appDelayLabel, constraints(0, 4, 1));
        add(appDelay, constraints(1, 4, 1));
        add(stepButton, constraints(0, 5, 1));
        add(startButton, startStopConstraints);
        add(openFile,constraints(0, 6, 1));
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
        visualisationTimer.setDelay(Math.max(100, Integer.parseInt(appDelay.getText())));
    }

    private void update() {
        foxInfo.update();
        houndsInfo.update();
        boardPanel.showState(play.getState());
    }

    public void Save(String fileName, LearningSystem L) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fout));
            oos.writeObject(L);
            oos.close();
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
            //return false;
        }


    }

    public void Load(String fileName) {

        try {

            FileInputStream fin = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fin));
            Object s = ois.readObject();
            if (s instanceof Fox) {
                fox = (Fox) s;
            } else {
                hounds = (Hounds) s;
            }
            ois.close();
            play = new Play(fox, hounds);
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        } else if (e.getSource() == openFile) {
            try {
                fc.showOpenDialog(frame);
                String fileName = fc.getSelectedFile().getName();
                fileName=fc.getCurrentDirectory().getPath()+File.separator+ fileName;
                Load(fileName);
            } catch (Exception e2) {
            }
        } else if (e.getSource() == saveFox) {
            try {
                fc.showSaveDialog(frame);
                String fileName = fc.getSelectedFile().getName();
                fileName=fc.getCurrentDirectory().getPath()+File.separator+ fileName;
                Save(fileName, fox);
            } catch (Exception e2) {
            }
        } else if (e.getSource() == saveHounds) {
            try{
            fc.showSaveDialog(frame);
            String fileName = fc.getSelectedFile().getName();
            fileName=fc.getCurrentDirectory().getPath()+File.separator+ fileName;
            Save(fileName, hounds);
            }catch (Exception e2){}
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
