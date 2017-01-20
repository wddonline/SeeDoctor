package org.wdd.app.android.seedoctor.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by richard on 1/20/17.
 */

public class NumberUtils {

    public static double b2mb(long b) {
        return b / 1000f / 1000f;
    }

    public static String formatFloatString(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);
        return nf.format(d);
    }
}
