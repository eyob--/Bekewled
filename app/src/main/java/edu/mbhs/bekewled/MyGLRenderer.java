package edu.mbhs.bekewled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

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
        tri = new Triangle(1/4.0, 0, 0, bmp);

        /*for (int i = -8; i<8; i++){
            for(int j = -8; j<8; j++){
                float x = -1/16+i/8f;
                float y = -1/16+j/8f;
                triangles[(i+8)*16+j+8] = new Triangle(1/8.0, x, y);

            }
        }*/

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float[] scratch = new float[16];

      /*  long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f *(((int) time));
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1f);*/

        //originally 3 was -3. I think I am right but maybe I am wrong and the tutorial was right.
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

       // Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        tri.draw(mMVPMatrix);
       /* for (Triangle t : triangles) {
            t.draw(mMVPMatrix);
        }*/
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
