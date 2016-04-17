package cn.itsite.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.itsite.R;
import cn.itsite.application.BaseApplication;
import cn.itsite.bean.UserConfig;
import cn.itsite.bean.WeatherData;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ToastUtils;

public class PersonalFragment extends Fragment {

    private TextView tv_name_topbar;

    private MyGridBaseAdapter mAdapterTop;
    private MyGridBaseAdapter mAdapterBottom;
    private MyListViewBaseAdapter mAdaptermid;

    private GridView gv_top_myfragment;
    private GridView gv_bottom_myfragment;
    private ListView lv_mid_myfragment;

    private TextView tv_today_weather;
    private TextView tv_nickname;
    private ImageView iv_head_icon;
    private TextView tv_tomorrow_weather;

    public RequestQueue mRequestQueue;
    public String cityName;
    public WeatherData mWeatherData;

    private String[] topGridnames = {"离线下载", "夜间模式", "字体大小"};
    private String[] bottomGridnames = {"文字模式", "推荐好友", "清理缓存"};

    private int[] topGridicons = {R.drawable.download, R.drawable.info_day, R.drawable.typebar_details};
    private int[] bottomGridicons = {R.drawable.picture_or_no, R.drawable.introduction, R.drawable.delete_allshare};

    private String[] midListLefttexts = {"我的消息", "我的评论", "我的收藏", "我的反馈", "主题皮肤", "使用帮助", "关注IT站点", "版本号"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        BaseApplication.bus.register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal_main_activity, container, false);

        tv_name_topbar = (TextView) view.findViewById(R.id.tv_title_topbar_personal_fragment);
        tv_name_topbar.setText("个人中心");

        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname_personal_fragment);

        iv_head_icon = (ImageView) view.findViewById(R.id.iv_head_icon_personal_fragment);

        tv_today_weather = (TextView) view.findViewById(R.id.tv_today_weather);
        tv_tomorrow_weather = (TextView) view.findViewById(R.id.tv_tomorrow_weather);

        gv_top_myfragment = (GridView) view.findViewById(R.id.gv_top_myfragment);
        gv_top_myfragment.setAdapter(mAdapterTop = new MyGridBaseAdapter(topGridicons, topGridnames));

        lv_mid_myfragment = (ListView) view.findViewById(R.id.lv_mid_myfragment);
        lv_mid_myfragment.setAdapter(mAdaptermid = new MyListViewBaseAdapter());

        gv_bottom_myfragment = (GridView) view.findViewById(R.id.gv_bottom_myfragment);
        gv_bottom_myfragment.setAdapter(mAdapterBottom = new MyGridBaseAdapter(bottomGridicons, bottomGridnames));


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initData();


        String cache = SpUtils.getString(getActivity(), ConstantsUtils.WEATHER_URL, null);
        if (!TextUtils.isEmpty(cache)) {
            parseWeatherData(cache);
        }

        cityName = SpUtils.getString(getActivity(), "cityName", null);
        ToastUtils.showToast(getActivity(), cityName);
        // cityName = "武汉";
        if (TextUtils.isEmpty(cityName)) {
            getWeatherServer("北京");
        } else {
            getWeatherServer(cityName);
        }

    }


    @Subscribe
    public void initData( UserConfig userConfig ) {

        if (userConfig.nickname != null) {
            tv_nickname.setText(userConfig.nickname);
            Glide.with(this).load(userConfig.figureurUrl).placeholder(R.drawable.defaultpicture).crossFade().into(iv_head_icon);
        }
    }


    class MyGridBaseAdapter extends BaseAdapter {

        private int[] icons;
        private String[] names;

        public MyGridBaseAdapter(int[] icons, String[] names) {
            super();
            this.icons = icons;
            this.names = names;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getActivity(), R.layout.layout_gv_item_personal_fragment_main_activity, null);
            ImageView iv_icon_gv_personal = (ImageView) view.findViewById(R.id.iv_icon_gv_personal);
            TextView tv_name_gv_personal = (TextView) view.findViewById(R.id.tv_name_gv_personal);

            iv_icon_gv_personal.setImageResource(icons[position]);
            tv_name_gv_personal.setText(names[position]);

            return view;
        }

    }

    class MyListViewBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return midListLefttexts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getActivity(), R.layout.layout_lv_setting_item_personal_fragment_main_activity, null);
            TextView tv_lefttext_setting_item = (TextView) view.findViewById(R.id.tv_lefttext_setting_item);
            tv_lefttext_setting_item.setText(midListLefttexts[position]);

            return view;
        }

    }

    /**
     * 获取天气接口数据
     */
    private void getWeatherServer(String cityName) {

        try {
            cityName = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = ConstantsUtils.WEATHER_URL + cityName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // 设置缓存
                SpUtils.setString(getActivity(), ConstantsUtils.WEATHER_URL, response);
                System.out.println("getWeatherServer****************************" + response);

                parseWeatherData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mRequestQueue.add(stringRequest);

    }

    private void parseWeatherData(String json) {
        Gson gson = new Gson();
        mWeatherData = gson.fromJson(json, WeatherData.class);

        if (mWeatherData != null) {
            // 刷新天气UI
            String today = "今天" + mWeatherData.getResult().get(0).getDate() + mWeatherData.getResult().get(0).getWeek() + "\n" + mWeatherData.getResult().get(0).getCity() + "天气："
                    + mWeatherData.getResult().get(0).getFuture().get(0).getDayTime() + "\n" + "温度：" + mWeatherData.getResult().get(0).getFuture().get(0).getTemperature();

            tv_today_weather.setText(today);

            String tomorrow = "明天\n" + mWeatherData.getResult().get(0).getFuture().get(1).getDayTime();
            tv_tomorrow_weather.setText(tomorrow);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRequestQueue.cancelAll(this);
    }


}
