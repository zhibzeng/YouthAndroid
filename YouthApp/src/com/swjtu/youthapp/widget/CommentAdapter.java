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
public class CommentAdapter extends BaseAdapter{
	private ArrayList<HashMap<String, Object>> arrays=null;
	private Context mContext;

	public CommentAdapter(Context mContext, ArrayList<HashMap<String, Object>>  arrays) {
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

	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(view==null){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
			viewHolder.username = (TextView) view.findViewById(R.id.comment_username);
			viewHolder.content=(TextView) view.findViewById(R.id.comment_content);
			viewHolder.createtime=(TextView) view.findViewById(R.id.comment_createtime);
			viewHolder.imageView = (ImageView) view.findViewById(R.id.comment_userimage);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.username.setText((CharSequence) this.arrays.get(position).get("username"));
		viewHolder.content.setText((CharSequence) this.arrays.get(position).get("content"));
		viewHolder.createtime.setText((CharSequence)this.arrays.get(position).get("createtime"));
		viewHolder.imageView.setImageResource((Integer) arrays.get(position).get("userimage"));
		return view;
	}
	
	final static class ViewHolder{
		public TextView username;
		public TextView content;
		public TextView createtime;
		public ImageView imageView;
	}

}
