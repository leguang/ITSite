package cn.itsite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.application.BaseApplication;
import cn.itsite.bean.CategoriesData;
import cn.itsite.ui.NewsListController;


public class NewsPlatformActivity extends BaseActivity {

    private String TAG = "HomeFragment";
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private CategoriesData mCategoriesData;
    private TabLayout mTabLayout;
    private ImageButton ib_lefterback_platform;
    private ImageButton ib_search_platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_platform);
        mCategoriesData = BaseApplication.categoriesData;

        if (savedInstanceState == null) {
            initView();
            initData();
        }
    }

    private void initView() {

        ib_lefterback_platform = (ImageButton) findViewById(R.id.ib_lefterback_platform);
        ib_search_platform = (ImageButton) findViewById(R.id.ib_search_platform);
        mViewPager = (ViewPager) findViewById(R.id.vp_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout_news_platform_activity);
    }

    private void initData() {

        mAdapter = new MyPagerAdapter();
        if (mViewPager != null) {
            mViewPager.setAdapter(mAdapter);
        }
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

        ib_lefterback_platform.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ib_search_platform.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent toSearchIntent = new Intent(NewsPlatformActivity.this, LoginRegisterActivity.class);
                startActivity(toSearchIntent);
            }
        });
    }


    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mCategoriesData.TypeDataList != null) {
                return mCategoriesData.TypeDataList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            NewsListController pager = new NewsListController(NewsPlatformActivity.this, mCategoriesData.TypeDataList.get(position).url);

            if (pager != null) {
                container.addView(pager.mRootView);
                pager.initData();
            }

            return pager.mRootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCategoriesData.TypeDataList.get(position).type;
        }
    }
}
