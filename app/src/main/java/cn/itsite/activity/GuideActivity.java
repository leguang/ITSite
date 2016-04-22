package cn.itsite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.utils.SpUtils;

public class GuideActivity extends BaseActivity {
    private Button bt_guide;
    private ViewPager vp;
    private int[] resouces = {R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3, R.drawable.welcome4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        initDate();
    }

    private void initDate() {

        vp.setAdapter(new GuideViewPagerAdapter());
        vp.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                bt_guide.setVisibility(position == resouces.length - 1 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_guide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.setBoolean(GuideActivity.this, "firstentry", false);
                // 页面跳转
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                // 结束自己
                GuideActivity.this.finish();
            }
        });
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.vp_guide_activity);
        bt_guide = (Button) findViewById(R.id.bt_guide_activity);
    }

    class GuideViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (resouces != null) {
                return resouces.length;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GuideActivity.this, R.layout.layout_viewpager_guide_activity, null);
            ImageView iv_pager = (ImageView) view.findViewById(R.id.iv_viewpager_guide_activity);
            iv_pager.setBackgroundResource(resouces[position]);
            container.addView(iv_pager);
            return iv_pager;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
