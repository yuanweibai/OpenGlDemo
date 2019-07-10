package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.CubeRender;
import com.rango.tool.opengldemo.shape.TestRender;

public class CubeActivity extends BaseOpenGlActivity {

    @Override
    GLSurfaceView.Renderer getRenderer() {
        return new CubeRender();
    }
}
