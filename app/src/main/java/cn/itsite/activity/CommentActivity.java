package cn.itsite.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itsite.R;
import cn.itsite.activity.base.BaseActivity;

public class CommentActivity extends BaseActivity {

    private ListView lv_comments_list;
    private MyBaseAdapter mBaseAdapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();

    }

    protected void initView() {

        lv_comments_list = (ListView) findViewById(R.id.lv_comments_list);
    }

    public void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 2000; i++) {
            list.add("中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国" + i);
        }

        mBaseAdapter = new MyBaseAdapter();
        lv_comments_list.setAdapter(mBaseAdapter);

    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // ViewHolder holder;
            // if (convertView == null) {
            // holder = new ViewHolder();
            View view = View.inflate(CommentActivity.this, R.layout.layout_list_item_comment, null);
            TextView tv_comment_content = (TextView) view.findViewById(R.id.tv_content_rv_item_comment);
            // convertView.setTag(holder);
            // } else {
            // holder = (ViewHolder) convertView.getTag();
            // }
            tv_comment_content.setText(list.get(position));

            return view;
        }
    }

    class ViewHolder {
        public TextView tv_comment;
    }
}
