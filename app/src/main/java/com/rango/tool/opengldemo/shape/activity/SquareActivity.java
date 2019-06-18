package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.SquareRender;

public class SquareActivity extends BaseOpenGlActivity {

    @Override
    GLSurfaceView.Renderer getRenderer() {
        return new SquareRender();
    }
}
