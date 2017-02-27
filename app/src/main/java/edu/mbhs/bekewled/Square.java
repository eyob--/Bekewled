package edu.mbhs.bekewled;

/**
 * Created by eytsegay on 2/27/17.
 */

public class Square extends Shape {

    public Square(float top, float bottom, float left, float right, float z, float[] color) {
        super(
                new float[]{
                        left, top, z,
                        left, bottom, z,
                        right, bottom, z,
                        right, top, z
                },
                new short[]{0, 1, 2, 0, 2, 3},
                color
        );
    }

}
