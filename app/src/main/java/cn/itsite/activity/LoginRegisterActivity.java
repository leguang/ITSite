package cn.itsite.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.application.BaseApplication;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ToastUtils;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class LoginRegisterActivity extends BaseActivity {
    private UserInfo mInfo;
    private TextView registerBtn;
    private TextView registerTitle;
    private TextInputLayout registerUser;
    private TextInputLayout registerPass;
    private TextInputLayout registerPassRep;
    private TextView loginTitle;
    private TextInputLayout loginUser;
    private TextInputLayout loginPass;
    private TextView loginBtn;
    private FloatingActionButton registerFab;
    private View registerCancel;
    private View loginView;
    private View loginCard;
    private View registerView;
    private View registerCard;
    private GridView mGridView;
    private ImageView mLogo;
    private GridViewBaseAdapter mAdapter;

    private Pattern pattern = Pattern.compile(ConstantsUtils.REGEX_MOBILE);
    private Matcher matcher;


    private String[] thirdNames = new String[]{"QQ", "微博", "微信"};
    private int[] thirdIcons = new int[]{R.drawable.icon_qq_pressed, R.drawable.icon_sina_pressed, R.drawable.icon_wx_pressed};
    public static Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        initView();
        initData();
    }

    private void initView() {
        loginView = findViewById(R.id.login_window);
        loginCard = findViewById(R.id.login_card);
        loginTitle = (TextView) findViewById(R.id.login_title);
        loginUser = (TextInputLayout) findViewById(R.id.login_user);
        loginPass = (TextInputLayout) findViewById(R.id.login_pass);
        loginBtn = (TextView) findViewById(R.id.login_btn);

        registerView = findViewById(R.id.register_window);
        registerCard = findViewById(R.id.register_card);
        registerTitle = (TextView) findViewById(R.id.register_title);
        registerUser = (TextInputLayout) findViewById(R.id.register_user);
        registerPass = (TextInputLayout) findViewById(R.id.register_pass);
        registerPassRep = (TextInputLayout) findViewById(R.id.register_pass_rep);
        registerBtn = (TextView) findViewById(R.id.register_btn);

        registerFab = (FloatingActionButton) findViewById(R.id.register_fab);
        registerCancel = findViewById(R.id.register_cancel);

        mGridView = (GridView) findViewById(R.id.gv_login_register_activity);
        mLogo = (ImageView) findViewById(R.id.iv_login_register_activity);
    }

    private void initData() {
        loginUser.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginUser.setErrorEnabled(true);
                if (TextUtils.isEmpty(s)) {
                    loginUser.setError("亲！手机号码不能为空哟");
                } else if (s.toString().length() <= 1 && !"1".equals(s.toString())) {
                    loginUser.setError("亲！您输入的不是手机号码吧");
                } else if (s.toString().length() > 10 && !isMobile(s.toString())) {
                    loginUser.setError("亲！您输入的不是手机号码吧");
                } else {
                    loginUser.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginPass.setErrorEnabled(true);
                if (TextUtils.isEmpty(s)) {
                    loginPass.setError("亲！密码不能为空哟");
                } else if (s.toString().length() < 6) {
                    loginPass.setError("亲！密码长度要大于6位哟");
                } else {
                    loginPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        registerUser.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerUser.setErrorEnabled(true);
                if (TextUtils.isEmpty(s)) {
                    registerUser.setError("亲！手机号码不能为空哟");
                } else if (s.toString().length() <= 1 && !"1".equals(s.toString())) {
                    registerUser.setError("亲！您输入的不是手机号码吧");
                } else if (s.toString().length() > 10 && !isMobile(s.toString())) {
                    registerUser.setError("亲！您输入的不是手机号码吧");
                } else {
                    registerUser.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPass.setErrorEnabled(true);
                if (TextUtils.isEmpty(s)) {
                    registerPass.setError("亲！密码不能为空哟");
                } else if (s.toString().length() < 6) {
                    registerPass.setError("亲！密码长度要大于6位哟");
                } else {
                    registerPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerPassRep.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = registerPass.getEditText().getText().toString();
                registerPassRep.setErrorEnabled(true);
                if (TextUtils.isEmpty(s)) {
                    registerPassRep.setError("亲！密码不能为空哟");
                } else if (s.toString().length() < 6) {
                    registerPassRep.setError("亲！密码长度要大于6位哟");
                } else if (s.toString().length() >= 6 && !s.toString().equals(password)) {
                    registerPassRep.setError("亲！两次密码要一致哟");
                } else {
                    registerPassRep.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mGridView.setAdapter(mAdapter = new GridViewBaseAdapter());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(LoginRegisterActivity.this, "登陆");
                login();
            }
        });

        registerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRegister();
            }
        });

        registerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateLogin();
            }
        });

    }


    private void animateRegister() {
        Path path = new Path();
        RectF rect = new RectF(-241F, -40F, 41F, 242F);
        path.addArc(rect, -45F, 180F);
        path.lineTo(-0F, -50F);
        FabAnimation fabAnimation = new FabAnimation(path);
        fabAnimation.setDuration(400);
        fabAnimation.setInterpolator(new AccelerateInterpolator());

        fabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                SupportAnimator animator = getCircularRevealAnimation(registerCard, registerView.getWidth() - 250, 400, 0f, 2F * registerView.getHeight());
                animator.setDuration(700);
                animator.setStartDelay(200);
                animator.addListener(new SupportAnimator.SimpleAnimatorListener() {
                    public void onAnimationStart() {
                        registerView.setVisibility(View.VISIBLE);
                        registerFab.setVisibility(View.GONE);
                    }

                    public void onAnimationEnd() {

                    }
                });
                animator.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        registerFab.startAnimation(fabAnimation);
    }

    private SupportAnimator getCircularRevealAnimation(View view, int centerX, int centerY, float startRadius, float endRadius) {
        return ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
    }

    private void animateLogin() {
        registerCancel.animate().scaleX(0F).scaleY(0F).alpha(0F).rotation(90F).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
        SupportAnimator animator = getCircularRevealAnimation(registerCard, registerView.getWidth() / 2, registerView.getHeight() / 2, 1f * registerView.getHeight(), 0F);
        animator.setDuration(500);
        animator.setStartDelay(100);
        animator.addListener(new SupportAnimator.SimpleAnimatorListener() {
            public void onAnimationStart() {
                loginView.setVisibility(View.VISIBLE);
            }

            public void onAnimationEnd() {
                registerView.setVisibility(View.GONE);
                registerCancel.setScaleX(1F);
                registerCancel.setScaleY(1F);
                registerCancel.setAlpha(1F);
                registerCancel.setRotation(45F);
                registerFab.setVisibility(View.VISIBLE);

                ObjectAnimator animX = ObjectAnimator.ofFloat(registerFab, "scaleX", 0F, 1F);
                ObjectAnimator animY = ObjectAnimator.ofFloat(registerFab, "scaleY", 0F, 1F);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(registerFab, "alpha", 0F, 1F);
                ObjectAnimator rotation = ObjectAnimator.ofFloat(registerFab, "rotation", 90F, 0F);
                AnimatorSet animator = new AnimatorSet();
                animator.playTogether(animX, animY, alpha, rotation);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.setDuration(200);
                animator.start();
            }
        });
        animator.start();
    }


    class FabAnimation extends Animation {
        private PathMeasure measure;
        private float[] pos;

        public FabAnimation(Path path) {
            measure = new PathMeasure(path, false);
            pos = new float[]{0, 0};
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            measure.getPosTan(measure.getLength() * interpolatedTime, pos, null);
            Matrix matrix = t.getMatrix();
            matrix.setTranslate(pos[0], pos[1]);
            matrix.preRotate(interpolatedTime * 45);
            t.setAlpha(1 - interpolatedTime);
        }
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public boolean isMobile(String mobile) {

        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    class GridViewBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return thirdIcons.length;
        }

        @Override
        public Object getItem(int position) {
            return thirdIcons[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(LoginRegisterActivity.this, R.layout.layout_gv_item_login_register_activity, null);
            TextView tv_share_name = (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_share_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_share_name.setText(thirdNames[position]);
            iv_share_icon.setImageResource(thirdIcons[position]);
            return view;
        }
    }

    public void login() {
        mTencent = Tencent.createInstance(ConstantsUtils.QQ_APP_ID, this.getApplicationContext());
        String token = SpUtils.getString(this, Constants.PARAM_ACCESS_TOKEN, null);
        String expires = SpUtils.getString(this, Constants.PARAM_EXPIRES_IN, null);
        String openId = SpUtils.getString(this, Constants.PARAM_OPEN_ID, null);

        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }


        if (!mTencent.isSessionValid()) {

            System.out.println(mTencent.isSessionValid());
            mTencent.login(this, "all", loginListener);
        }
    }


    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {

        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {

        String token = jsonObject.optString(Constants.PARAM_ACCESS_TOKEN);
        String expires = jsonObject.optString(Constants.PARAM_EXPIRES_IN);
        String openId = jsonObject.optString(Constants.PARAM_OPEN_ID);

        SpUtils.setString(this, Constants.PARAM_ACCESS_TOKEN, token);
        SpUtils.setString(this, Constants.PARAM_EXPIRES_IN, expires);
        SpUtils.setString(this, Constants.PARAM_OPEN_ID, openId);

        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(UserInfolistener);

        } else {
        }
    }


    IUiListener UserInfolistener = new IUiListener() {

        @Override
        public void onError(UiError e) {

        }

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            JSONObject json = (JSONObject) response;

            if (json.has("figureurl")) {

                System.out.println(json.optString("figureurl_qq_2"));

                String figureurl = json.optString("figureurl_qq_2");

                BaseApplication.userConfig.figureurUrl = figureurl;
                SpUtils.setString(LoginRegisterActivity.this, ConstantsUtils.FIGUREURL, figureurl);

            }

            if (json.has("nickname")) {
                System.out.println(json.optString("nickname"));
                String nickname = json.optString("nickname");

                BaseApplication.userConfig.nickname = nickname;
                SpUtils.setString(LoginRegisterActivity.this, ConstantsUtils.NICKNAME, nickname);
            }

            BaseApplication.bus.register(LoginRegisterActivity.this);

            BaseApplication.bus.post(BaseApplication.userConfig);
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
