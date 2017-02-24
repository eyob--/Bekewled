package edu.mbhs.bekewled;

import android.graphics.Bitmap;

public class Jewel {

    private Triangle picture;
    private int row;
    private int col;

    public enum JewelType{

        KOCH(1, false), SIERPINSKI(2, false), CARPET(3, false), DRAGON(4, false), TREE(5, false), APOLONIAN(6, false),
                HILBERT(7, false), MANDLEBROT(8, true), JULIA(9, true), NEWTON(10, true);

        private int number;
        private boolean special;

        JewelType(int num, boolean cool){
            number = num;
            special = cool;
        }
    }
//lets have min row/col be 0, max be 8
    public Jewel(int type, Bitmap b, int roe, int cahl){
        picture = new Triangle(0.1, (cahl-4)/4*0.8, (roe-4)/4*0.8, b);
        row = roe;
        col = cahl;
    }
    public void setpos(int roo, int cool){
        row = roo;
        col = cool;

    }

}
