package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.ColorsTriangleRender;

public class ColorsTriangleActivity extends BaseOpenGlActivity {

    @Override
    GLSurfaceView.Renderer getRenderer() {
        return new ColorsTriangleRender();
    }
}
