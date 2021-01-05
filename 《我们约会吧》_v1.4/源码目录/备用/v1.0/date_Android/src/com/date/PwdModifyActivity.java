package com.date;

import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PwdModifyActivity extends Activity {
	MyConnector mc = null;			//声明MyConnector对象
	String uno = "";				//记录用户的ID
	ProgressDialog pd= null;		//声明进度对话框
	String [] infoList = null;	//存放用户的信息：id、姓名、性别、头像
	
	private EditText etOldPwd = null;
	private EditText etPwd1 = null;
	private EditText etPwd2 = null;

	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(PwdModifyActivity.this);
				builder.setTitle("修改密码成功")
					   .setMessage("修改密码成功")
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(PwdModifyActivity.this,MainActivity.class);	//创建Intent
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
        setContentView(R.layout.modify_pwd);
        
        Intent intent = getIntent();			//获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno");		//获得当前用户的id
		
        etOldPwd = (EditText)findViewById(R.id.etOldPwd);			//获得昵称EditText对象
        etOldPwd.getBackground().setAlpha(210);
		etPwd1 = (EditText)findViewById(R.id.etPwd1);			//获得密码EditText对象
		etPwd1.getBackground().setAlpha(210);
		etPwd2 = (EditText)findViewById(R.id.etPwd2);			//获得确认密码EditText对象
		etPwd2.getBackground().setAlpha(210);
		
               
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnPwdBack);				//获得返回按钮对象
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PwdModifyActivity.this,MainActivity.class);	//创建Intent对象
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				intent.putExtra("uno", uno);
				startActivity(intent);										//启动Activity
				finish();
			}
		});
		
		ImageButton btnModifyPwd = (ImageButton)findViewById(R.id.btnPwdModify);		//获得注册Button按钮对象
		btnModifyPwd.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnModifyPwd.setPadding(8, 8, 8, 8);
		btnModifyPwd.getBackground().setAlpha(100);
		btnModifyPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(PwdModifyActivity.this, "请稍候...", "正在连接服务器...", false);
				modify_pwd();
			}
		});
    }
  //方法：连接服务器，进行修改密码
	public void modify_pwd(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//获得用户输入的数据并进行验证
//				DatePicker daBirthday = (DatePicker) findViewById(R.id.dpBirthday);
				
				//EditText etStatus = (EditText)findViewById(R.id.etStatus);		//获得心情EditText对象
				String oldPwd = etOldPwd.getEditableText().toString().trim();		//获得原密码
				String pwd1 = etPwd1.getEditableText().toString().trim();		//获得密码
				String pwd2 = etPwd2.getEditableText().toString().trim();		//获得确认密码
					//String birthday = daBirthday.getYear()+"-"+daBirthday.getMonth()+"-"+daBirthday.getDayOfMonth();
				//String status = etStatus.getEditableText().toString().trim();	//获得状态
			//	String status="0";
				if(oldPwd.equals("") || pwd1.equals("") || pwd2.equals("") ){
					Toast.makeText(PwdModifyActivity.this, "请将修改信息填写完整", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//判断两次输入的密码是否一致
					Toast.makeText(PwdModifyActivity.this, "两次输入的密码不一致！", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#UserInfo#>"+uno);		//向服务器发出请求	
					
					String fInfo = mc.din.readUTF();		//读取好友信息
					infoList = fInfo.split("\\|");		//分割字符串
					
					String pwd = infoList[8].toString().trim();
					if(oldPwd.equals(pwd)){
						String modifyInfo = "<#PWD_MODIFY#>"+uno+"|"+pwd1;
						MyConnector mc2 = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
						mc2.dout.writeUTF(modifyInfo);
						String result = mc2.din.readUTF();
						pd.dismiss();
						if(result.equals("<#PWD_MODIFY_SUCCESS#>")){		//返回信息为修改成功
//							result= result.substring(15);		//去掉信息头
//							
							myHandler.sendEmptyMessage(0);				//发出Handler消息
							Toast.makeText(PwdModifyActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
							
//							Intent intent = new Intent(PwdModifyActivity.this, MainActivity.class);
//							intent.putExtra("uno", uno);
//							startActivity(intent);
//							finish();
						}
						else{		//修改失败
							Toast.makeText(PwdModifyActivity.this, "修改失败！请重试！", Toast.LENGTH_LONG).show();
						}
					}
					
				
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(PwdModifyActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(PwdModifyActivity.this, "连接超时", Toast.LENGTH_LONG).show();
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
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PwdModifyActivity.this,LoginActivity.class);	//创建Intent对象
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//启动Activity
		finish();
	}
	
	
}
