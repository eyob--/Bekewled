package edu.mbhs.bekewled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Triangle[] triangles = new Triangle[16*16];
    private Jewel[][] jewels = new Jewel[8][8];
    private Triangle tri;
    private float[] mRotationMatrix = new float[16];
    private Context contxt;
    public MyGLRenderer(Context c) {
        contxt = c;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int id = contxt.getResources().getIdentifier("drawable/mandy_icon", null,
                contxt.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(contxt.getResources(), id);
        tri = new Triangle(1/4., -1/8., -1/8., bmp);

        for (int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                float x = -1/16+i/8f;
                float y = -1/16+j/8f;
                //triangles[(i+8)*16+j+8] = new Triangle(1/8.0, x, y);
                jewels[i][j] = new Jewel(1, bmp, i, j);
            }
        }


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
        tri.draw(scratch);
        for (Jewel[] js : jewels) {
            for (Jewel j : js) {
                //if (time == 5L){
                    j.doMoves();
                //}
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
            for (int i = 0; i < jewels.length - 1; i += 2) {
                for (int j = 0; j < jewels.length; j++) {
                    jewels[i][j].dontMove(i + 1, j);
                    jewels[i + 1][j].dontMove(i, j);
                }
            }
            for (float f : tri.getTriangleCoords()) System.out.print(f + " ");
            System.out.println();
            System.out.println(tri.getCenterX()+"sa");
            tri.setCenterX(tri.getCenterX()+0.5f);
            System.out.println(tri.getCenterX()+"as");
            for (float f : tri.getTriangleCoords()) System.out.print(f + " ");
            System.out.println();
        }

    }
}
