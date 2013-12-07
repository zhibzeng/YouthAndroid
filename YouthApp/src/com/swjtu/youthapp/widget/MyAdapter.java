package com.swjtu.youthapp.widget;
import java.util.ArrayList;
import java.util.HashMap;
import com.swjtu.youthapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> arrays=null;
	private Context mContext;

	public MyAdapter(Context mContext, ArrayList<HashMap<String, Object>>  arrays) {
		this.mContext = mContext;
		this.arrays = arrays;

	}

	public int getCount() {
		return this.arrays.size();
	}

	public Object getItem(int position) {
		return arrays.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
  
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.newsitem, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.newsitemtitle);
			viewHolder.imageView=(ImageView) view.findViewById(R.id.newsitemimage);
			viewHolder.time = (TextView) view.findViewById(R.id.newsItemTime);
			viewHolder.comeFrom = (TextView) view.findViewById(R.id.newsItemComefrom);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}			
		viewHolder.tvTitle.setText((CharSequence) this.arrays.get(position).get("ItemTitle"));
		viewHolder.imageView.setImageResource((Integer) arrays.get(position).get("ItemImage"));
		viewHolder.time.setText((CharSequence)this.arrays.get(position).get("time"));
		viewHolder.comeFrom.setText((CharSequence)this.arrays.get(position).get("comefrom"));
		
		return view;

	}

	final static class ViewHolder {
		public TextView tvTitle;
		public TextView comeFrom;
		public TextView time;
		public ImageView imageView;
	}
}