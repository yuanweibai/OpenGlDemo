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

public class LightBallRender implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "uniform mat4 vMatrix;" +
                    "attribute vec3 vPosition; " +
                    "varying vec4 vDiffuse;" +
                    "vec4 pointLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){" +
                    "    vec3 newTarget=normalize((vMatrix*vec4(normal+vPosition,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz);" +
                    "    vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);" +
                    "    return lightDiffuse*max(0.0,dot(newTarget,vp));" +
                    "}" +
                    "void main(){" +
                    "   gl_Position = vMatrix * vec4(vPosition,1);" +
                    "   vec4 at=vec4(1.0,1.0,1.0,1.0); " +
                    "   vec3 pos=vec3(100.0,80.0,80.0); " +
                    "   vDiffuse=pointLight(normalize(vPosition),pos,at);" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "varying vec4 vDiffuse;" +
                    "void main(){" +
                    "   vec4 finalColor=vec4(1.0);" +
                    "   gl_FragColor=finalColor*vDiffuse+finalColor*vec4(0.15,0.15,0.15,1.0);" +
                    "}";

    private static float[] circlePointers;

    /**
     * 三角函数值的单位应该Math.PI，如：Math.sin(i * Math.PI / 180f)；
     *
     * @return
     */
    private float[] createPositions() {
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;
        float step = 5f;
        for (float i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float) Math.sin(i * Math.PI / 180.0);
            h2 = (float) Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    private FloatBuffer pointerBuffer;
    private int program;

    private float[] frustumMatrix = new float[16];
    private float[] lookMatrix = new float[16];
    private float[] matrix = new float[16];

    public LightBallRender() {
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
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(frustumMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(lookMatrix, 0, 0.0f, 0.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(matrix, 0, frustumMatrix, 0, lookMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(program);
        int matrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, pointerBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, circlePointers.length / 3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
