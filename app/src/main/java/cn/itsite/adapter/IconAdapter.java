package cn.itsite.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconAdapter extends BaseAdapter {

    private int[] icons;
    private String[] names;
    private Context context;
    private int layoutID;
    private int iconID;
    private int nameID;

    public IconAdapter(Context context, int[] icons, String[] names, int layoutID, int iconID, int nameID) {
        super();
        this.icons = icons;
        this.names = names;
        this.context = context;
        this.layoutID = layoutID;
        this.iconID = iconID;
        this.nameID = nameID;
    }

    @Override
    public int getCount() {
        if (names != null) {
            return names.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, layoutID, null);
        ImageView iv_icon_gv_personal = (ImageView) view.findViewById(iconID);
        TextView tv_name_gv_personal = (TextView) view.findViewById(nameID);

        iv_icon_gv_personal.setImageResource(icons[position]);
        tv_name_gv_personal.setText(names[position]);

        return view;
    }
}
