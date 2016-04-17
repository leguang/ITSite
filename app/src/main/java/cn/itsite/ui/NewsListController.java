package cn.itsite.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.itsite.R;
import cn.itsite.activity.NewsDetailActivity;
import cn.itsite.bean.NewsData;
import cn.itsite.ui.base.BaseController;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.LocalDisplay;
import cn.itsite.utils.SpUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class NewsListController extends BaseController {

    private String TAG = "NewsListController";
    public String mUrl;
    private RecyclerView rv_list;
    private ArrayList<NewsData> newsDataList = new ArrayList<NewsData>();
    private MyAdapter myAdapter;
    private StoreHouseHeader header;
    private PtrFrameLayout ptrframe;
    public RequestQueue mRequestQueue;

    public NewsListController(Context context, String rss_url) {
        super(context);
        this.mUrl = rss_url;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.layout_viewpager_news_platform_activity, null);
        header = new StoreHouseHeader(mContext);
        ptrframe = (PtrFrameLayout) view.findViewById(R.id.store_house_ptr_frame_news_list_pager);
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list_viewpager_news_platform_activity);
        return view;
    }

    @Override
    public void initData() {
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
                getDataFromServer();
                frame.refreshComplete();
            }
        });

        String cache = SpUtils.getString(mContext, mUrl, null);
        if (!TextUtils.isEmpty(cache)) {
            try {
                parseData(cache);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }


        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(myAdapter = new MyAdapter());
        // 设置item动画
        rv_list.setItemAnimator(new DefaultItemAnimator());
        myAdapter.setOnItemClickLitener(new OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                // 在本地记录已读状态
                String readLinks = SpUtils.getString(mContext, "readLinks", "");
                String readLink = newsDataList.get(position).link;
                if (!readLinks.contains(readLink)) {
                    readLinks = readLinks + readLink + ",";
                    SpUtils.setString(mContext, "readLinks", readLinks);
                }
                // 实现局部界面刷新, 这个view就是被点击的item布局对象
                changeReadState(view);
                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mContext, NewsDetailActivity.class);
                intent.putExtra("newsData", newsDataList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }

    // 从网络获取数据
    private void getDataFromServer() {
        final long timeNew = System.currentTimeMillis();
        final long timeOld = SpUtils.getLong(mContext, "lastTime", 0l);
        if (timeNew - timeOld > ConstantsUtils.VALID_TIME) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        response = new String(response.getBytes("iso-8859-1"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Snackbar.make(mRootView, "数据解析异常!", Snackbar.LENGTH_LONG).show();
                    }

                    try {
                        parseData(response);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        Snackbar.make(mRootView, "数据解析异常!", Snackbar.LENGTH_LONG).show();
                    }
                    // 设置缓存
                    SpUtils.setString(mContext, mUrl, response);
                    SpUtils.setLong(mContext, "lastTime", timeNew);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Snackbar.make(mRootView, "网络异常,未获取到数据!", Snackbar.LENGTH_LONG).show();
                }
            });
            mRequestQueue.add(stringRequest);
        }
    }

    protected void parseData(String result) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(result));
        Element root = document.getRootElement();
        List<Element> list = root.element("channel").elements("item");
        for (Element element : list) {
            NewsData newsData = new NewsData();
            newsData.title = element.element("title").getTextTrim();
            newsData.link = element.element("link").getTextTrim();
            newsData.description = element.element("description").getTextTrim();
            newsData.pubDate = element.element("pubDate").getTextTrim();
            String[] temp = newsData.pubDate.split(" ");
            newsData.pubDate = temp[0] + temp[4].trim();
            newsDataList.add(newsData);
        }
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_lv_item_viewpager_news_platform_activity, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            NewsData newsData = newsDataList.get(position);
            Glide.with(mContext).load(R.drawable.test1).placeholder(R.drawable.defaultpicture).crossFade().into(holder.iv_pic);

            holder.tv_title.setText(newsData.title.trim());
            holder.tv_description.setText(newsData.description.trim());
            holder.tv_pubDate.setText(newsData.pubDate.trim());

            String readLinks = SpUtils.getString(mContext, "readLinks", "");
            if (readLinks.contains(newsData.link)) {
                holder.tv_title.setTextColor(Color.GRAY);
            } else {
                holder.tv_title.setTextColor(Color.BLACK);
            }

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            if (newsDataList != null) {
                return newsDataList.size();
            }
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title;
            ImageView iv_pic;
            TextView tv_comments;
            TextView tv_description;
            TextView tv_pubDate;

            public MyViewHolder(View view) {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_description = (TextView) view.findViewById(R.id.tv_description);
                tv_pubDate = (TextView) view.findViewById(R.id.tv_pubDate);
                iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            }
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


}
