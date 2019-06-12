package com.rango.tool.opengldemo;

import com.rango.tool.opengldemo.shape.activity.ColorsTriangleActivity;

public class TestUtils {

    private static final boolean IS_TEST = false;

    public static Class getTestActivityClass() {
        if (!IS_TEST) {
            return null;
        }
        return ColorsTriangleActivity.class;
    }
}
