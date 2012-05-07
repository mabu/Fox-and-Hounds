package foxandhounds.gui;

import java.awt.Color;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Piece extends JLabel {
    private ImageIcon Icon;
    private Color backGround;

    public Piece(String NameIcon) {
        Icon = new ImageIcon(NameIcon);
        backGround = Color.BLACK;
        this.setIcon(Icon);
    }

    public ImageIcon returnImage() {
        return Icon;
    }

    public Color returnColor() {
        return backGround;
    }
}
