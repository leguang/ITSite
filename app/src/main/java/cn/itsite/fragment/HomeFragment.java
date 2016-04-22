package cn.itsite.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.itsite.R;
import cn.itsite.activity.NewsPlatformActivity;
import cn.itsite.activity.SerchActivity;
import cn.itsite.adapter.OnItemClickLitener;
import cn.itsite.adapter.PlatformSelectHomeFragmentAdapter;
import cn.itsite.utils.ToastUtils;


public class HomeFragment extends Fragment {

    private PlatformSelectHomeFragmentAdapter mAdapter;
    private int[] icons = {R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu, R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu, R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu,
            R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu, R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu, R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu,
            R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu, R.drawable.tencent, R.drawable.wangyi, R.drawable.souhu};
    private String[] names = {"腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐", "腾讯", "网易", "搜狐"};
    private TextView tv_name_topbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_activity, container, false);

        tv_name_topbar = (TextView) view.findViewById(R.id.tv_title_topbar_home_fragment);
        tv_name_topbar.setText("主页");

        ImageView  ib_search_topbar = (ImageView) view.findViewById(R.id.ib_search_topbar);
        ib_search_topbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SerchActivity.class));
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_select_paltform);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter = new PlatformSelectHomeFragmentAdapter(getActivity(),icons,names));


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initData() {
        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast(getActivity(), "第" + position);
                Intent intent = new Intent(getActivity(), NewsPlatformActivity.class);
                startActivity(intent);
            }
        });
    }

}
