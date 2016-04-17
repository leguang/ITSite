package cn.itsite.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.itsite.R;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.LocalDisplay;
import cn.itsite.utils.ToastUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class CommunityFragment extends Fragment {

    private TextView tv_name_topbar;
    private StoreHouseHeader header;
    private PtrFrameLayout ptrframe;
    private RecyclerView rv_sort;
    private MyAdapter mAdapter;
    private String[] description = {"话题1", "话题2", "话题3", "话题4", "话题5", "话题6", "话题7", "话题8"};
    private int[] imagArray = {R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community_main_activity, container, false);

        tv_name_topbar = (TextView) view.findViewById(R.id.tv_title_topbar_community_fragment);
        tv_name_topbar.setText("社区");

        header = new StoreHouseHeader(getActivity());
        ptrframe = (PtrFrameLayout) view.findViewById(R.id.store_house_ptr_frame_sort_fragment);

        rv_sort = (RecyclerView) view.findViewById(R.id.rv_sort);

        initData();

        return view;
    }

    private void initData() {

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
                // getDataFromServer();
                frame.refreshComplete();
            }
        });

        rv_sort.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_sort.setAdapter(mAdapter = new MyAdapter());
        // 设置item动画
        rv_sort.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast(getActivity(), "第:" + position);

//                Intent intent = new Intent();
//                intent.setClass(getActivity(), NewsDetailActivity.class);
//
//                NewsData mNewsData = new NewsData();
//                mNewsData.link = "http://dis.myzaker.com/wap/v2/?discussion_id=156&post_id=56f270d39490cb6370000035";
//                intent.putExtra("newsData", mNewsData);
//                getActivity().startActivity(intent);

            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.layout_rv_list_item_community_fragment, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

//            Glide.with(CommunityFragment.this).load(imagArray[position]).placeholder(R.drawable.defaultpicture).crossFade().into(holder.iv_center);
            Glide.with(CommunityFragment.this).load("http://q.qlogo.cn/qqapp/1105136720/82482A35C8395A1C8574C81CB09957DD/100").placeholder(R.drawable.defaultpicture).crossFade().into(holder.iv_center);

            holder.tv_description_sort.setText(description[position]);

            // 如果设置了回调，则设置点击事件
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
        public int getItemCount() {
            if (imagArray != null) {
                return imagArray.length;
            }
            return 0;
        }

        class MyViewHolder extends ViewHolder {
            ImageView iv_center;
            TextView tv_description_sort;

            public MyViewHolder(View view) {
                super(view);
                iv_center = (ImageView) view.findViewById(R.id.iv_center);
                tv_description_sort = (TextView) view.findViewById(R.id.tv_description_sort);

            }
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


}
