package com.swjtu.youthapp;

import java.io.File;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import com.swjtu.youthapp.common.DeleteFile;
import com.swjtu.youthapp.common.GetInitPreference;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.guide.Appstart;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

//app entrance
public class StartActivity extends InstrumentedActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//full screen and no title 
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startactivity);
		GetInitPreference initPreference = new GetInitPreference(this);
		String string = initPreference.getInitData();
		if (string.equals("first")) { // whether it is the first time to launch app
			String filename = android.os.Environment
					.getExternalStorageDirectory() + "/YouthAppData";
			File databaseFile = new File(filename);
			if (databaseFile.exists()) {
				DeleteFile.delete(databaseFile);
			}
			new Thread() {
				public void run() {
					createDatabase();
				}
			}.start();
			Intent intent = new Intent(StartActivity.this, Appstart.class);
			startActivity(intent);
			overridePendingTransition(R.anim.view_push_down_out_in,
					R.anim.view_push_down_in_out);
			finish();
		} else {
			Intent intent = new Intent(StartActivity.this, Welcome.class);
			startActivity(intent);
			overridePendingTransition(R.anim.view_push_down_in_out,
					R.anim.view_push_down_out_in);
			finish();

		}
	}

	public void createDatabase() {
		deleteDatabase(android.os.Environment.getExternalStorageDirectory()
				+ "/YouthAppData");
		SqliteControl sqliteControl = new SqliteControl(this);
		sqliteControl.FirstStart();
		sqliteControl.close();
	}

	private void init() {
		JPushInterface.init(getApplicationContext());
	}

}
