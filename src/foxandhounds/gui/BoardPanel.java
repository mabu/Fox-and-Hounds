package foxandhounds.gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import foxandhounds.logic.State;
import foxandhounds.logic.Coordinates;

public class BoardPanel extends JPanel {
    private Piece fox;
    private Piece[] hounds = new Piece[4];

    public BoardPanel() {
    }

    public void init(ControlPanel controlPanel) {
        setLayout(new GridLayout(8, 8));
        for(int i = 0; i < 64; i++) {
            JPanel square = new JPanel();
            if ((i / 8 + i) % 2 == 1) {
                square.setBackground(Color.BLACK);
                square.setName(String.valueOf((63 - i) / 8 * 4 + i % 8 / 2));
                square.addMouseListener(controlPanel);
            } else {
                square.setBackground(Color.WHITE);
            }
            add(square);
        }
        fox = new Piece("fox-icon.jpg");
        for (int i = 0; i < 4; ++i) {
            hounds[i] = new Piece("dog-icon.jpg");
        }
    }

    public void addPiece(Piece piece, int squareId) {
        ((JPanel)getComponent(squareId)).add(piece);
    }

    private int idByCoordinates(Coordinates coordinates) {
        int row = coordinates.getRow();
        return (7 - row) * 8 + coordinates.getColumn() * 2 + row % 2;
    }

    public void showState(State state) {
        addPiece(fox, idByCoordinates(state.getFox()));
        Coordinates[] houndsCoordinates = state.getHounds();
        for (int i = 0; i < 4; ++i) {
            addPiece(hounds[i], idByCoordinates(houndsCoordinates[i]));
        }
        repaint();
    }
}
