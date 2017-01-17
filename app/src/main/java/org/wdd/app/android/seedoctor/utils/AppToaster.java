package org.wdd.app.android.seedoctor.utils;

import android.widget.Toast;

import org.wdd.app.android.seedoctor.app.SDApplication;

/**
 * Created by wangdd on 16-11-27.
 */

public class AppToaster {

    public static void show(int res) {
        Toast.makeText(SDApplication.getInstance(), SDApplication.getInstance().getText(res), Toast.LENGTH_SHORT).show();
    }

    public static void show(String txt) {
        Toast.makeText(SDApplication.getInstance(), txt, Toast.LENGTH_SHORT).show();
    }
}
