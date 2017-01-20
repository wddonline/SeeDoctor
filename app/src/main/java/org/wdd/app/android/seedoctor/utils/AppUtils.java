package org.wdd.app.android.seedoctor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by richard on 1/9/17.
 */

public class AppUtils {

    public static boolean isFragmentAvaliable(Fragment fragment) {
        if (fragment == null) return false;
        if (!fragment.isAdded()) return false;
        Activity hostActivity = fragment.getActivity();
        if (!isActivityAvaliable(hostActivity)) return false;
        return true;
    }

    public static boolean isActivityAvaliable(Activity activity) {
        if (activity == null) return false;
        if (activity.isFinishing()) return false;
        return true;
    }

    public static void clipText(Context context, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager cmb = (android.content.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(text);
        } else {
            android.text.ClipboardManager cmb = (android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(text);
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
