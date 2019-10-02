package ebiNeutrinoSDK.gui.component;


import java.awt.*;
import java.awt.image.ImageObserver;

public class EBIDrawImgProp {

    private int x = 0;
    private int y = 0;
    private int w = 0;
    private int h = 0;
    private int intervall = 0;
    private int counterW = 0;
    private int counterH = 0;
    private Image img = null;
    private ImageObserver obs = null;
    private boolean isDrawRepeatImageW = false;
    private boolean isDrawRepeatImageH = false;

    public EBIDrawImgProp(){}

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(final int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(final int h) {
        this.h = h;
    }

    public int getIntervall() {
        return intervall;
    }

    public void setIntervall(final int intervall) {
        this.intervall = intervall;
    }

    public int getCounterW() {
        return counterW;
    }

    public void setCounterW(final int counterW) {
        this.counterW = counterW;
    }

    public int getCounterH() {
        return counterH;
    }

    public void setCounterH(final int counterH) {
        this.counterH = counterH;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(final Image img) {
        this.img = img;
    }

    public ImageObserver getObs() {
        return obs;
    }

    public void setObs(final ImageObserver obs) {
        this.obs = obs;
    }

    public boolean isDrawRepeatImageW() {
        return isDrawRepeatImageW;
    }

    public void setDrawRepeatImageW(final boolean drawRepeatImageW) {
        isDrawRepeatImageW = drawRepeatImageW;
    }

    public boolean isDrawRepeatImageH() {
        return isDrawRepeatImageH;
    }

    public void setDrawRepeatImageH(final boolean drawRepeatImageH) {
        isDrawRepeatImageH = drawRepeatImageH;
    }
}
