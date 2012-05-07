/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package foxandhounds.gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Javad
 */
public class Board extends JPanel {
    Board() {
        setLayout(new GridLayout(8, 8));
        for(int i = 0; i < 64; i++) {
            JPanel square = new JPanel();
            if ((i / 8 + i) % 2 == 1) {
                square.setBackground(Color.BLACK);
            } else {
                square.setBackground(Color.WHITE);
            }
            add(square);
        }
    }

    public void addPiece(Piece piece, int squareId) {
        ((JPanel)getComponent(squareId)).add(piece);
    }

}
