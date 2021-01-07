package com.date;


import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class RegActivity extends Activity implements View.OnClickListener {
	MyConnector mc = null;			//声明MyConnector对象
	String uno = "";				//记录用户的ID
	ProgressDialog pd= null;		//声明进度对话框
	
	private static final String[] dates_sex={"女","男"};
	private static final String[] dates_age={"20-30","30-40","40-50","50以上"};
	private Spinner spsex;
	private Spinner spage;
	private ArrayAdapter<CharSequence> adaptersex;
	private ArrayAdapter<CharSequence> adapterage;
	private EditText etName = null;
	private EditText etPwd1 = null;
	private EditText etPwd2 = null;
	private EditText etEmail = null;
	private TextView tvShowDialog;
	private Calendar cal;
	private int year,month,day;
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(RegActivity.this);
				builder.setTitle("注册成功")
					   .setMessage("注册成功，您的约会号为：" + uno)
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(RegActivity.this,MainActivity.class);	//创建Intent
							intent.putExtra("uno", uno);		//设置Extra字段
							startActivity(intent);				//启动FunctionTab
							finish();
						}
						   
					   });
				builder.create();
				builder.show();
				
				
				break;
			}
			super.handleMessage(msg);
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainregister);
        etName = (EditText)findViewById(R.id.etName);			//获得昵称EditText对象
		etName.getBackground().setAlpha(210);
		etPwd1 = (EditText)findViewById(R.id.etPwd1);			//获得密码EditText对象
		etPwd1.getBackground().setAlpha(210);
		etPwd2 = (EditText)findViewById(R.id.etPwd2);			//获得确认密码EditText对象
		etPwd2.getBackground().setAlpha(210);
		etEmail = (EditText)findViewById(R.id.etEmail);		//获得邮箱EditText对象
		etEmail.getBackground().setAlpha(210);
		
        spsex = (Spinner) findViewById(R.id.spSex);         //获得性别选择框对象
        spsex.getBackground().setAlpha(150);
        adaptersex = new ArrayAdapter<CharSequence>(RegActivity.this,android.R.layout.simple_spinner_item,dates_sex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsex.setAdapter(adaptersex);
        
        spage = (Spinner) this.findViewById(R.id.spAge);         //获得年纪选择框对象
        spage.getBackground().setAlpha(150);
        adapterage = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,dates_age);
        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spage.setAdapter(adapterage);

		getDate();               //获取当前日期
		tvShowDialog=(TextView) findViewById(R.id.tvShowDialog);    //获取生日文本框对象
		tvShowDialog.setOnClickListener(this);

		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);				//获得返回按钮对象
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);                    //设置样式
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
			
			public void onClick(View v) {                //返回按钮所执行的方法
				Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//创建Intent对象
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				startActivity(intent);										//启动Activity
				finish();
			}
		});
		
		ImageButton btnReg = (ImageButton)findViewById(R.id.btnReg);		//获得注册Button按钮对象
		btnReg.setScaleType(ImageButton.ScaleType.CENTER_CROP);             //设置样式
		btnReg.setPadding(8, 8, 8, 8);
		btnReg.getBackground().setAlpha(100);
		btnReg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {               //注册按钮所执行的方法
				pd = ProgressDialog.show(RegActivity.this, "请稍候...", "正在连接服务器...", false);
				register();
			}
		});
    }

	//获取当前日期（用于选择生日时的默认值）
	private void getDate() {
		cal=Calendar.getInstance();
		year=cal.get(Calendar.YEAR);       //获取年月日
		month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
		day=cal.get(Calendar.DAY_OF_MONTH);
	}
	//点击生日的TextView时触发的方法
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tvShowDialog:
				DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int month, int day) {
						tvShowDialog.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
					}
				};
				DatePickerDialog dialog=new DatePickerDialog(RegActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
				dialog.show();
				break;
			default:
				break;
		}
	}

	//方法：连接服务器，进行注册
	public void register(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//获得用户输入的数据并进行验证
				//EditText etStatus = (EditText)findViewById(R.id.etStatus);		//获得心情EditText对象
				String name = etName.getEditableText().toString().trim();		//获得昵称
				String pwd1 = etPwd1.getEditableText().toString().trim();		//获得密码
				String pwd2 = etPwd2.getEditableText().toString().trim();		//获得确认密码
				String email = etEmail.getEditableText().toString().trim();		//获得邮箱
				String ssex = (String) spsex.getSelectedItem(); //获得性别
				String birthday = tvShowDialog.getText().toString();//获得生日

	
				//String status = etStatus.getEditableText().toString().trim();	//获得状态
				//String status="0";
				//如果某个值为空
				if(name.equals("") || pwd1.equals("") || pwd2.equals("") || email.equals("") ){
					pd.dismiss();
					Toast.makeText(RegActivity.this, "请将注册信息填写完整", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//判断两次输入的密码是否一致
					pd.dismiss();
					Toast.makeText(RegActivity.this, "两次输入的密码不一致！", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);  //建立连接
					String regInfo = "<#REGISTER#>"+name+"|"+pwd1+"|"+email+"|"+ssex+"|"+birthday;
					mc.dout.writeUTF(regInfo);    //发送数据
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#REG_SUCCESS#>")){		//返回信息为注册成功
						result= result.substring(15);		//去掉信息头
						uno = result;				//记录用户的ID
						myHandler.sendEmptyMessage(0);				//发出Handler消息
						Toast.makeText(RegActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
					}
					else{		//注册失败
						Toast.makeText(RegActivity.this, "注册失败！请重试！", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(RegActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
						 Looper.loop();
						 Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(RegActivity.this, "连接超时", Toast.LENGTH_LONG).show();
						 Looper.loop();
						 Looper.myLooper().quit();
					 }	
				}
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}

	//按下返回按钮，回到登录界面
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//创建Intent对象
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//启动Activity
		finish();
	}
	
	//规划菜单栏
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.app_about).setIcon(R.drawable.help).setAlphabeticShortcut('H');
		menu.add(0, 2, 2, R.string.back).setIcon(R.drawable.backbefore).setAlphabeticShortcut('E');
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:
			openHelpDialog();
			break;
		case 2:
			openBackDialog();
			break;
		}
		return true;
	}
	
	//帮助信息显示
	private void openBackDialog() {
		Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//创建Intent对象
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//启动Activity
		finish();
	}
	
	//帮助
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) RegActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help_register, null);
		new AlertDialog.Builder(this)
				.setTitle(R.string.app_about)
				.setView(v)
				.setIcon(R.drawable.help)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {
							}
						}).show();
	}
}
