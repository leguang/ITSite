package cn.itsite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itsite.R;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> list;
    private OnItemClickLitener mOnItemClickLitener;

    public CommentListAdapter(Context context, ArrayList<String> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_comment_content.setText(list.get(position));

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_list_item_comment, parent, false));
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_comment_content;

        public MyViewHolder(View view) {
            super(view);
            tv_comment_content = (TextView) view.findViewById(R.id.tv_content_rv_item_comment);
        }
    }
}
