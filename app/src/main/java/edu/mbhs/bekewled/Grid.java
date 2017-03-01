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
            for (int i = 0; i < jewels.length-1; i += 2) {
                for (int j = 0; j < jewels.length; j++) {
                    Jewel dummy = jewels[i + 1][j];
                    jewels[i + 1][j] = jewels[i][j];
                    jewels[i][j] = dummy;
                    jewels[i][j].setpos(i, j);
                    jewels[i + 1][j].setpos(i+1, j);

                }
            }
        }

        System.out.println(xTap + " " + yTap);
        System.out.println();
        Jewel j = getClosestJewelTo(xTap, yTap);
        if (j != null) {
            System.out.println(j.getRow() + " " + j.getCol());
        }
        dealWithSelf();
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
    //This can be done 3X more efficiently with better looping
    public void dealWithSelf(){
        for (int i =0; i<8; i++){
            for(int j=0; j<8; j++){
                int[][][] maches = jewelMatch(i, j);
                if (maches[0] != null || maches[1] != null){
                    matchFall(maches);
                }
            }
        }
    }
    public void matchFall(int[][][] gone){
        Jewel[][] missing = new Jewel[2][];

        //row, col
        if(gone[1].length>0) {
            missing[1] = new Jewel[gone[1].length];
            for (int i = 0; i < gone[1].length; i++) {
                missing[1][i] = jewels[gone[1][i][0]][gone[1][i][1]];
                missing[1][i].setType((int) (Math.random() * 8));

            }
            int j;
            for (j = gone[1][gone[1].length - 1][0] + 1; j < 8 - missing[1].length - 1; j++) {
                jewels[j][gone[1][0][1]] = jewels[j + 1][gone[1][0][1]];
                jewels[j][gone[1][0][1]].setpos(j, gone[1][0][1]);
            }
            int j0 = j;
            for (; j < 8; j++) {
                jewels[j][gone[1][0][1]] = missing[1][j - j0];
                jewels[j][gone[1][0][1]].fallpos(j, gone[1][0][1], 8 + j - j0, gone[1][0][1]);
            }

            for (int i = 0; i < gone[0].length; i++) {
                if (gone[0][i][1] == gone[1][0][1]) {
                    continue;
                }
                missing[0][i] = jewels[gone[0][i][0]][gone[0][i][1]];
                missing[0][i].setType((int) (Math.random() * 8));

            }
        }
        if(gone[0].length>0) {
            int k;
            missing[0] = new Jewel[gone[0].length-(int)Math.pow(0,gone[1].length)];
            for (Jewel jay : missing[0]) {
                int row = jay.getRow();
                int col = jay.getCol();
                for (k = row + 1; k < 8 - 1; k++) {
                    jewels[k][col] = jewels[k + 1][col];
                    jewels[k][col].setpos(k, col);
                }
                jewels[7][col] = jay;
                jay.fallpos(7, col, 8, col);

            }

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
        if(i<0){
            i=0;
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
        if(i<0){
            i=0;
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
