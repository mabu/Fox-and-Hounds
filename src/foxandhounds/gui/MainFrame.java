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

    public MainFrame() {
        JPanel boardPanel = new JPanel();
        this.setTitle("Board");
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        F = new Piece("src/foxandhounds/Image/fox-icon.jpg");
        for (int i = 0; i < 4; ++i) {
            H[i] = new Piece("src/foxandhounds/Image/dog-icon.jpg");
        }

        boardPanel.add(board);

        stepButton.addActionListener(this);
        controlPanel.add(stepButton);
        this.setLayout(new FlowLayout());
        this.add(boardPanel);
        this.add(controlPanel);
        this.repaint();
        showState();

        this.setVisible(true);
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
        }
    }
}
