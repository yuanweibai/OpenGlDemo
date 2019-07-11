package com.rango.tool.opengldemo.shape.activity;

import android.opengl.GLSurfaceView;

import com.rango.tool.opengldemo.shape.CylinderRender;

public class CylinderActivity extends BaseOpenGlActivity {

    @Override
    GLSurfaceView.Renderer getRenderer() {
        return new CylinderRender();
    }
}
