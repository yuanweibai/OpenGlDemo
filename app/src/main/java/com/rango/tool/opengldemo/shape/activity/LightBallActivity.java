package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.LightBallRender;

public class LightBallActivity extends BaseOpenGlActivity {

    @Override GLSurfaceView.Renderer getRenderer() {
        return new LightBallRender();
    }
}
