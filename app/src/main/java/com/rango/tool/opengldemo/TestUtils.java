package com.rango.tool.opengldemo;

import com.rango.tool.opengldemo.shape.activity.OtherShapeActivity;

public class TestUtils {

    private static final boolean IS_TEST = true;

    public static Class getTestActivityClass() {
        if (!IS_TEST) {
            return null;
        }
        return OtherShapeActivity.class;
    }
}
