package edu.mbhs.bekewled;

/**
 * Created by eytsegay on 2/26/17.
 */

public class Grid {

    Jewel[][] jewels;

    public Grid(Jewel[][] grid) {
        jewels = grid;
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

}
