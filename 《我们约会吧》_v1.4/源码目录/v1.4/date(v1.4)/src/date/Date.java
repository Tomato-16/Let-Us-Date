package date;

import java.util.ArrayList;

public class Date {
	public String d_id;	//Ô¼»á±àºÅ
	public String d_style;
	public String d_sex;
	public String d_age;
	public String d_distance;
	public String u_no;
	public String u_name;
	public String h_id;
	public String u_latitude;
	public String u_longitude;
	public ArrayList<Comments> commentList;
	
	public void setCommentList(ArrayList<Comments> commentList) {
		this.commentList = commentList;
	}

	public Date(String no,String style,String sex,String age,String distance,String u_no,String h_id,
			ArrayList<Comments> cmtList){
		this.d_id = no;
		this.d_style = style;
		this.d_sex = sex;
		this.d_age = age;
		this.d_distance = distance;
		this.u_no = u_no;
		this.h_id = h_id;
		this.commentList = cmtList;
	}
	public Date(String no,String style,String sex,String age,String distance,String u_name,String h_id,String u_no,String u_latitude,String u_longitude){
		this.d_id = no;
		this.d_style = style;
		this.d_sex = sex;
		this.d_age = age;
		this.d_distance = distance;
		this.u_name = u_name;
		this.h_id = h_id;
		this.u_no = u_no;
		this.u_latitude = u_latitude;
		this.u_longitude = u_longitude;
	}
	
	public Date(String no,String style,String sex,String age,String distance,String u_no){
		this.d_id = no;
		this.d_style = style;
		this.d_sex = sex;
		this.d_age = age;
		this.d_distance = distance;
		this.u_no = u_no;
	}
}
