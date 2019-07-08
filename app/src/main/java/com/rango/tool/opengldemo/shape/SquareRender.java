package com.rango.tool.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareRender implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 position;" +
                    "uniform mat4 matrix;" +
                    "void main(){" +
                    "gl_Position = matrix*position;" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 color;" +
                    "void main(){" +
                    "gl_FragColor = color;" +
                    "}";

    private static float squareCoords[] = new float[]{
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0,
    };

    private static short index[] = new short[]{
            0, 1, 2, 0, 2, 3
    };

    private static final float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private FloatBuffer coordsBuffer;
    private ShortBuffer indexBuffer;
    private int program;

    private float[] lookMatrix = new float[16];
    private float[] frustumMatrix = new float[16];
    private float[] matrix = new float[16];


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        coordsBuffer = byteBuffer.asFloatBuffer();
        coordsBuffer.put(squareCoords);
        coordsBuffer.position(0);

        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(index.length * 2);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = indexByteBuffer.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, VERTEX_SHADER_CODE);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, FRAGMENT_SHADER_CODE);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float rate = width / (float) height;
        Matrix.setLookAtM(lookMatrix, 0, 0, 0, 15, 0, 0, 0, 0, 1, 0);
        Matrix.frustumM(frustumMatrix, 0, -rate, rate, -1, 1, 8, 19);
        Matrix.multiplyMM(matrix, 0, frustumMatrix, 0, lookMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(program);

        int matrixHandle = GLES20.glGetUniformLocation(program, "matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, coordsBuffer);

        int colorHandle = GLES20.glGetUniformLocation(program, "color");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
