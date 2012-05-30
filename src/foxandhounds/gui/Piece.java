package foxandhounds.gui;

import java.awt.Color;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Piece extends JLabel {
    private ImageIcon icon;
    private Color backGround;

    public Piece(String name) {
        icon = new ImageIcon(getClass().getResource("/foxandhounds/gui/" + name));
        backGround = Color.BLACK;
        setIcon(icon);
    }

    public ImageIcon returnImage() {
        return icon;
    }

    public Color returnColor() {
        return backGround;
    }
}
