package org.wdd.app.android.seedoctor.ui.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.builder.BannerAdsBuilder;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.news.presenter.NewsDetailPresenter;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.utils.Constants;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

public class NewsDetailActivity extends BaseActivity {

    public static void show(Activity context, String id, String image, String titile) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("image", image);
        intent.putExtra("title", titile);
        context.startActivity(intent);
    }

    public static void showForResult(Activity activity, String id, String image, String title, int requsetCode) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("image", image);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent, requsetCode);
    }

    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;
    private BannerAdsBuilder adsBuilder;

    private NewsDetailPresenter presenter;
    private String id;
    private String image;
    private String title;
    private boolean initCollectStatus = false;
    private boolean currentCollectStatus = initCollectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");

        presenter = new NewsDetailPresenter(this);
    }

    private void initTitles() {
        toolbar = (Toolbar) findViewById(R.id.activity_news_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_collection_do:
                        presenter.collectNews(id, image, title);
                        return true;
                    case R.id.menu_collection_undo:
                        presenter.uncollectNews(id);
                        return true;
                }
                return false;
            }
        });

        TextView titileView = (TextView) findViewById(R.id.activity_news_detail_title);
        titileView.setText(title);
    }

    private void initViews() {
        ViewGroup adsView = (ViewGroup) findViewById(R.id.activity_news_detail_ads_view);
        adsBuilder = new BannerAdsBuilder(this, adsView, Constants.NEWS_DETAIL_AD_ID, true);
        webView = (WebView) findViewById(R.id.activity_news_detail_webview);
        progressBar = (ProgressBar) findViewById(R.id.activity_news_detail_progress);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSupportZoom(true); // 支持缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String javascript =  "javascript:function hideOther() {" +
                        "document.getElementsByClassName('news-func-item news-ask-doctor')[0].remove();" +
                        "document.getElementsByClassName('cy-dl sn-dl')[0].remove();" +
                        "document.getElementsByClassName('share-to')[0].remove();" +
                        "document.getElementsByClassName('more-comments')[0].remove();" +
                        "}";
                view.loadUrl(javascript);
                view.loadUrl("javascript:hideOther();");
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                if (adsBuilder.isAdded()) return;
                adsBuilder.addBannerAds();
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
        } else {
            backAction();
        }
        super.onBackPressed();
    }

    private void backAction() {
        if (currentCollectStatus != initCollectStatus) {
            Intent intent = new Intent();
            intent.putExtra("id", id);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        presenter.getCollectionStatus(id);
        return true;
    }

    public void setNewsCollectionViews(boolean isCollected) {
        initCollectStatus = isCollected;
        currentCollectStatus = isCollected;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(!isCollected);
        menu.findItem(R.id.menu_collection_undo).setVisible(isCollected);
    }

    public void updateNewsCollectedStatus(boolean success) {
        currentCollectStatus = true;
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(false);
        menu.findItem(R.id.menu_collection_undo).setVisible(true);
    }

    public void updateNewsUncollectedStatus(boolean success) {
        currentCollectStatus = false;
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(true);
        menu.findItem(R.id.menu_collection_undo).setVisible(false);
    }
}
