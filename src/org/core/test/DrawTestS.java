package org.core.test;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: alfaRomeoPC
 * Date: 17/01/12
 * Time: 12.31
 * To change this template use File | Settings | File Templates.
 */
public class DrawTestS extends JFrame {
    

    @Override
	public void paint(final Graphics g){
        final Graphics2D g2 = (Graphics2D)g;
       final int x=10;
       final int y =47;
       int w = 194;

       for(int i = 0; i<=7; i++){

          if(i<4){
           w = w +2;
              g2.drawLine(x,y+i,w+i,y+i);
          }else if(i>=3 && i<=5){
              w = w +1;
              g2.drawLine(x,y+i,w+i,y+i);
          }else{
             g2.drawLine(x,y+i,w+i,y+i);
           }
       }

       w = w+7;

       for(int i = 7; i<=20; i++){
           g2.drawLine(x,y+i,w,y+i);
           w = w +1;
       }

        w = w-20;
        for(int i = 21; i<=25; i++){
            g2.drawLine(x,y+i,w+i,y+i);
            w = w +2;
        }



        g2.drawString("Rectangle",20,100);
    }


    
    public static void main(final String arg[]){

        final DrawTestS frame = new DrawTestS();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setBounds(100, 100, 500, 600);
        frame.setVisible(true);
        
        
        
    }
}
