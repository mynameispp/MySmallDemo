package com.ffzxnet.mysmall.lib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 创建者： feifan.pi 在 2017/4/10.
 */

public class ToasUtil {
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
