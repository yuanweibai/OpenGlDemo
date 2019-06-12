package com.rango.tool.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class IsoscelesTriangleRender implements GLSurfaceView.Renderer {

    private final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    " gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    " gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    private static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    //设置颜色，依次为红绿蓝和透明通道
    private float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    private FloatBuffer vertexBuffer;
    private int progrom;
    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] matrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

        progrom = GLES20.glCreateProgram();
        GLES20.glAttachShader(progrom, vertexShader);
        GLES20.glAttachShader(progrom, fragmentShader);
        GLES20.glLinkProgram(progrom);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width / (float) height;
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 3, 19);
//        Matrix.orthoM(projectMatrix, 0, -ratio, ratio, -1, 1, 4, 12);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 12f, 0, 0, 0, 0, 1f, 0f);
        Matrix.multiplyMM(matrix, 0, projectMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(progrom);

        int vertexMatrix = GLES20.glGetUniformLocation(progrom, "vMatrix");
        GLES20.glUniformMatrix4fv(vertexMatrix, 1, false, matrix, 0);

        int vertexPosition = GLES20.glGetAttribLocation(progrom, "vPosition");
        GLES20.glEnableVertexAttribArray(vertexPosition);
        GLES20.glVertexAttribPointer(vertexPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);


        int fragmentColor = GLES20.glGetUniformLocation(progrom, "vColor");
        GLES20.glUniform4fv(fragmentColor, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(vertexPosition);
    }

    private int loadShader(int type, String code) {
        int id = GLES20.glCreateShader(type);
        GLES20.glShaderSource(id, code);
        GLES20.glCompileShader(id);
        return id;
    }
}
