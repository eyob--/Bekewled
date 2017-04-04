package edu.mbhs.bekewled;

import android.graphics.Bitmap;

public class Jewel {

    private Triangle picture;
    private int row;
    private int col;
    public static final float jewelWidth = 0.19f;
    public static final float jewelDist = jewelWidth+0.04f;
    public enum JewelType{

        KOCH(0, false), SIERPINSKI(1, false), CARPET(2, false), DRAGON(3, false), TREE(4, false), APOLONIAN(5, false),
                HILBERT(6, false), MANDLEBROT(7, true), JULIA(8, true), NEWTON(9, true);

        private int number;
        private boolean special;

        JewelType(int num, boolean cool){
            number = num;
            special = cool;
        }
    }
    private JewelType j;
//lets have min row/col be 0, max be 8
    public Jewel(int type, Bitmap b, int roe, int cahl){
        this(JewelType.values()[type], b, roe, cahl);
    }
    public Jewel(JewelType type, Bitmap b, int roe, int cahl){
        System.out.println((cahl-4)/4f*0.8f);
        //picture = new Triangle(jewelWidth, (cahl-4)/4f*0.8, (roe-4)/4f*0.8, b);
        float[] pos = intToFloatPos(cahl, roe);
        picture = new Triangle(jewelWidth, pos[0], pos[1], b);
        row = roe;
        col = cahl;
        j = type;
    }
    public Jewel(int type, int roe, int cahl){
        System.out.println((cahl-4)/4f*0.8f);
        //picture = new Triangle(jewelWidth, (cahl-4)/4f*0.8, (roe-4)/4f*0.8, b);
        float[] pos = intToFloatPos(cahl, roe);
        picture = new Triangle(jewelWidth, pos[0], pos[1], type);
        row = roe;
        col = cahl;
        j = JewelType.values()[type];
    }
    public void setpos(int roo, int cool){
        row = roo;
        col = cool;
        //picture.setDest((cool-4)/4f*0.8f, (roo-4)/4f*0.8f);
        float[] pos = intToFloatPos(cool, roo);
        picture.setDest(pos[0], pos[1]);
    }
    public void fallpos(int roo, int cool, int roo2, int cool2) {
        row = roo;
        col = cool;
        float[] posf = intToFloatPos(cool, roo);
        float[] pos0 = intToFloatPos(cool2, roo2);
        picture.setCenter(pos0[0],pos0[1]);
        picture.setDest(posf[0],posf[1]);
    }
    public void dontMove(int roo, int cool){
        //picture.setDest2((cool - 4) / 4f * 0.8f, (roo - 4) / 4f * 0.8f);
        picture.setDest2((cool - 4f) * jewelDist, (roo - 4f) * jewelDist);
        //float[] pos = intToFloatPos(roo, cool);
        //picture.setDest2(pos[0], pos[1]);
    }
    public void draw(float[] mvpMatrix) {
        picture.draw(mvpMatrix);
    }
    public void doMoves(){
        picture.moveHoriz();
        picture.moveVert();
    }

    //Should not use picture.getCenterX because that is vulnerable to motion of jewels
    //instead should use where things should be
    public double distFrom(float x, float y) {
        //System.out.println(picture.getCenterX() + " " + picture.getCenterY());
        //return Math.hypot(x - picture.getCenterX(), y - picture.getCenterY());
        float[] pos = intToFloatPos(this.col, this.row);
        //System.out.println("+GAB+ " + pos[0] + " " + pos[1]);
        return Math.hypot(x - pos[0], y-pos[1]);
    }

    public Triangle getPicture() {
        return picture;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    public JewelType getType() {
        return j;
    }
    public void setType(int t){
        this.j = JewelType.values()[t];
    }
    public void setType(JewelType t){
        this.j = t;
    }
    public static float[] intToFloatPos(int x, int y){
        float[] toret = {(x-4f)*jewelDist, (y-4f)*jewelDist};
        return toret;
    }
}
