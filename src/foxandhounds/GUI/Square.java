/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package foxandhounds.GUI;



import javax.swing.JPanel;

/**
 *
 * @author Javad
 */
public class Square extends JPanel{
    
    private int x;
    private int y;
    private Fox F ;
    private Hound H;
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
    public void addFox(Fox newF){
        F=newF;
        this.add(F);

        
    }
    public void addHound(Hound newH){
        H=newH;
    }
    public Fox getFox(){
        return F;
    }
    public Hound getHound(){
        return H;
    }


   

}
