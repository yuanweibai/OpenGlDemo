package com.rango.tool.opengldemo.shape;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRender implements GLSurfaceView.Renderer {

    private static final String VERTEX_CODE =
            "attribute vec4 position;" +
                    "uniform mat4 matrix;" +
                    "attribute vec4 color;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    "gl_Position = matrix * position;" +
                    "vColor = color;" +
                    "}";

    private static final String FRAGMENT_CODE =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    "gl_FragColor = vColor;" +
                    "}";

    private static final float value = 0.5f;
    private static final float pointers[] = {
            -value, value, value,
            -value, value, -value,
            -value, -value, value,
            -value, -value, -value,
            value, value, value,
            value, value, -value,
            value, -value, value,
            value, -value, -value
    };

    private static final short indexs[] = {
            0, 1, 3, 1, 3, 2,
            0, 4, 6, 0, 6, 2,
            4, 6, 7, 4, 7, 5,
            5, 1, 3, 5, 7, 3,
            0, 4, 5, 0, 5, 1,
            2, 6, 7, 2, 7, 3
    };

    private static final float color[] = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private int program;
    private FloatBuffer pointerBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    private float[] frustumMatrix = new float[16];
    private float[] lookMatrix = new float[16];
    private float[] matrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        ByteBuffer pointerByteBuffer = ByteBuffer.allocateDirect(pointers.length * 4);
        pointerByteBuffer.order(ByteOrder.nativeOrder());
        pointerBuffer = pointerByteBuffer.asFloatBuffer();
        pointerBuffer.put(pointers);
        pointerBuffer.position(0);

        ByteBuffer colorByteBuffer = ByteBuffer.allocateDirect(color.length * 4);
        colorByteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = colorByteBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(indexs.length * 2);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = indexByteBuffer.asShortBuffer();
        indexBuffer.put(indexs);
        indexBuffer.position(0);

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, VERTEX_CODE);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, FRAGMENT_CODE);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float rate = width / (float) height;
        Matrix.frustumM(frustumMatrix, 0, -rate, rate, -1, 1, 9, 18);
        Matrix.setLookAtM(lookMatrix, 0, 5f, 5f, 12, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(matrix, 0, frustumMatrix, 0, lookMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);

        int matrixHandle = GLES20.glGetUniformLocation(program, "matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, pointerBuffer);

        int colorHandle = GLES20.glGetAttribLocation(program, "color");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexs.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
