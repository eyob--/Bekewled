package edu.mbhs.bekewled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by eytsegay on 2/17/17.
 */

public class Triangle {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "   gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";
    public static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";
    private int mProgram;

    private int mPositionHandle, mColorHandle, mMVPMatrixHandle;


    private final int vertexStride = COORDS_PER_VERTEX * 4;

    FloatBuffer vertexBuffer;
    public FloatBuffer uvBuffer;
    private ShortBuffer drawListBuffer;
    static final int COORDS_PER_VERTEX = 3;



    /*private float[] baseTriangleCoords = {
                0.0f, 0.622f, 0.0f,
                -0.5f, -0.311f, 0.0f,
                0.5f, 0.311f, 0.0f
        };*/
    private float[] baseTriangleCoords = {
        0f, 0f, 0f,
        1f, 0f, 0f,
        1f, 1f, 0f,
        0f, 1f, 0f

    };
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
   // indices = new short[] {0, 1, 2, 0, 2, 3};
    private float centerX = 0;
    private float centerY = 0;
    private float scale = 1;
    private float[] triangleCoords = baseTriangleCoords;

    private float destX = Float.NaN;
    private float destY = Float.NaN;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    float[] color = {0.637f, 0.770f, 0.223f, 1.0f};
    private float moveInc = 0.1f;

    public Triangle() {
        setupPos();
        setupShad();
    }
    private void setupPos(){
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
    private void setupShad(){
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vs_Image);
        fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fs_Image);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }
    private void setupImage(Bitmap bmp){
        float[] uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);


        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
    }
    public Triangle(double ex, double wy) {
        float x = (float) ex;
        float y = (float) wy;
        this.setCenter(x, y);

        setupShad();
    }
    public Triangle(double ex, double wy, Bitmap b) {
        float x = (float) ex;
        float y = (float) wy;
        this.setCenter(x, y);

        setupShad();
        setupImage(b);
    }
    public Triangle(double s, double ex, double wy){
        this.setScale((float) s);
        float x = (float) ex;
        float y = (float) wy;
        this.setCenter(x, y);

        setupShad();

    }
    public Triangle(double s, double ex, double wy, Bitmap b){
        this.setScale((float) s);
        float x = (float) ex;
        float y = (float) wy;
        this.setCenter(x, y);

        setupShad();
        setupImage(b);
    }
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);



        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false, 0, uvBuffer);



        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);


        int mSamplerLoc = GLES20.glGetUniformLocation (mProgram,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);


        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float x) {
        setCenter(x, centerY);
    }
    public void setCenter(float cx, float cy) {
        this.centerX = cx;
        this.centerY = cy;
        for (int i = 0; i < triangleCoords.length; i += 3){
            triangleCoords[i] = baseTriangleCoords[i]*scale + cx;
            triangleCoords[i+1] = baseTriangleCoords[i+1]*scale + cy;
        }
        this.setupPos();
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float y) {
        setCenter(centerX, y);
    }
    // This is handled so terribly. Shapes shouldn't be made like this
    public void setScale(float s){
        this.scale = s;
    }

    public void setDest(float x, float y){
        destX = x;
        destY = y;
    }
    public boolean movingHoriz(){
        if (destX == Float.NaN){
            return false;
        }
        if ((destX-centerX)*(destX-centerX)<moveInc){
            setCenterX(destX);
            destX = Float.NaN;
            return false;
        }
        return true;
    }
    public boolean movingVert(){
        if (destY == Float.NaN){
            return false;
        }
        if ((destY-centerY)*(destY-centerY)<moveInc){
            setCenterY(destY);
            destY = Float.NaN;
            return false;
        }
        return true;
    }
    public boolean moveHoriz(){
        if (!movingHoriz()){
            return false;
        }

        setCenterX()

        return true;
    }

}
