/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class MainFrame extends JFrame {

    private State state;
    private Fox fox = new Fox(0.1, 0.1, 0.95);
    private GFox F;
    private GHound[] H = new GHound[4];
    private Hounds hounds = new Hounds(0.1, 0.1, 0.95);

    public MainFrame() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, FontFormatException, UnsupportedLookAndFeelException {
        initUI();
    }

    private void initUI() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, FontFormatException, IOException, UnsupportedLookAndFeelException {

        JPanel info = new JPanel();
        JPanel boardPanel = new JPanel();
        this.setSize(600, 650);
        this.setLocation(0, 0);
        this.setVisible(true);
        final Board b = new Board();
        this.setTitle("Board");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        state = fox.startGame();
        Coordinates foxCoordinates = state.getFox();
        F = new GFox("src/foxandhounds/Image/fox-icon.jpg",
                foxCoordinates.getRow(),
                foxCoordinates.getColumn(), Color.BLACK);
        Coordinates[] houndsCoordinates = state.getHounds();
        for (int i = 0; i < 4; ++i) {
            H[i] = new GHound("src/foxandhounds/Image/dog-icon.jpg",
                    houndsCoordinates[i].getRow(),
                    houndsCoordinates[i].getColumn(), Color.BLACK);
            b.addHound(H[i], idByXY(H[i].getXCor(), H[i].getYCor()));
        }

        b.addFox(F, idByXY(F.getXCor(), F.getYCor()));

        boardPanel.add(b);
        Button step = new Button("Next Step");

        step.addActionListener(new ActionListener() {

            boolean foxTurn = true;

            public void actionPerformed(ActionEvent e) {
                //System.out.println();
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
                Coordinates foxCoordinates = state.getFox();
                F.setPos(foxCoordinates.getColumn(), foxCoordinates.getRow());
                b.addFox(F, idByXY(F.getXCor(), F.getYCor()));
                Coordinates[] houndsCoordinates = state.getHounds();
                for (int i = 0; i < 4; ++i) {
                    H[i].setPos(houndsCoordinates[i].getColumn(),
                            houndsCoordinates[i].getRow());
                    b.addHound(H[i], idByXY(H[i].getXCor(), H[i].getYCor()));
                }
                foxTurn = !foxTurn;
                b.repaint();
            }
        });
        info.add(step);
        this.setLayout(new FlowLayout());
        this.add(boardPanel);
        this.add(info);
        this.repaint();

    }

    public int idByXY(int col, int row) {

        return (7 - row) * 8 + col * 2 + row % 2;

    }
}
