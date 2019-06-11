package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rango.tool.opengldemo.R;

public abstract class BaseOpenGlActivity extends AppCompatActivity {

    abstract GLSurfaceView.Renderer getRenderer();

    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_layout);
        glSurfaceView = findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(getRenderer());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
