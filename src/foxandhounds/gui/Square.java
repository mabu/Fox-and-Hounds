/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package foxandhounds.gui;



import javax.swing.JPanel;

/**
 *
 * @author Javad
 */
public class Square extends JPanel{
    
    private int x;
    private int y;
    private GFox F ;
    private GHound H;
    //private Color BG;
    public Square(int X, int Y){
        x=X;
        y=Y;
        

    }
    public int getXCor(){
        return x;
    }
    public int getYCor(){
        return y;
    }
    public void setXCor(int X){
        x=X;
    }
    public void setYCor(int Y){
        y=Y;
    }
    public void addFox(GFox newF){
        F=newF;
        this.add(F);

        
    }
    public void addHound(GHound newH){
        H=newH;
    }
    public GFox getFox(){
        return F;
    }
    public GHound getHound(){
        return H;
    }


   

}
