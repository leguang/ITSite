package cn.itsite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.itsite.R;

public class ListSettingPersonalFragmentAdapter extends RecyclerView.Adapter<ListSettingPersonalFragmentAdapter.MyViewHolder> {

    private String[] midListLefttexts;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;

    public ListSettingPersonalFragmentAdapter(Context context, String[] midListLefttexts) {
        super();

        this.context = context;
        this.midListLefttexts = midListLefttexts;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        if (midListLefttexts != null) {
            return midListLefttexts.length;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tv_name.setText(midListLefttexts[position]);

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

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_lv_setting_item_personal_fragment_main_activity, parent, false));
        return holder;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_lefttext_setting_item);

        }
    }


}
