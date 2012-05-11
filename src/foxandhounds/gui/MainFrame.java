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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
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
    private int visualisationDelay = 500; // milliseconds
    private Timer visualisationTimer = new Timer(visualisationDelay, this);

    public MainFrame() {
        JPanel boardPanel = new JPanel();
        setTitle("Board");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        F = new Piece("src/foxandhounds/Image/fox-icon.jpg");
        for (int i = 0; i < 4; ++i) {
            H[i] = new Piece("src/foxandhounds/Image/dog-icon.jpg");
        }

        boardPanel.add(board);

        stepButton.addActionListener(this);
        controlPanel.add(stepButton);
        startButton.addActionListener(this);
        controlPanel.add(startButton);
        stopButton.addActionListener(this);
        controlPanel.add(stepButton);
        setLayout(new FlowLayout());
        add(boardPanel);
        add(controlPanel);
        repaint();
        showState();

        (new Thread(play)).start();

        setVisible(true);
    }

    private int idByCoordinates(Coordinates coordinates) {
        int row = coordinates.getRow();
        return (7 - row) * 8 + coordinates.getColumn() * 2 + row % 2;
    }

    private void showState() {
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
            play.start();
            visualisationTimer.start();
        } else if (e.getSource() == stopButton) {
            controlPanel.remove(stopButton);
            controlPanel.add(startButton);
            controlPanel.validate();
            play.stop();
            visualisationTimer.stop();
        } else if (e.getSource() == visualisationTimer) {
            showState();
        }
    }
}
