package struts.test;
import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
public class SetTestAction extends ActionSupport{
	private String country;
	private String city;
	private Map<Integer, String> citys;
	private int list;
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getList() {
		return list;
	}

	public void setList(int list) {
		this.list = list;
	}

	public Map<Integer, String> getCitys() {
		return citys;
	}

	public void setCitys(Map<Integer, String> citys) {
		this.citys = citys;
	}

	public String execute(){
		//Map sessionMap = ServletActionContext.getContext().getSession();
		Customer customer = new Customer();
		customer.setContact("jack");
		customer.setEmail("568585595@qq.com");
		//sessionMap.put("customer",customer);
		citys = new HashMap<Integer, String>();
		citys.put(1, "chengdu");
		citys.put(2, "du");
		citys.put(3, "cheng");
		return SUCCESS;
	}
	
	public String put(){
		System.out.println(list);
		System.out.println(country);
		System.out.println(city);
		return "put";
	}
	
	//进度条显示
	public String processor(){
		try {
			Thread.sleep(12000);
		} catch (Exception e) {
		}
		return "processor";
	}
	private int complete=0;
	public int getComplete(){
		complete+=10;
		return complete;
	}
	
	

}
class Customer{
	private String contact;
	private String email;
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	Customer(String contact,String email){
		this.contact=contact;
		this.email = email;
	}
	Customer(){
		
	}
}
