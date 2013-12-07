package com.swjtu.youthapp;
import com.swjtu.youthapp.data.FetchDataFromServer;
import com.swjtu.youthapp.po.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * @author zhibinZeng
 * @Date 2013-1-31
 * @Description 用户注册Activity
 */
public class UserRegister extends Activity{
	private static final String[] ADDRESS = {"天佑斋1","天佑斋2","天佑斋3","天佑斋4","天佑斋5","天佑斋6"
		,"天佑斋7","天佑斋8","天佑斋9","天佑斋10","天佑斋11","天佑斋12","天佑斋13","天佑斋14","天佑斋15","天佑斋16"
		,"天佑斋17","天佑斋18","天佑斋19","天佑斋20","天佑斋21","天佑斋22","其他"
	};
	private static final String[] QUESTION = {"你的寝室号是什么?","你的初恋是谁?","你的小学校名是什么?","你高三的班主任是谁?"};
	private EditText registerName,password,passwordConfirm,answer;
	private Button registerSubmitBtn,registerResetBtn;
	private RadioGroup sexRadioGroup;
	private Spinner addressSpinner,questionSpinner;
	private boolean flag = true;
	private ProgressDialog registerProgressDialog;
	private Integer type=3;//注册返回码
	/* type=1 用户已经存在
	 * type=2 注册成功
	 * type=3 注册失败
	 */
	private static final String USER_INFO_SHARE_PREFERENCE="userinfo";
	private static final String USER_NAME="username";
	private static final String USER_PWD = "userpwd";
	//首页底部导航条
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,main_bottom_layout4;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.userregister);
	        registerName = (EditText) findViewById(R.id.registerName);
	        password = (EditText) findViewById(R.id.password);
	        passwordConfirm = (EditText) findViewById(R.id.passwrodConfirm);
	        answer = (EditText)findViewById(R.id.answer);
	        registerSubmitBtn = (Button) findViewById(R.id.registerSubmitBtn);
	        registerResetBtn = (Button) findViewById(R.id.registerResetBtn);
	        sexRadioGroup = (RadioGroup) findViewById(R.id.sexMenu);
	        addressSpinner = (Spinner) findViewById(R.id.address);
	        questionSpinner = (Spinner) findViewById(R.id.question);
	        registerSubmitBtn.setOnClickListener(submitOnClickListener);
	        registerResetBtn.setOnClickListener(resetOnClickListener);
	        initData();
			//首页底部导航条
			main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
			main_bottom_layout1.setOnClickListener(clickListener_layout1);
			main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
			main_bottom_layout2.setOnClickListener(clickListener_layout2);
			main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
			main_bottom_layout3.setOnClickListener(clickListener_layout3);
			main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
			main_bottom_layout4.setOnClickListener(clickListener_layout4);
	 }
	 
	 /**
		 * 首页底部导航条 导航1 按钮单击事件
		 */
	    private OnClickListener clickListener_layout1 = new OnClickListener() {
			public void onClick(View v) {
				main_bottom_layout1.setSelected(true);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(UserRegister.this, ViewFlipperActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout2.setSelected(false);
				UserRegister.this.finish();
			}
		};

		/**
		 * 首页底部导航条 导航2 按钮单击事件
		 */
	    private OnClickListener clickListener_layout2 = new OnClickListener() {
			public void onClick(View v) {
					main_bottom_layout1.setSelected(false);
					main_bottom_layout2.setSelected(true);
					main_bottom_layout3.setSelected(false);
					main_bottom_layout4.setSelected(false);
					Intent intent = new Intent();
					intent.setClass(UserRegister.this, UserInfo.class);
					intent.putExtra("clickble", true);
					startActivity(intent);
					main_bottom_layout2.setSelected(false);
					UserRegister.this.finish();
			}
		};
		
		/**
		 * 首页底部导航条 导航3 按钮单击事件
		 */
	    private OnClickListener clickListener_layout3 = new OnClickListener() {
			public void onClick(View v) {
				main_bottom_layout1.setSelected(false);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(true);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(UserRegister.this, VisionActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout3.setSelected(false);
				UserRegister.this.finish();
				
			}
		};

		/**
		 * 首页底部导航条 导航4 按钮单击事件
		 */
	    private OnClickListener clickListener_layout4 = new OnClickListener() {
			public void onClick(View v) {
					main_bottom_layout1.setSelected(false);
					main_bottom_layout2.setSelected(false);
					main_bottom_layout3.setSelected(false);
					main_bottom_layout4.setSelected(true);
					Intent intent = new Intent();
					intent.setClass(UserRegister.this, SinaWeiboWebView.class);
					intent.putExtra("clickble", true);
					startActivity(intent);
					overridePendingTransition(R.anim.fade,R.anim.fade);
					main_bottom_layout4.setSelected(false);
					UserRegister.this.finish();
			}
		};
		
	 
	 
	 /**
	  * 初始化显示数据
	  */
	 private void initData(){
		  ArrayAdapter<String> addressAdpter = new ArrayAdapter<String>(this,
				  android.R.layout.simple_spinner_item,
				  ADDRESS);
		  addressAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      addressSpinner.setAdapter(addressAdpter);
		  
	      ArrayAdapter<String> questionAdapter = new ArrayAdapter<String>(this, 
				  android.R.layout.simple_spinner_item,
				  QUESTION);
	      questionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      questionSpinner.setAdapter( questionAdapter);
	      
	 }
	 
	 
	 /**
	  * validate Data 
	  */
	 private Boolean validateData(){
		 if(registerName.getText().toString().trim().length()==0){
			 registerName.setError("请输入用户名");
			 return false;
		 }
		 if(password.getText().toString().trim().length()==0){
			 password.setError("请输入密码");
			 return false;
		 }
		 if(passwordConfirm.getText().toString().trim().length()==0){
			 passwordConfirm.setError("请再次输入密码");
			 return false;
		 }
		if(passwordConfirm.getText().toString().trim().length()!=0&&
				!passwordConfirm.getText().toString().trim().equals(password.getText().toString().trim())){
			passwordConfirm.setError("两次输入密码不匹配");
			 return false;
		}
		if(answer.getText().toString().trim().length()==0){
			 answer.setError("请输入答案");
			 return false;
		 }
		 return true;
	 }
	 
	 
	 /**
	  * 返回用户选择的address
	  */
	 private String getAddress(){
		 return ADDRESS[addressSpinner.getSelectedItemPosition()];
	 }
	 

	 /**
	  * 返回用户选择的密保问题
	  */
	 private String getQuestion(){
		 return QUESTION[questionSpinner.getSelectedItemPosition()];
	 }
	 
	 
	 /**
	  * 返回用户性别
	  */
	 private String getSex(){
			RadioButton mRadio = (RadioButton) findViewById(sexRadioGroup
	    			.getCheckedRadioButtonId());
	    			return mRadio.getText().toString();
	 }
	 
	 
	 /**
	  * 注册完成后转向登陆页面
	  */
	 Handler registerHandler = new Handler(){
			public void handleMessage(Message msg) {
				//TODO 转向登录页面
				Integer type=msg.getData().getInt("type");
				if(type==1){
					Toast.makeText(UserRegister.this,"用户名已存在，请重新输入",Toast.LENGTH_SHORT).show();
				}else if(type==2){
					Toast.makeText(UserRegister.this,"注册成功，即将跳转到登陆页面",Toast.LENGTH_SHORT).show();
					SharedPreferences share = getSharedPreferences(USER_INFO_SHARE_PREFERENCE, 0);
					share.edit().putString(USER_NAME,registerName.getText().toString().trim()).commit();
					share.edit().putString(USER_PWD,password.getText().toString().trim()).commit();
					Intent intent = new Intent();
					intent.setClass(UserRegister.this,UserLogin.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade, R.anim.fade);
					UserRegister.this.finish();
					//跳转到登陆页面
				}else{
					Toast.makeText(UserRegister.this,"网络故障，注册失败",Toast.LENGTH_SHORT).show();
				}
			}
		 
	 };
	 
	 
	 /**
	  *submit button listener
	  */
	 private OnClickListener submitOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			flag = validateData();
			if(flag){
				new AlertDialog.Builder(UserRegister.this).setTitle(
						"注册用户信息").setMessage(
								"用户名："+registerName.getText().toString()+"\n"
								+"性别："+getSex()+"\n"
								+"住址："+getAddress()+"\n"
								+"密保问题："+getQuestion()+"\n"
								+"密保答案: "+answer.getText().toString()+"\n"
								)
								.setCancelable(false)
								.setPositiveButton("确认",
										new DialogInterface.OnClickListener(){
									public void onClick(
											DialogInterface dialog,int id){
										registerProgressDialog=ProgressDialog.show(UserRegister.this, "正在连接", "请稍等....",true);
										registerProgressDialog.setCancelable(true);
										FetchDataFromServer registerToServer = new FetchDataFromServer();
										User user = new User();
										user.setName(registerName.getText().toString().trim());
										user.setPassword(password.getText().toString().trim());
										user.setSex(getSex());
										user.setAddress(getAddress());
										user.setQuestion(getQuestion());
										user.setAnswer(answer.getText().toString().trim());
										type=registerToServer.userRegister(user);
										Message message = new Message();
										Bundle bundle = new Bundle();
						                bundle.putInt("type", type);
						                message.setData(bundle);
										registerHandler.sendMessage(message);
										registerProgressDialog.dismiss();
									}
								}).setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.dismiss();
											}
										}).show();
			
				}
		};
	 };
	 
	 
	 /**
	  * reset button listener
	  */
	 private OnClickListener resetOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			registerName.setText("");
			password.setText("");
			passwordConfirm.setText("");
			answer.setText("");
		}
	};

}
