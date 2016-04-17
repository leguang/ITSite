package cn.itsite.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.itsite.R;
import cn.itsite.activity.NewsPlatformActivity;
import cn.itsite.utils.ToastUtils;


public class HomeFragment extends Fragment {

    private RvGridAdapter mAdapter;
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

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_select_paltform);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter = new RvGridAdapter());


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

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    class RvGridAdapter extends RecyclerView.Adapter<RvGridAdapter.MyViewHolder> {

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public int getItemCount() {
            if (icons != null) {
                return icons.length;
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.tv_name.setText(names[position]);
            holder.iv_icon.setImageResource(icons[position]);

            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.layout_rv_grid_item_homefragment, parent, false));
            return holder;

        }

        class MyViewHolder extends ViewHolder {

            ImageView iv_icon;
            TextView tv_name;

            public MyViewHolder(View view) {
                super(view);
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);

            }
        }

    }
}
