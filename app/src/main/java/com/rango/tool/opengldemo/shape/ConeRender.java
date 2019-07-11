package com.rango.tool.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ConeRender implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 position;" +
                    "uniform mat4 matrix;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    "gl_Position = matrix*position;" +
                    "if(position.z != 0.0){" +
                    "vColor=vec4(0.0,0.0,0.0,1.0);" +
                    "}else{" +
                    "vColor=vec4(0.9,0.9,0.9,1.0);" +
                    "}" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    "gl_FragColor = vColor;" +
                    "}";

    private static float[] circlePointers;

    /**
     * 三角函数值的单位应该Math.PI，如：Math.sin(i * Math.PI / 180f)；
     *
     * @return
     */
    private float[] createPositions() {
        ArrayList<Float> pointers = new ArrayList<>();
        pointers.add(0f);
        pointers.add(0f);
        pointers.add(2f);

        float span = 360f / 360;
        float radius = 0.5f;
        for (float i = 0f; i < 360 + span; i += span) {
            pointers.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pointers.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pointers.add(0f);
        }

        float[] circlePointers = new float[pointers.size()];
        for (int i = 0; i < pointers.size(); i++) {
            circlePointers[i] = pointers.get(i);
        }
        return circlePointers;
    }

    private FloatBuffer pointerBuffer;
    private int program;

    private float[] frustumMatrix = new float[16];
    private float[] lookMatrix = new float[16];
    private float[] matrix = new float[16];

    public ConeRender() {
        circlePointers = createPositions();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(circlePointers.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        pointerBuffer = byteBuffer.asFloatBuffer();
        pointerBuffer.put(circlePointers);
        pointerBuffer.position(0);

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
        Matrix.frustumM(frustumMatrix, 0, -rate, rate, -1, 1, 3, 20);
        Matrix.setLookAtM(lookMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, pointerBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, circlePointers.length / 3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
