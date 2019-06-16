package com.rango.tool.opengldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rango.tool.opengldemo.shape.activity.ColorsTriangleActivity;
import com.rango.tool.opengldemo.shape.activity.IsoscelesTriangleActivity;
import com.rango.tool.opengldemo.shape.activity.OtherShapeActivity;
import com.rango.tool.opengldemo.shape.activity.TriangleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.triangle_btn).setOnClickListener(v -> startActivity(TriangleActivity.class));
        findViewById(R.id.isosceles_triangle_btn).setOnClickListener(v -> startActivity(IsoscelesTriangleActivity.class));
        findViewById(R.id.colors_triangle_btn).setOnClickListener(v -> startActivity(ColorsTriangleActivity.class));
        findViewById(R.id.square_btn).setOnClickListener(v -> startActivity(OtherShapeActivity.class));

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
