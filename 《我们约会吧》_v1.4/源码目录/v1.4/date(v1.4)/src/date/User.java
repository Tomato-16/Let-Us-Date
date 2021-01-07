package date;

public class User {
	public String u_no;
	public String u_name;
	public String u_email;
	public String u_state;
	public String h_id;
	public String u_sex;
	public String u_birthday;
	public String u_longitude;
	public String u_latitude;
	public String u_pwd;
	
	public User(String u_no,String u_name,String u_email,String u_state,String h_id){
		this.u_no = u_no;
		this.u_name = u_name;
		this.u_state = u_state;
		this.u_email = u_email;
		this.h_id = h_id;
	}
	public User(String u_no,String u_name,String u_email,String u_state,String h_id,String u_sex,String u_birthday){
		this.u_no = u_no;
		this.u_name = u_name;
		this.u_state = u_state;
		this.u_email = u_email;
		this.h_id = h_id;
		this.u_sex = u_sex;
		this.u_birthday = u_birthday;
	}
	public User(String u_no,String u_name,String u_email,String h_id,String u_sex,String u_birthday,String u_longitude,String u_latitude){
		this.u_no = u_no;
		this.u_name = u_name;		
		this.u_email = u_email;
		this.h_id = h_id;
		this.u_sex = u_sex;
		this.u_birthday = u_birthday;
		this.u_longitude = u_longitude;
		this.u_latitude = u_latitude;
	}
	public User(String u_no,String u_name,String u_email,String h_id,String u_sex,String u_birthday,String u_longitude,String u_latitude,String u_pwd){
		this.u_no = u_no;
		this.u_name = u_name;		
		this.u_email = u_email;
		this.h_id = h_id;
		this.u_sex = u_sex;
		this.u_birthday = u_birthday;
		this.u_longitude = u_longitude;
		this.u_latitude = u_latitude;
		this.u_pwd = u_pwd;
	}
}