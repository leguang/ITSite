package cn.itsite.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.bean.NewsData;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.LocalDisplay;
import cn.itsite.view.MetaballView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class NewsDetailActivity extends BaseActivity {

    public WebView wv_news;
    private ImageButton ib_comments;
    private ImageButton ib_more;
    private EditText tv_input;
    private MetaballView pb_progress;
    public WebSettings settings;
    public NewsData newsData;
    private LinearLayout ll_news_detail;
    private StoreHouseHeader header;
    private PtrFrameLayout ptrframe;

    int downY = 0;
    int moveY = 0;
    int dY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        newsData = (NewsData) getIntent().getSerializableExtra("newsData");
        initView();
        initData();
    }

    private void initView() {

        header = new StoreHouseHeader(this);
        ptrframe = (PtrFrameLayout) findViewById(R.id.store_house_ptr_frame_news_detail);
        wv_news = (WebView) findViewById(R.id.wv_news);
        ib_comments = (ImageButton) findViewById(R.id.ib_comments);
        ll_news_detail = (LinearLayout) findViewById(R.id.ll_news_detail);
        ib_more = (ImageButton) findViewById(R.id.ib_more);
        tv_input = (EditText) findViewById(R.id.tv_input);
        pb_progress = (MetaballView) findViewById(R.id.metaball);
    }

    private void initData() {
        final String[] mStringList = {ConstantsUtils.DOMAIN_1, ConstantsUtils.DOMAIN_2};

        // header
        header.setTextColor(Color.BLACK);
        header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);

        /**
         * using a string, support: A-Z 0-9 - . you can add more letters by
         * {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        header.initWithString(mStringList[0]);
        // for changing string
        ptrframe.addPtrUIHandler(new PtrUIHandler() {

            private int mLoadTime = 0;

            @Override
            public void onUIReset(PtrFrameLayout frame) {
                mLoadTime++;
                String string = mStringList[mLoadTime % mStringList.length];
                header.initWithString(string);
            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout frame) {
                String string = mStringList[mLoadTime % mStringList.length];
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout frame) {

            }

            @Override
            public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

            }
        });
        ptrframe.setHeaderView(header);
        ptrframe.addPtrUIHandler(header);
        ptrframe.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrframe.autoRefresh(true);
            }
        }, 100);

        ptrframe.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                wv_news.reload();
                frame.refreshComplete();
            }
        });


        settings = wv_news.getSettings();

        // wv_news.setBackgroundColor(0); // 设置背景色
        // wv_news.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setUseWideViewPort(true);// 支持双击缩放
        settings.setRenderPriority(RenderPriority.HIGH);

        settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式

        settings.setDomStorageEnabled(true);

        String cacheDirPath = getCacheDir().getAbsolutePath() + "/webViewCache ";

        settings.setDatabasePath(cacheDirPath);

        settings.setDatabaseEnabled(true);

        settings.setAppCachePath(cacheDirPath);

        settings.setAppCacheEnabled(true);

        settings.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        wv_news.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_progress.setVisibility(View.GONE);
                settings.setLoadsImagesAutomatically(true);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                wv_news.loadUrl(newsData.link);

            }

            /**
             * 所有跳转的链接都会在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        wv_news.loadUrl(newsData.link);// 加载网页
        // wv_news.loadUrl("file:///android_asset/test.html");// 加载网页
        wv_news.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 优化：超过多少个像素才触发隐藏，不然手指一按下就不隐藏了。
                 */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) (event.getRawY() + 0.5f);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveY = (int) (event.getRawY() + 0.5f);
                        dY = moveY - downY;
                        if (dY < -10) {

                            // 列表向下滚动，隐藏面板
                            if (ll_news_detail.getVisibility() == View.VISIBLE) {
                                hideBottonBar();
                                hideSystemUI();
                            }

                        } else if (dY > 10) {
                            // 列表向上滚动，显示面板
                            if (ll_news_detail.getVisibility() == View.GONE) {
                                showBottonBar();
                                showSystemUI();
                            }

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });

    }

    public void onClick_more(View view) {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra("newsData", newsData);
        startActivity(intent);
    }

    public void onClick_comment(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showBottonBar() {
        ll_news_detail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_on));

        ll_news_detail.setVisibility(View.VISIBLE);
    }

    private void hideBottonBar() {
        ll_news_detail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_off));

        ll_news_detail.setVisibility(View.GONE);
    }

}
