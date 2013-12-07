package com.swjtu.youthapp;

import java.util.List;
import java.util.Random;

import com.swjtu.youthapp.data.SqliteControl;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Copyright (C) 2010,Under the supervision of China Telecom Corporation Limited
 * Guangdong Research Institute The New Vphone Project
 * 
 * @Author fonter.yang
 * @Create date：2010-10-11
 *    
 */
public class GridAdapter extends BaseAdapter {
	private static int[] ColumnImage = {R.drawable.column1,R.drawable.column2,R.drawable.column3,
		R.drawable.column4,R.drawable.column5,R.drawable.column6,R.drawable.column7,R.drawable.column8};
	private static int count=0;
	private class GridHolder {
		TextView appName;
		ImageButton appCancelButton;
		ImageView gridImageView;
		RelativeLayout columnLayout;
		}

	private Context context;

	private List<GridInfo> list;
	private LayoutInflater mInflater;

	public GridAdapter(Context c) {
		super();
		this.context = c;
	}

	public void setList(List<GridInfo> list) {
		this.list = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int index) {

		return list.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		GridHolder holder;
		if (convertView == null) {   
			convertView = mInflater.inflate(R.layout.grid_item, null);   
			holder = new GridHolder();
			holder.appName = (TextView)convertView.findViewById(R.id.itemText);
			holder.appCancelButton = (ImageButton)convertView.findViewById(R.id.itemCancel);
			holder.gridImageView=(ImageView)convertView.findViewById(R.id.gridImage);
			holder.columnLayout = (RelativeLayout) convertView.findViewById(R.id.itemImage);
			holder.columnLayout.setBackgroundResource(ColumnImage[(count%8)]);
			count++;
			holder.appCancelButton.setTag(index);
			holder.appCancelButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								int index = (Integer)v.getTag();
								GridInfo gridInfo = (GridInfo) list.get(index);
								//先将分类从用户订阅表中删除
								SqliteControl sqliteControl = new SqliteControl(context);
								boolean flag=sqliteControl.deleteCategoryOrder(gridInfo.getCategoryid());
								sqliteControl.close();
								if(flag){
								
									list.remove(index);
									((ViewFlipperActivity)context).refreshFlipper();
								}
							}
						});
			convertView.setTag(holder);   
		}else{
			 holder = (GridHolder) convertView.getTag();   
		}
		GridInfo info = list.get(index);
		if (info != null) {   
			holder.appName.setText(info.getName());
			holder.gridImageView.setImageBitmap(info.getImage());
		}
		return convertView;
	}

}
