package cn.itsite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.io.UnsupportedEncodingException;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.application.BaseApplication;
import cn.itsite.bean.CategoriesData;
import cn.itsite.bean.WeatherData;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.ToastUtils;

public class SplashActivity extends BaseActivity {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public RequestQueue mRequestQueue;
    public String cityName = "武汉";
    public WeatherData mWeatherData = null;
    public Double latitude;
    public Double longitude;
    public View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        initLogin();
        getServer();
        initLocation();
    }

    private void initLogin() {
        BaseApplication.islogin = SpUtils.getBoolean(this, ConstantsUtils.ISLOGIN, false);

        if (BaseApplication.islogin) {
            BaseApplication.mTencent = Tencent.createInstance(ConstantsUtils.QQ_APP_ID, this.getApplicationContext());
            String token = SpUtils.getString(this, Constants.PARAM_ACCESS_TOKEN, null);
            String expires = SpUtils.getString(this, Constants.PARAM_EXPIRES_IN, null);
            String openId = SpUtils.getString(this, Constants.PARAM_OPEN_ID, null);

            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                BaseApplication.mTencent.setAccessToken(token, expires);
                BaseApplication.mTencent.setOpenId(openId);
            }


            if (BaseApplication.mTencent.isSessionValid()) {

                System.out.println(BaseApplication.mTencent.isSessionValid() + "********################*************");

                BaseApplication.userInfo.nickname = SpUtils.getString(this, ConstantsUtils.USERINFO_NICKNAME, null);
                BaseApplication.userInfo.figureurUrl = SpUtils.getString(this, ConstantsUtils.USERINFO_FIGUREURL, null);
                BaseApplication.userInfo.loginType = SpUtils.getInt(this, ConstantsUtils.USERINFO_LOGIN_TYPE, ConstantsUtils.ERROR_LOGIN_TYPE);


            } else {

                BaseApplication.islogin = false;
                SpUtils.setBoolean(this, ConstantsUtils.ISLOGIN, BaseApplication.islogin);
                ToastUtils.showToast(this, "登陆已过期");

                SpUtils.setString(this, ConstantsUtils.USERINFO_NICKNAME, null);
                SpUtils.setString(this, ConstantsUtils.USERINFO_FIGUREURL, null);
                SpUtils.setString(this, ConstantsUtils.USERINFO_LOGIN_TYPE, null);

            }

        }
    }

    private void initLocation() {

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.start();
        LocationClientOption option = new LocationClientOption();
        mLocationClient.registerLocationListener(myListener);

        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            System.out.println("************************************************");

            switch (location.getLocType()) {

                case BDLocation.TypeServerError:
                case BDLocation.TypeNetWorkException:
                case BDLocation.TypeCriteriaException:
                    Snackbar.make(view, "定位失败！", Snackbar.LENGTH_LONG).show();
                    jumpTo();
                    break;
                default:
                    cityName = location.getCity();
                    System.out.println("*****************定位定位********************" + cityName);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    System.out.println("*****************定位定位********************");
                    if (!TextUtils.isEmpty(cityName) && cityName.contains("市")) {
                        cityName = cityName.substring(0, cityName.lastIndexOf("市"));
                        SpUtils.setString(SplashActivity.this, "cityName", cityName);
                        SpUtils.setFloat(SplashActivity.this, "latitude", Float.parseFloat(latitude.toString()));
                        SpUtils.setFloat(SplashActivity.this, "longitude", Float.parseFloat(longitude.toString()));
                        System.out.println("*****************定位定位********************" + cityName);
                        jumpTo();
                    }
                    break;
            }
        }
    }


    private void initView() {
        view = getWindow().getDecorView();
        mRequestQueue = Volley.newRequestQueue(this);
    }


    private void initData() {
        String cache = SpUtils.getString(this, ConstantsUtils.SERVER_URL, null);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
    }

    /**
     * 获取网络数据
     */
    private void getServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConstantsUtils.SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("iso-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("*****************网络********************" + response);
                parseData(response);

                // 设置缓存
                SpUtils.setString(SplashActivity.this, ConstantsUtils.SERVER_URL, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Snackbar.make(view, "网络异常,未获取到数据!", Snackbar.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(stringRequest);
    }

    /**
     * 解析网络数据
     *
     * @param result
     */
    protected void parseData(String result) {
        Gson gson = new Gson();
        BaseApplication.categoriesData = gson.fromJson(result, CategoriesData.class);
    }

    private void jumpTo() {
        Boolean isFirstentry = SpUtils.getBoolean(this, "firstentry", true);
        if (isFirstentry) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestQueue.cancelAll(this);
        mLocationClient.stop();
    }

}
