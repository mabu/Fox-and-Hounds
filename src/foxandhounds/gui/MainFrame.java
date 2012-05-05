/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package foxandhounds.gui;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.GridLayout;
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

    public MainFrame() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, FontFormatException, UnsupportedLookAndFeelException {
        initUI();
    }

    private void initUI() throws ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, FontFormatException, IOException, UnsupportedLookAndFeelException {
        
        
     this.setSize(600,600);
     this.setLocation(0,0);
     this.setVisible(true);
     
     this.setTitle("Board");
     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     Board b = new Board();
     Fox F = new Fox("src/Image/fox-icon.jpg",8,3,Color.BLACK);
     Hound H1 = new Hound("src/Image/dog-icon.jpg",1,2,Color.BLACK);
     Hound H2 = new Hound("src/Image/dog-icon.jpg",1,4,Color.BLACK);
     Hound H3 = new Hound("src/Image/dog-icon.jpg",1,6,Color.BLACK);
     Hound H4 = new Hound("src/Image/dog-icon.jpg",1,8,Color.BLACK);
     b.addFox(F, idByXY(F.getXCor(),F.getYCor()));
     b.addHound(H1,  idByXY(H1.getXCor(),H1.getYCor()));
     b.addHound(H2,  idByXY(H2.getXCor(),H2.getYCor()));
     b.addHound(H3,  idByXY(H3.getXCor(),H3.getYCor()));
     b.addHound(H4,  idByXY(H4.getXCor(),H4.getYCor()));
     this.add(b);
     
    }
    public int idByXY(int x, int y){
        
            return ((y-1)*8+x)-1;
        
    }
    
}
