package cn.itsite.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import cn.itsite.adapter.IconAdapter;
import cn.itsite.adapter.ListSettingPersonalFragmentAdapter;
import cn.itsite.adapter.OnItemClickLitener;
import cn.itsite.application.BaseApplication;
import cn.itsite.bean.UserInfoData;
import cn.itsite.bean.WeatherData;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {

    private TextView tv_name_topbar;

    private IconAdapter topAdapter;
    private IconAdapter bottomAdapter;
    private ListSettingPersonalFragmentAdapter midAdapter;

    private GridView gv_top_personalfragment;
    private GridView gv_bottom_personalfragment;
    private RecyclerView rv_mid;
    private UserInfoData userInfo;
    private TextView tv_today_weather;
    private TextView tv_nickname;
    private CircleImageView iv_head_icon;
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

        iv_head_icon = (CircleImageView) view.findViewById(R.id.iv_head_icon_personal_fragment);

        tv_today_weather = (TextView) view.findViewById(R.id.tv_today_weather);
        tv_tomorrow_weather = (TextView) view.findViewById(R.id.tv_tomorrow_weather);

        gv_top_personalfragment = (GridView) view.findViewById(R.id.gv_top_personalfragment);
        gv_top_personalfragment.setAdapter(topAdapter = new IconAdapter(getActivity(), topGridicons, topGridnames, R.layout.layout_gv_item_personal_fragment_main_activity, R.id.iv_icon_gv_personal_fragment, R.id.tv_name_gv_personal_fragment));

        rv_mid = (RecyclerView) view.findViewById(R.id.rv_mid_personal_fragment);
        rv_mid.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mid.setAdapter(midAdapter = new ListSettingPersonalFragmentAdapter(getActivity(), midListLefttexts));

        gv_bottom_personalfragment = (GridView) view.findViewById(R.id.gv_bottom_personalfragment);
        gv_bottom_personalfragment.setAdapter(bottomAdapter = new IconAdapter(getActivity(), bottomGridicons, bottomGridnames, R.layout.layout_gv_item_personal_fragment_main_activity, R.id.iv_icon_gv_personal_fragment, R.id.tv_name_gv_personal_fragment));

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();


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

    private void initData() {
        if (BaseApplication.islogin) {
            tv_nickname.setText(BaseApplication.userInfo.nickname);
            Glide.with(this).load(BaseApplication.userInfo.figureurUrl).crossFade().into(iv_head_icon);
        }

        midAdapter.setOnItemClickLitener(new OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast(getActivity(), "第" + position);
            }
        });
    }


    @Subscribe
    public void updateUi(UserInfoData userInfo) {

        if (userInfo.nickname != null) {
            tv_nickname.setText(userInfo.nickname);
            Glide.with(this).load(userInfo.figureurUrl).crossFade().into(iv_head_icon);
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
        BaseApplication.bus.unregister(this);
    }
}
