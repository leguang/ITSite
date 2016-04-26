package cn.itsite.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.application.BaseApplication;
import cn.itsite.fragment.CommunityFragment;
import cn.itsite.fragment.HomeFragment;
import cn.itsite.fragment.PersonalFragment;

public class MainActivity extends BaseActivity {

    private HomeFragment mHomeFragment;
    private CommunityFragment mCommunityFragment;
    private PersonalFragment mPersonalFragment;

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = BottomBar.attach(this, savedInstanceState);

        if (savedInstanceState == null) {
            initView();
            initData();
        }

    }

    public void initView() {


    }

    public void initData() {
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                hideAllFragments(ft);

                switch (menuItemId) {
                    case R.id.bb_menu_home:

                        if (mHomeFragment == null) {
                            mHomeFragment = new HomeFragment();
                            ft.add(R.id.content_main_activity, mHomeFragment, "home");
                        } else {
                            ft.show(mHomeFragment);
                        }
                        ft.commit();
                        break;

                    case R.id.bb_menu_community:
                        if (mCommunityFragment == null) {
                            mCommunityFragment = new CommunityFragment();
                            ft.add(R.id.content_main_activity, mCommunityFragment, "sort");
                        } else {
                            ft.show(mCommunityFragment);
                        }
                        ft.commit();
                        break;

                    case R.id.bb_menu_personal:
                        if (mPersonalFragment == null) {
                            mPersonalFragment = new PersonalFragment();
                            ft.add(R.id.content_main_activity, mPersonalFragment, "my");
                        } else {
                            ft.show(mPersonalFragment);
                        }
                        ft.commit();
                        if (!BaseApplication.islogin) {
                            startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                        }
                        break;
                }

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

                switch (menuItemId) {
                    case R.id.bb_menu_home:
                        break;

                    case R.id.bb_menu_community:
                        break;

                    case R.id.bb_menu_personal:
                        break;

                }

            }
        });

    }


    private void hideAllFragments(FragmentTransaction ft) {
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mCommunityFragment != null) {
            ft.hide(mCommunityFragment);
        }
        if (mPersonalFragment != null) {
            ft.hide(mPersonalFragment);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }


}
