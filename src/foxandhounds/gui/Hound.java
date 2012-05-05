/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package foxandhounds.gui;


import java.awt.Color;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Javad
 */
public class Hound extends JLabel{
     private int X, Y;
    private ImageIcon Icon;
    private Color backGround;
    private Point XYPoint = new Point();

    public Hound(String NameIcon, int startY, int startX, Color BG) {

        Icon = new ImageIcon(NameIcon);
        backGround = BG;
        X = startX;
        Y = startY;
        this.setIcon(Icon);
        //this.setBackground(BG);

    }

    public ImageIcon returnImage() {
        return Icon;
    }

    public Color returnColor() {
        return backGround;
    }

    public void setColor(Color BG) {
        backGround = BG;
    }

    public int returnX() {
        return X;
    }

    public void setPos(int newX, int newY) {
        X = newX;
        Y = newY;
    }

    public int getXCor() {
        return X;
    }

    public int getYCor() {
        return Y;
    }

    public void setPoint(int newX, int newY) {
        XYPoint.x = newX;
        XYPoint.y = newY;
        X = newX;
        Y = newY;
    }

    public Point getPos() {
        return XYPoint;
    }

    public boolean Canmove(int y, Color BG) {
        if ((y == Y + 1) && (backGround==BG)){
            return true;
        } else {

            return false;
        }



    }
}



