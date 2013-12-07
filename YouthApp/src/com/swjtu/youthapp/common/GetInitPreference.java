package com.swjtu.youthapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GetInitPreference {
	private Context context;
	private static String StartInitConfig="initConfig";//初始配置文件名
	private static String Startcount="";//标志是否第一次启动
	/**
	 * Constructor
	 * */

	public GetInitPreference(){}
	public GetInitPreference(Context context){
		this.context=context;
	}
	
	//获取配置信息
	public String getInitData(){
		SharedPreferences sPreferences = context.getSharedPreferences(StartInitConfig,0 );
		Startcount=sPreferences.getString("Startcount","first");
		if(Startcount.equals("first")){
			changeInitData();
		}
		return Startcount;
	}
	
	//更改启动次数配置
	public void changeInitData(){
		SharedPreferences sPreferences = context.getSharedPreferences(StartInitConfig,0 );
		Editor editor = sPreferences.edit();
		editor.putString("Startcount","other");
		editor.commit();
	}
	
	

}
