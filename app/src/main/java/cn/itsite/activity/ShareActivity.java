package cn.itsite.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.adapter.IconAdapter;
import cn.itsite.bean.NewsData;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ToastUtils;
import cn.itsite.view.RangeSliderView;

public class ShareActivity extends BaseActivity implements IWeiboHandler.Response {

    private GridView gv_more_share;
    private GridView gv_more_function;
    private WebView wv_news;
    private NewsData newsData;
    private TextView tv_typeface;
    private ImageView iv_cancel;
    private WebSettings settings;
    private RangeSliderView rsv;
    private String[] typeface = new String[]{"超小字体", "小号字体", "中等字体", "大号字体", "超大号字体"};
    private String[] shareNames = new String[]{"QQ好友", "QQ空间", "新浪微博", "微信朋友圈", "微信好友", "复制"};
    private String[] functionNames = new String[]{"刷新", "日间模式", "收藏"};
    private int[] shareIcons = new int[]{R.drawable.icon_qq_pressed, R.drawable.icon_share_qzone, R.drawable.icon_sina_pressed, R.drawable.icon_wechatmoments, R.drawable.icon_wx_pressed,
            R.drawable.icon_share_copy};

    private int[] functionIcons = new int[]{R.drawable.info_refresh_night, R.drawable.info_day, R.drawable.info_favo};
    private Tencent mTencent;
    private IWeiboShareAPI mWeiboShareAPI = null;
    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        newsData = (NewsData) getIntent().getSerializableExtra("newsData");

        mTencent = Tencent.createInstance(ConstantsUtils.QQ_APP_ID, this.getApplicationContext());

        api = WXAPIFactory.createWXAPI(this, ConstantsUtils.WEIXIN_APP_ID);

        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ConstantsUtils.APP_KEY);

        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        initView();
        initData();

    }

    protected void initView() {
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel_share_activity);
        gv_more_share = (GridView) findViewById(R.id.gv_more_share);
        gv_more_function = (GridView) findViewById(R.id.gv_more_function);

        tv_typeface = (TextView) findViewById(R.id.tv_typeface);
        rsv = (RangeSliderView) findViewById(R.id.rsv);

    }

    public void initData() {
        gv_more_share.setAdapter(new IconAdapter(this, shareIcons, shareNames, R.layout.layout_gv_item_more_share_activity, R.id.iv_icon_more_share_activity, R.id.tv_name_more_share_activity));
        gv_more_function.setAdapter(new IconAdapter(this, functionIcons, functionNames, R.layout.layout_gv_item_more_share_activity, R.id.iv_icon_more_share_activity, R.id.tv_name_more_share_activity));
        gv_more_share.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showQQShare();
                        break;
                    case 1:
                        showQQZoneshare();

                        break;
                    case 2:
                        showWeiboShare();
                        break;
                    case 3:
                        showWeixinFriendsShare();
                        break;
                    case 4:
                        showWeixinShare();
                        break;
                    case 5:
                        showCopy();

                }

            }
        });

        rsv.setInitialIndex(1);
        rsv.setOnSlideListener(new RangeSliderView.OnSlideListener() {

            @Override
            public void onSlide(int index) {
                tv_typeface.setText(typeface[index]);
                wv_news.getSettings().setTextZoom(50 + index * 50);
            }
        });

        gv_more_function.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                switch (position) {
                    case 0:
                        wv_news.reload();
                        break;
                    case 1:
                        Boolean isDaytime = SpUtils.getBoolean(ShareActivity.this, "isDaytime", true);
                        isDaytime = !isDaytime;
                        SpUtils.setBoolean(ShareActivity.this, "isDaytime", isDaytime);

                        if (isDaytime) {
                            tv_name.setText("日间模式");
                            iv_icon.setImageResource(R.drawable.info_day);
                            ToastUtils.showToast(ShareActivity.this, "日间模式");
                        } else {
                            tv_name.setText("夜间模式");
                            iv_icon.setImageResource(R.drawable.info_night);
                            ToastUtils.showToast(ShareActivity.this, "夜间模式");
                        }

                        break;
                    case 2:


                        break;
                }
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showCopy() {


    }


    protected void showQQZoneshare() {
        ToastUtils.showToast(ShareActivity.this, "QQ空间");

    }

    protected void showWeiboShare() {
        ToastUtils.showToast(ShareActivity.this, "微博分享");

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        TextObject textObject = textObject = new TextObject();
        WebpageObject mediaObject = new WebpageObject();
        if (newsData != null) {
            textObject.text = newsData.title;

            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = newsData.title;
            mediaObject.description = newsData.description;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            // 设置 Bitmap 类型的图片到视频对象里.设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
            mediaObject.actionUrl = newsData.link;
        }

        weiboMessage.textObject = textObject;
        weiboMessage.mediaObject = mediaObject;
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(ShareActivity.this, request);

    }

    private void showWeixinFriendsShare() {

    }

    protected void showWeixinShare() {

        ToastUtils.showToast(ShareActivity.this, "微信分享");
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = newsData.title;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = newsData.title;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;


        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

    protected void showQQShare() {
        String title = newsData.title;
        String imageUrl = ConstantsUtils.LOGO_URL;
        String targetUrl = newsData.link;
        String summary = newsData.description;
        String appName = "IT站点";
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);

        mTencent.shareToQQ(ShareActivity.this, params, qqShareListener);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @param baseResponse 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {

        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "分享失败" + "Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    class FunctionBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return functionNames.length;
        }

        @Override
        public Object getItem(int position) {
            return functionNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ShareActivity.this, R.layout.layout_gv_item_more_share_activity, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name.setText(functionNames[position]);
            iv_icon.setImageResource(functionIcons[position]);

            switch (position) {
                case 0:

                    break;
                case 1:
                    if (!SpUtils.getBoolean(ShareActivity.this, "isDaytime", true)) {
                        iv_icon.setImageResource(R.drawable.info_night);
                    }
                    break;
                case 2:
                    break;
            }

            return view;
        }
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object response) {
        }

        @Override
        public void onError(UiError e) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTencent != null) {
            mTencent.releaseResource();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
}
