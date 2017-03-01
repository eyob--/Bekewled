package edu.mbhs.bekewled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.text.TextUtils;
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
    public static Bitmap[] bs;
    float screenWidth, screenHeight;

    public MyGLRenderer(Context c) {
        contxt = c;
        loadTex();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        startingAnimation();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //int id = contxt.getResources().getIdentifier("drawable/mandlebrot", null,
        //        contxt.getPackageName());
       /* Bitmap[] bs = new Bitmap[files.length];
        int[] ids = new int[files.length];
        for (int k = 0; k<files.length; k++){
            ids[k] = contxt.getResources().getIdentifier("drawable/"+files[k], null, contxt.getPackageName());
            bs[k] = BitmapFactory.decodeResource(contxt.getResources(), ids[k]);
        }
        // Temporary create a bitmap
        //Bitmap bmp = BitmapFactory.decodeResource(contxt.getResources(), id);
*/
        int type;
        for (int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                float x = -1/16+i/8f;
                float y = -1/16+j/8f;
                //triangles[(i+8)*16+j+8] = new Triangle(1/8.0, x, y);
                type = (int)(Math.random()*files.length);

                jewels[i][j] = new Jewel(type, i, j);
            }
        }
        grid = new Grid(jewels);
       // tri = new Triangle(1/4., -1/8., -1/8., bs[3]);
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
        //tri.draw(scratch);
        for (Jewel[] js : grid.jewels) {
            for (Jewel j : js) {
                j.doMoves();
                j.draw(mMVPMatrix);
            }
        }
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void processTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            int go1, go2;
            System.out.println("hello");
            grid.handleTap(2 * (e.getX() / screenWidth - 0.5f), 2 * (e.getY() / screenHeight - 0.5f));

//            tri.setCenterX(tri.getCenterX()+0.5f);
        }

    }
    public void loadTex() {
        bs = new Bitmap[files.length];
        int[] ids = new int[files.length];
        for (int k = 0; k < files.length; k++) {
            ids[k] = contxt.getResources().getIdentifier("drawable/" + files[k], null, contxt.getPackageName());
            bs[k] = BitmapFactory.decodeResource(contxt.getResources(), ids[k]);
        }

    }
    public void startingAnimation() {

    }
}
