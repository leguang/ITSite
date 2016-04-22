package cn.itsite.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;
import cn.itsite.adapter.CommentListAdapter;
import cn.itsite.adapter.OnItemClickLitener;
import cn.itsite.utils.ToastUtils;

public class CommentActivity extends BaseActivity {

    private RecyclerView rv_comments_list;
    private ArrayList<String> list;
    private CommentListAdapter commentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();

    }

    protected void initView() {

        rv_comments_list = (RecyclerView) findViewById(R.id.rv_comments_list);
    }

    public void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 2000; i++) {
            list.add("中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国" + i);
        }

        rv_comments_list.setLayoutManager(new LinearLayoutManager(this));
        rv_comments_list.setAdapter(commentListAdapter = new CommentListAdapter(this, list));
        commentListAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast(CommentActivity.this, "第" + position);
            }
        });
    }
}
