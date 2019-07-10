package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.CircleRender;
import com.rango.tool.opengldemo.shape.ConeRender;

public class ConeActivity extends BaseOpenGlActivity {

    @Override GLSurfaceView.Renderer getRenderer() {
        return new ConeRender();
    }
}
