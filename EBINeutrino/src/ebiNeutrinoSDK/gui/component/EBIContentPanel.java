package ebiNeutrinoSDK.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class EBIContentPanel  extends JPanel {

       public ArrayList<EBIDrawImgProp> imgList = new ArrayList<EBIDrawImgProp>();

       public  EBIContentPanel(){
           setOpaque(false);
       }

        @Override
        public void paintComponent(final Graphics g){
              if(this.imgList.size() > 0){
                   final Graphics2D g2 = (Graphics2D)g;
                   for(int l = 0; l<this.imgList.size(); l++ ){
                     final EBIDrawImgProp prp = this.imgList.get(l);
                       if(prp.isDrawRepeatImageW()){
                           for(int i =prp.getX(); i< prp.getW(); i+=prp.getIntervall()){
                                 g2.drawImage(prp.getImg(),i,prp.getY(),prp.getObs());
                           }
                       }
                       if(prp.isDrawRepeatImageH()){
                           for(int i =prp.getY(); i< prp.getH(); i+=prp.getIntervall()){
                               g2.drawImage(prp.getImg(),prp.getX(),i,prp.getObs());
                           }
                       }
                   }
                  super.paintComponent(g);
              }

        }

        public void drawRepeatedImageWidth(final Image img, final int x, final int y, final int w, final int intervall, final ImageObserver obs){

            final EBIDrawImgProp prp = new EBIDrawImgProp();

            prp.setImg(img);
            prp.setX(x);
            prp.setY(y);
            prp.setW(w);
            prp.setIntervall(intervall);
            prp.setObs(obs);
            prp.setDrawRepeatImageW(true);
            this.imgList.add(prp);
        }

        public void drawRepeatedImageHeight(final Image img, final int x, final int y, final int h, final int intervall, final ImageObserver obs){
            final EBIDrawImgProp prp = new EBIDrawImgProp();

            prp.setImg(img);
            prp.setX(x);
            prp.setY(y);
            prp.setH(h);
            prp.setIntervall(intervall);
            prp.setObs(obs);
            prp.setDrawRepeatImageH(true);
            this.imgList.add(prp);
        }

}
