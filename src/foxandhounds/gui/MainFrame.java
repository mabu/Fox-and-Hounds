package foxandhounds.gui;

import java.awt.FlowLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Fox and Hounds");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BoardPanel boardPanel = new BoardPanel();
        ControlPanel controlPanel = new ControlPanel(boardPanel);

        setLayout(new FlowLayout());
        add(boardPanel);
        add(controlPanel);
        repaint();
        setVisible(true);
    }
}
