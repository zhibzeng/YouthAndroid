package com.swjtu.youthapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.po.CategoryEntity;
import com.swjtu.youthapp.widget.PullDownListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryOrderActivity extends Activity {
	private ArrayList<HashMap<String, Object>> listItem;
	private ListView categoryOrderListView;
	private SimpleAdapter listItemAdapter;
	//底部导航条
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,main_bottom_layout4;
	private Button categoryOrderBackBtn; //返回按钮
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.categoryorder);
		categoryOrderListView = (ListView) findViewById(R.id.categorytitlelist);
		initCategoryTitle();
		// 底部导航条
		main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
		main_bottom_layout1.setOnClickListener(clickListener_layout1);

		main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
		main_bottom_layout2.setOnClickListener(clickListener_layout2);

		main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
		main_bottom_layout3.setOnClickListener(clickListener_layout3);

		main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
		main_bottom_layout4.setOnClickListener(clickListener_layout4);
		
		categoryOrderBackBtn = (Button) findViewById(R.id.categoryOrderBackBtn);
		categoryOrderBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO go back to main Activity
				Intent intent = new Intent();
				intent.setClass(CategoryOrderActivity.this,ViewFlipperActivity.class);
				startActivity(intent);
				CategoryOrderActivity.this.finish();
			}
		});
	}
		//按下返回键	
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
	    		Intent intent =new Intent();
				intent.setClass(CategoryOrderActivity.this,ViewFlipperActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.view_push_down_out_in, R.anim.view_push_down_in_out);//activity切换动画
				CategoryOrderActivity.this.finish();
	    	}
	    	return true;
	  }
	  
	  
	public void initCategoryTitle(){
		  listItem = new ArrayList<HashMap<String, Object>>();
		 List<CategoryEntity>categoryOrderList=new ArrayList<CategoryEntity>();
		 List<CategoryEntity>categoryUnOrderList=new ArrayList<CategoryEntity>();
		 SqliteControl sqliteControl = new SqliteControl(this);
		 categoryOrderList=sqliteControl.getCategoryOrder();
		 categoryUnOrderList=sqliteControl.getUnOrderCategories();
		 sqliteControl.close();
		 if(categoryOrderList!=null){
		     for(int i=0;i<categoryOrderList.size();i++)
	         {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            map.put("categoryOrderImage", R.drawable.order_finish);//图像资源的ID,无图片则设为null
	            map.put("categoryOrderTitle", categoryOrderList.get(i).getName());
	            map.put("order",1);//已经订阅则order=1
	            map.put("categoryid",categoryOrderList.get(i).getId());
	            listItem.add(map);
	         }
			 }
		 if(categoryUnOrderList!=null){
		     for(int i=0;i<categoryUnOrderList.size();i++)
	         {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            map.put("categoryOrderImage", R.drawable.order);//图像资源的ID,无图片则设为null
	            map.put("categoryOrderTitle", categoryUnOrderList.get(i).getName());
	            map.put("order",0);//已经订阅则order=1
	            map.put("categoryid",categoryUnOrderList.get(i).getId());
	            listItem.add(map);
	         }
			 }
		 
		  //生成适配器的Item和动态数组对应的元素
	     listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
	            R.layout.categoryitem,//ListItem的XML实现
	            //动态数组与ImageItem对应的子项        
	            new String[] {"categoryOrderImage","categoryOrderTitle"}, 
	            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
	            new int[] {R.id.categoryOrderImage,R.id.categoryOrderTitle}
	     );   
	     //添加并且显示
	     categoryOrderListView.setAdapter(listItemAdapter);  
	     categoryOrderListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				int order;
				int categoryid;
				String title;
				HashMap<String, Object> data = (HashMap<String, Object>)categoryOrderListView.getItemAtPosition(position);
				order=(Integer) data.get("order");
				categoryid=(Integer) data.get("categoryid");
				title=(String) data.get("categoryOrderTitle");
				if(order==0){
					//点击未订阅的分类
					SqliteControl sqliteControl = new SqliteControl(CategoryOrderActivity.this);
					sqliteControl.InsertIntoCategoryOrder(null, categoryid);
					sqliteControl.close();
					//下面是动态修改Listview中Map的值
					listItem.remove(position);//移除指定位置的值
					//创建一个新的list项
					 HashMap<String, Object> map = new HashMap<String, Object>();
					 //插入新值
					  map.put("categoryOrderImage", R.drawable.order_finish);//图像资源的ID,无图片则设为null
			          map.put("categoryOrderTitle", title);
			          map.put("order",1);//已经订阅则order=1
			          map.put("categoryid",categoryid);
			        //把新的值插入到list的指定位置
			          listItem.add(position, map);
			     	 // 通知适配器，数据已经改变
			          listItemAdapter.notifyDataSetChanged();
				}else{
					//点击已经订阅过的分类
					SqliteControl sqliteControl = new SqliteControl(CategoryOrderActivity.this);
					boolean flag=sqliteControl.deleteCategoryOrder(categoryid);
					if(flag){
						//下面是动态修改Listview中Map的值
						listItem.remove(position);//移除指定位置的值
						//创建一个新的list项
						HashMap<String, Object> map = new HashMap<String, Object>();
						//插入新值
						map.put("categoryOrderImage", R.drawable.order);//图像资源的ID,无图片则设为null
				        map.put("categoryOrderTitle", title);
				        map.put("order",0);//已经订阅则order=1
				        map.put("categoryid",categoryid);
				        //把新的值插入到list的指定位置
				        listItem.add(position, map);
				     	// 通知适配器，数据已经改变
				        listItemAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	
	
	
	/**
	 * 底部导航条 导航1 按钮单击事件
	 */
    private OnClickListener clickListener_layout1 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(true);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(CategoryOrderActivity.this, ViewFlipperActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.fade,R.anim.fade);
				main_bottom_layout1.setSelected(false);
				CategoryOrderActivity.this.finish();
		}
	};
	
	/**
	 * 底部导航条 导航2 按钮单击事件
	 */
    private OnClickListener clickListener_layout2 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(true);
			main_bottom_layout3.setSelected(false);
			main_bottom_layout4.setSelected(false);
			Intent intent = new Intent();
			intent.setClass(CategoryOrderActivity.this, UserInfo.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout2.setSelected(false);
			CategoryOrderActivity.this.finish();
		}
	};
	
	/**
	 * 底部导航条 导航3 按钮单击事件
	 */
    private OnClickListener clickListener_layout3 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(false);
			main_bottom_layout3.setSelected(true);
			main_bottom_layout4.setSelected(false);
			Intent intent = new Intent();
			intent.setClass(CategoryOrderActivity.this, VisionActivity.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout3.setSelected(false);
			CategoryOrderActivity.this.finish();
		}
	};
	
	
	
	/**
	 * 底部导航条 导航4 按钮单击事件
	 */
    private OnClickListener clickListener_layout4 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(false);
			main_bottom_layout3.setSelected(false);
			main_bottom_layout4.setSelected(true);
			Intent intent = new Intent();
			intent.setClass(CategoryOrderActivity.this, SinaWeiboWebView.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			overridePendingTransition(R.anim.fade,R.anim.fade);
			main_bottom_layout4.setSelected(false);
			CategoryOrderActivity.this.finish();
		}
	};
	
	

}
