/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package foxandhounds.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Javad
 */
public class Board extends JPanel{

    Board(){
       initBoard();
    }
    private void initBoard(){
        this.setLayout(new GridLayout(8,8));
        boolean oddRow= true;

        for(int i =1; i<=64; i++ ){
            Square l = new Square(i,i/8);
            //l.add(new Hound());
            l.setOpaque(true);
            if((i/8)%2==0)
                oddRow=false;
            else
                oddRow=true;
            if (i%8==0)
                oddRow=!oddRow;
            if(oddRow)
                if ((i%2)==0)
                    l.setBackground(Color.WHITE);

                else
                    l.setBackground(Color.BLACK);
            else
                if ((i%2)==0)
                    l.setBackground(Color.BLACK);
                else
                    l.setBackground(Color.WHITE);
            this.add(l);
            //System.out.println(this.getComponent(i-1));
        }
    }
    public Square getSquareById(int i){
        Square s = (Square)this.getComponent(i);
        //s.getXCor();
        return s;
    }
    public void addFox(Fox F, int squareId){
        //System.out.print(squareId);
        Square s = getSquareById(squareId);
        s.add(F);
        //repaint();
        //this.add(F);
    }
     public void addHound(Hound H, int squareId){
        Square s = getSquareById(squareId);
        s.add(H);
        //this.add(F);
    }

}
