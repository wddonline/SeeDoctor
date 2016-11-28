package org.wdd.app.android.seedoctor.views;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.wdd.app.android.seedoctor.http.impl.VolleyTool;

/**
 * Created by richard on 11/28/16.
 */

public class HttpImageView extends NetworkImageView {

    public HttpImageView(Context context) {
        this(context, null);
    }

    public HttpImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HttpImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url) {
//        ImageLoader imageLoader = new ImageLoader(VolleyTool.getInstance(getContext()), );
//        super.setImageUrl(url, imageLoader);
    }
}
