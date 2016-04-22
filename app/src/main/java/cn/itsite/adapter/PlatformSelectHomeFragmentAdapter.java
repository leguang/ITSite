package cn.itsite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.itsite.R;

public class PlatformSelectHomeFragmentAdapter extends RecyclerView.Adapter<PlatformSelectHomeFragmentAdapter.MyViewHolder> {

    private int[] icons;
    private String[] names;
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;

    public PlatformSelectHomeFragmentAdapter(Context context, int[] icons, String[] names) {
        super();
        this.icons = icons;
        this.names = names;
        this.context = context;
    }

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
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_rv_grid_item_homefragment, parent, false));
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        public MyViewHolder(View view) {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
