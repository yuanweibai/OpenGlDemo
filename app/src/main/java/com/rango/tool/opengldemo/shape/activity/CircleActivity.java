package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.CircleRender;

public class CircleActivity extends BaseOpenGlActivity {

    @Override GLSurfaceView.Renderer getRenderer() {
        return new CircleRender();
    }
}
