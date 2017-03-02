package edu.mbhs.bekewled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by eytsegay on 2/17/17.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    public static final String[] files = {"koch","sierpinski","carpet","dragon","tree","apolonian", "hilbert","mandlebrot"};
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Triangle[] triangles = new Triangle[16*16];
    private Jewel[][] jewels = new Jewel[8][8];
    private Triangle tri;
    private float[] mRotationMatrix = new float[16];
    private Context contxt;

    private Grid grid;

    private Square square, square1, square2;
    private boolean makeSquare1, makeSquare2;

    float screenWidth, screenHeight;

    public MyGLRenderer(Context c) {
        contxt = c;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        startingAnimation();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //int id = contxt.getResources().getIdentifier("drawable/mandlebrot", null,
        //        contxt.getPackageName());
        Bitmap[] bs = new Bitmap[files.length];
        int[] ids = new int[files.length];
        for (int k = 0; k<files.length; k++){
            ids[k] = contxt.getResources().getIdentifier("drawable/"+files[k], null, contxt.getPackageName());
            bs[k] = BitmapFactory.decodeResource(contxt.getResources(), ids[k]);
        }
        // Temporary create a bitmap
        //Bitmap bmp = BitmapFactory.decodeResource(contxt.getResources(), id);
        tri = new Triangle(1/4., -1/8., -1/8., bs[bs.length-1]);

        for (int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                float x = -1/16+i/8f;
                float y = -1/16+j/8f;
                //triangles[(i+8)*16+j+8] = new Triangle(1/8.0, x, y);
                int type = (int)(Math.random()*files.length);
                jewels[i][j] = new Jewel(type, bs[type], i, j);
            }
        }
        grid = new Grid(jewels, bs);
        //square = new Square(0.55f, 0.4f, -0.95f, -0.7f, 0f, new float[]{1f, 0f, 0f, 1f});

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        System.out.println(width + " " + height);
        GLES20.glViewport(0, 0, width, height);
        if(width < height) {
            float ratio = (float) height / width;
            Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);
        }
        else {
            float ratio = (float) width / height;
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }
        screenWidth = width;
        screenHeight = height;

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawBackground();

        float[] scratch = new float[16];

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f *(((int) time));
        Matrix.setIdentityM(mRotationMatrix, 0);
        //Matrix.setRotateM(mRotationMatrix, 0, 0*angle, 0, 0, -1f);
        //Matrix.translateM(mRotationMatrix, 0, 1f, 0f, 0f);

        //originally 3 was -3. I think I am right but maybe I am wrong and the tutorial was right.
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        tri.draw(scratch);
        for (Jewel[] js : grid.jewels) {
            for (Jewel j : js) {
                j.doMoves();
                j.draw(mMVPMatrix);
            }
        }

        //square = new Square(0.55f, 0.4f, -0.95f, -0.7f, 0f, new float[]{1f, 0f, 0f, 1f});
        //square.draw(mMVPMatrix);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("MyGLRenderer", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void processTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            int go1, go2;
            System.out.println("hello");
            grid.handleTap(2 * (e.getX() / screenWidth - 0.5f), 2 * (e.getY() / screenHeight - 0.5f), this);

            tri.setCenterX(tri.getCenterX()+0.5f);
        }

    }

    public void startingAnimation() {

    }

    public void drawBackground() {
        GLES20.glClearColor(.216f, .643f, .478f, 1f);

        if (makeSquare1) {
            resetSquare1();
            makeSquare1 = false;
        }
        if (makeSquare2) {
            resetSquare2();
            makeSquare2 = false;
        }
        if (square1 != null) {
            square1.draw(mMVPMatrix);
        }
        if (square2 != null) {
            square2.draw(mMVPMatrix);
        }
        //square.draw(mMVPMatrix);
    }

    public void makeSquare1() {
        makeSquare1 = true;
    }

    public void makeSquare2() {
        makeSquare2 = true;
    }

    public void resetSquare1() {
        float extralen = 0.01f;
        float cx = grid.chosenJewel1.getPicture().getCenterX() - extralen;
        float cy = grid.chosenJewel1.getPicture().getCenterY() - extralen;
        float len = grid.chosenJewel1.getPicture().getSidelength() + 2 * extralen;
        System.out.println(grid.chosenJewel1.getRow() + " " + grid.chosenJewel1.getCol());
        System.out.println(cx + " " + cy + " " + len);
        square1 = new Square(cy + len, cy, cx, cx + len, 0f, new float[]{1f, 1f, 0f, 1f});
    }

    public void resetSquare2() {
        float extralen = 0.01f;
        float cx = grid.chosenJewel2.getPicture().getCenterX() - extralen;
        float cy = grid.chosenJewel2.getPicture().getCenterY() - extralen;
        float len = grid.chosenJewel2.getPicture().getSidelength() + 2 * extralen;
        square2 = new Square(cy + len, cy, cx, cx + len, 0f, new float[]{1f, 1f, 0f, 1f});
    }

    public void clearSquares() {
        square1 = null;
        square2 = null;
    }
}
