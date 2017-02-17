package edu.mbhs.bekewled;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by eytsegay on 2/17/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MyGLRenderer();
        setRenderer(renderer);
    }

}