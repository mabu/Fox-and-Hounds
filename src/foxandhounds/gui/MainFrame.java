package foxandhounds.gui;

import foxandhounds.logic.Coordinates;
import foxandhounds.logic.Fox;
import foxandhounds.logic.Hounds;
import foxandhounds.logic.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Javad
 */
public class MainFrame extends JFrame implements ActionListener {

    private State state;
    private Fox fox = new Fox(0.1, 0.1, 0.95);
    private Piece F;
    private Piece[] H = new Piece[4];
    private Hounds hounds = new Hounds(0.1, 0.1, 0.95);
    private Board board = new Board();
    private boolean foxTurn = true;

    public MainFrame() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, FontFormatException, UnsupportedLookAndFeelException {
        initUI();
    }

    private void initUI() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, FontFormatException, IOException, UnsupportedLookAndFeelException {

        JPanel info = new JPanel();
        JPanel boardPanel = new JPanel();
        this.setSize(800,800);
        this.setVisible(true);
        this.setTitle("Board");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        state = fox.startGame();
        F = new Piece("src/foxandhounds/Image/fox-icon.jpg");
        for (int i = 0; i < 4; ++i) {
            H[i] = new Piece("src/foxandhounds/Image/dog-icon.jpg");
        }

        boardPanel.add(board);
        Button step = new Button("Next Step");

        step.addActionListener(this);
        info.add(step);
        this.setLayout(new FlowLayout());
        this.add(boardPanel);
        this.add(info);
        this.repaint();
        showState();
    }

    private int idByCoordinates(Coordinates coordinates) {
        int row = coordinates.getRow();
        return (7 - row) * 8 + coordinates.getColumn() * 2 + row % 2;
    }

    private void showState() {
        board.addPiece(F, idByCoordinates(state.getFox()));
        Coordinates[] houndsCoordinates = state.getHounds();
        for (int i = 0; i < 4; ++i) {
            board.addPiece(H[i], idByCoordinates(houndsCoordinates[i]));
        }
        board.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (foxTurn) {
            state = fox.move(state);
        } else {
            state = hounds.move(state);
        }
        if (state.isFinal()) {
            // update final Q-values and restart the game
            hounds.move(state);
            fox.move(state);
            state = fox.startGame();
            foxTurn = false;
        }
        showState();
        foxTurn = !foxTurn;
    }
}
