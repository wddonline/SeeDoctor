package org.wdd.app.android.seedoctor.ui.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

public class NewsDetailActivity extends BaseActivity {

    public static void show(Activity context, String id, String titile) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", titile);
        context.startActivity(intent);
    }

    private WebView webView;
    private ProgressBar progressBar;

    private String id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initTitles();
        initData();
        initViews();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_news_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titileView = (TextView) findViewById(R.id.activity_news_detail_title);
        titileView.setText(title);
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.activity_web_webview);
        progressBar = (ProgressBar) findViewById(R.id.activity_web_progress);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSupportZoom(true); // 支持缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

//                //编写 javaScript方法
//                String javascript =  "javascript:function hideOther() {" +
//                        "document.getElementsByClassName('news-func-item news-ask-doctor')[0].style.display='none';" +
//                        ";}";
//
//                //创建方法
//                view.loadUrl(javascript);
            }
        });

        String url = ServiceApi.NEWS_DETAIL;
        url = String.format(url, id, AppUtils.getScreenWidth(this));
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
