package com.rango.tool.opengldemo;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rango.tool.opengldemo.shape.TriangleRender;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGlSurfaceView = findViewById(R.id.gl_surface_view);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(new TriangleRender());
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }
}
