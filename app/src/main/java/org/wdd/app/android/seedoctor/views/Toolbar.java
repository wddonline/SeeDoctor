package org.wdd.app.android.seedoctor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by richard on 11/29/16.
 */

public class Toolbar extends android.support.v7.widget.Toolbar {

    private Paint paint;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.parseColor("#e0e0e0"));
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = getMeasuredHeight() - 2;
        canvas.drawLine(0, y, getMeasuredWidth(), y, paint);
    }
}
