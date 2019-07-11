package com.rango.tool.opengldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rango.tool.opengldemo.shape.activity.BallActivity;
import com.rango.tool.opengldemo.shape.activity.CircleActivity;
import com.rango.tool.opengldemo.shape.activity.ColorsTriangleActivity;
import com.rango.tool.opengldemo.shape.activity.ConeActivity;
import com.rango.tool.opengldemo.shape.activity.CubeActivity;
import com.rango.tool.opengldemo.shape.activity.CylinderActivity;
import com.rango.tool.opengldemo.shape.activity.IsoscelesTriangleActivity;
import com.rango.tool.opengldemo.shape.activity.LightBallActivity;
import com.rango.tool.opengldemo.shape.activity.SquareActivity;
import com.rango.tool.opengldemo.shape.activity.TriangleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.triangle_btn).setOnClickListener(v -> startActivity(TriangleActivity.class));
        findViewById(R.id.isosceles_triangle_btn).setOnClickListener(v -> startActivity(IsoscelesTriangleActivity.class));
        findViewById(R.id.colors_triangle_btn).setOnClickListener(v -> startActivity(ColorsTriangleActivity.class));
        findViewById(R.id.square_btn).setOnClickListener(v -> startActivity(SquareActivity.class));
        findViewById(R.id.circle_btn).setOnClickListener(v -> startActivity(CircleActivity.class));
        findViewById(R.id.cube_btn).setOnClickListener(v -> startActivity(CubeActivity.class));
        findViewById(R.id.cone_btn).setOnClickListener(v -> startActivity(ConeActivity.class));
        findViewById(R.id.cylinder_btn).setOnClickListener(v -> startActivity(CylinderActivity.class));
        findViewById(R.id.ball_btn).setOnClickListener(v -> startActivity(BallActivity.class));
        findViewById(R.id.light_ball_btn).setOnClickListener(v -> startActivity(LightBallActivity.class));

        Class cl = TestUtils.getTestActivityClass();
        if (cl != null) {
            startActivity(cl);
        }
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(MainActivity.this, c);
        startActivity(intent);
    }

}
