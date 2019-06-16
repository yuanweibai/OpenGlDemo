package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.OtherShapeRender;

public class OtherShapeActivity extends BaseOpenGlActivity {

    @Override
    GLSurfaceView.Renderer getRenderer() {
        return new OtherShapeRender();
    }
}
