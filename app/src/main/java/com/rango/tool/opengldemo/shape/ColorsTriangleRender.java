package com.rango.tool.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ColorsTriangleRender implements GLSurfaceView.Renderer {

    private static final String VERTEX_CODE =
            "attribute vec4 position;" +
                    "attribute vec4 aColor;" +
                    "varying vec4 color;" +
                    "uniform mat4 matrix;" +
                    "void main() {" +
                    "gl_Position = matrix * position;" +
                    "color = aColor;" +
                    "}";

    private static final String FRAGMENT_CODE =
            "precision mediump float;" +
                    "varying vec4 color;" +
                    "void main() {" +
                    "gl_FragColor = color;" +
                    "}";

    private static float triangleCoords[] = {
            0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };

    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    private int program;
    private FloatBuffer floatBuffer;
    private FloatBuffer colorBuffer;

    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] matrix = new float[16];


    public ColorsTriangleRender() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(triangleCoords);
        floatBuffer.position(0);

        ByteBuffer colorByte = ByteBuffer.allocateDirect(color.length * 4);
        colorByte.order(ByteOrder.nativeOrder());

        colorBuffer = colorByte.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        int vertex = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertex, VERTEX_CODE);
        GLES20.glCompileShader(vertex);

        int fragment = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragment, FRAGMENT_CODE);
        GLES20.glCompileShader(fragment);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex);
        GLES20.glAttachShader(program, fragment);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width / (float) height;
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 4, 15);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 10, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(matrix, 0, projectMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(program);

        int positionHandle = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, floatBuffer);

        int colorHandle = GLES20.glGetAttribLocation(program, "aColor");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        int matrixHandle = GLES20.glGetUniformLocation(program, "matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
