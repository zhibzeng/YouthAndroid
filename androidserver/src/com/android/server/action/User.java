package com.android.server.action;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONObject;
import com.android.server.po.user;
import com.android.server.service.IUserService;
import com.google.gson.JsonArray;
public class User extends JsonActionSupport{
	private int id ;
	private String name;//用户名
	private String password; //密码 
	private String sex;//性别
	private String age;//年龄
	private String address; //住址
	private String registertime;//注册时间
	private String question;//密保问题
	private String answer;//密保答案
	private String marry;//婚姻状况
	private String hobby;//爱好
	private String email;//电子邮件
	private IUserService userService;
	
	//用户登录
	public void login() throws IOException{
		Integer type;
		user user = null;
		user = userService.getByName(name.trim());
		JSONObject o = new JSONObject();
		if(user!=null){
			if(user.getPassword().trim().equals(password.trim())){
				type=0;//登录成功
				o.put("id",user.getId()+"");
				o.put("name",user.getName()+"");
				o.put("password",user.getPassword()+"");
				o.put("address", user.getAddress()+"");
				o.put("age", user.getAge()+"");
				o.put("sex", user.getSex()+"");
				o.put("registertime",user.getRegistertime()+"");
				o.put("marry", user.getMarry()+"");
				o.put("hobby", user.getHobby()+"");
				o.put("email", user.getEmail()+"");
				o.put("question", user.getQuestion()+"");
				o.put("answer", user.getAnswer()+"");
			}else{
				type=1;//密码错误
			}
		}else{
			type=2;//尚未注册
		}
		o.put("type",type);
		write2Client(o.toString());
		System.out.println(o.toString());
	}
	
	//用户注册
	public void register() throws IOException{
		Integer type=null;
		user user = null;
		user = userService.getByName(name.trim());
		if(user!=null){
			type=1;//用户已经存在
		}else{
			user = new user();
			user.setName(name);
			user.setAge(Integer.parseInt(age));
			user.setAddress(address);
			user.setEmail(email);
			user.setHobby(hobby);
			user.setMarry(marry);
			user.setPassword(password);
			user.setSex(sex);
			user.setQuestion(question);
			user.setAnswer(answer);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
			String str = formatter.format(curDate);
			user.setRegistertime(str);
			if(userService.save(user)){
				type=2;//注册成功
			}else{
				type=3;//注册失败
			}
		}
		JSONObject o = new JSONObject();
		o.put("type", type);
		write2Client(o.toString());
	}
	
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRegistertime() {
		return registertime;
	}
	public void setRegistertime(String registertime) {
		this.registertime = registertime;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	
	

}
