package edu.mbhs.bekewled;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by eytsegay on 2/26/17.
 */

public class Grid {

    Jewel[][] jewels;
    public Bitmap[] pics;
    public Grid(Jewel[][] grid) {
        jewels = grid;
    }
    public Grid(Jewel[][] grid, Bitmap[] bs) {
        jewels = grid;
        pics = bs;
    }
    public void handleTap(float xTap, float yTap) {
        if (!(jewels[0][0].getPicture().movingHoriz() || jewels[0][0].getPicture().movingVert())) {
            for (int i = 0; i < jewels.length - 1; i += 2) {
                for (int j = 0; j < jewels.length; j++) {
                    jewels[i][j].dontMove(i + 1, j);
                    jewels[i + 1][j].dontMove(i, j);
                }
            }
        }

        System.out.println(xTap + " " + yTap);
        System.out.println();
        Jewel j = getClosestJewelTo(xTap, yTap);
        if (j != null) {
            System.out.println(j.getRow() + " " + j.getCol());
        }
    }

    public Jewel getClosestJewelTo(float x, float y) {
        Jewel closest = null;
        double minDist = Double.MAX_VALUE;
        double dist;
        for (Jewel[] row : jewels) {
            for (Jewel j : row) {
                dist = j.distFrom(x, y);
                if (dist < minDist) {
                    minDist = dist;
                    closest = j;
                }
            }
        }
        System.out.println(minDist);
        if (minDist > 0.25) { // adjust this value, supposed to handle border jewels where you're not really touching anything
            closest = null;
        }
        return closest;
    }
    //bad name
    //called when the user tries a match
    //probably called out of handle tap
    public void userMatch(int row1, int col1, int row2, int col2){
        
        
    }
    public void matchFall(int[][] gone){
        for (int[] ori:gone){

        }

    }
    public int[][][] jewelMatch(int row, int col){
        Jewel.JewelType type = jewels[row][col].getType();
        ArrayList<int[]> horiz = new ArrayList<int[]>();
        ArrayList<int[]> vert = new ArrayList<int[]>();

        int i = row;
        for (; i>=0; i--){
            Jewel g = jewels[i][col];
            if (g.getType()!= type){
                break;
            }
        }
        for (; i<8; i++){
            Jewel g = jewels[i][col];
            if (g.getType() == type){
                vert.add(new int[]{i,col});
            }
            else{
                break;
            }
        }

        i = col;
        for (; i>=0; i--){
            Jewel g = jewels[row][i];
            if (g.getType()!= type){
                break;
            }
        }
        for (; i<8; i++){
            Jewel g = jewels[row][i];
            if (g.getType() == type){
                vert.add(new int[]{row,i});
            }
            else{
                break;
            }
        }
        int[][][] toRet = new int[2][][];
        toRet[0] = null;
        toRet[1] = null;
        if (vert.size() >= 3){
            int[][] a = new int[vert.size()][];
            int place = 0;
            for (int[] k : vert){
                a[place] = k;
                place++;
            }
            toRet[1] = a;
        }
        if (horiz.size() >= 3){
            int[][] a = new int[horiz.size()][];
            int place = 0;
            for (int[] k : horiz){
                a[place] = k;
                place++;
            }
            toRet[0] = a;
        }
        return toRet;
    }

}
