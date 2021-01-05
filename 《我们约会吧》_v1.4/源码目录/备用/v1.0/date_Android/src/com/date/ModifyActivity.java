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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyActivity extends Activity {
	MyConnector mc = null;			//声明MyConnector对象
	String uno = "";				//记录用户的ID
	ProgressDialog pd= null;		//声明进度对话框
	String [] infoList = null;	//存放用户的信息：id、姓名、邮箱、性别、年龄
	
	private static final String[] dates_sex={"女","男"};
	private static final String[] dates_age={"70","80","90"};
	private Spinner spsex;
	private Spinner spage;
	private ArrayAdapter<CharSequence> adaptersex;
	private ArrayAdapter<CharSequence> adapterage;
	private EditText etName = null;
	private EditText etEmail = null;
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:	
				 etName = (EditText)findViewById(R.id.etNameModify);			//获得昵称EditText对象
					etName.getBackground().setAlpha(210);
					
					etEmail = (EditText)findViewById(R.id.etEmailModify);		//获得邮箱EditText对象
					etEmail.getBackground().setAlpha(210);
					
			        spsex = (Spinner) findViewById(R.id.spSexModify);
			        spsex.getBackground().setAlpha(150);
			        adaptersex = new ArrayAdapter<CharSequence>(ModifyActivity.this,android.R.layout.simple_spinner_item,dates_sex);
			        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			        spsex.setAdapter(adaptersex);
//					spsex.setSelection(1,true);
			        
			        spage = (Spinner) findViewById(R.id.spAgeModify);
			        spage.getBackground().setAlpha(150);
			        adapterage = new ArrayAdapter<CharSequence>(ModifyActivity.this,android.R.layout.simple_spinner_item,dates_age);
			        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        
			        
				etName.setText(infoList[1]);			    
				etEmail.setText(infoList[2]);
				for(int i=0;i<2;i++){
					if(infoList[4].equals(dates_sex[i])){
						spsex.setAdapter(adaptersex);
						spsex.setSelection(i,true);
					}
				}
				String age = infoList[5].substring(2, 3)+"0";
				for(int i=0;i<3;i++){
					if(age.equals(dates_age[i])){
						spage.setAdapter(adapterage);
						spage.setSelection(i);
					}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	Handler myHandler2 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
				builder.setTitle("个人信息修改成功")
					   .setMessage("个人信息修改成功")
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(ModifyActivity.this,MainActivity.class);	//创建Intent
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
        setContentView(R.layout.modify);
        
        Intent intent = getIntent();			//获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno");		//获得当前用户的id
        
//        etName = (EditText)findViewById(R.id.etNameModify);			//获得昵称EditText对象
//		etName.getBackground().setAlpha(210);
//
//		etEmail = (EditText)findViewById(R.id.etEmailModify);		//获得邮箱EditText对象
//		etEmail.getBackground().setAlpha(210);
//		
//        spsex = (Spinner) findViewById(R.id.spSexModify);
//        spsex.getBackground().setAlpha(150);
//        adaptersex = new ArrayAdapter<CharSequence>(ModifyActivity.this,android.R.layout.simple_spinner_item,dates_sex);
//        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spsex.setAdapter(adaptersex);
//        
//        spage = (Spinner) this.findViewById(R.id.spAgeModify);
//        spage.getBackground().setAlpha(150);
//        adapterage = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,dates_age);
//        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spage.setAdapter(adapterage);     
        
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnModifyBack);				//获得返回按钮对象
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ModifyActivity.this,MainActivity.class);	//创建Intent对象
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				intent.putExtra("uno", uno);
				startActivity(intent);										//启动Activity
				finish();
			}
		});
		
		ImageButton btnModify = (ImageButton)findViewById(R.id.btnModify);		//获得注册Button按钮对象
		btnModify.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnModify.setPadding(8, 8, 8, 8);
		btnModify.getBackground().setAlpha(100);
		btnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(ModifyActivity.this, "请稍候...", "正在连接服务器...", false);
				modify();
			}
		});
		
		getUser();
    }
  //方法：连接服务器，进行修改
	public void modify(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//获得用户输入的数据并进行验证
//			
				String name = etName.getEditableText().toString().trim();		//获得昵称				
				String email = etEmail.getEditableText().toString().trim();		//获得邮箱
				String ssex = (String) spsex.getSelectedItem();
				String birthday = "19"+(String) spage.getSelectedItem()+"-"+"1-1";
				
				if(name.equals("") || email.equals("") ){
					Toast.makeText(ModifyActivity.this, "请将修改信息填写完整", Toast.LENGTH_LONG).show();
					return;
				}
				
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					String modifyInfo = "<#INFO_MODIFY#>"+uno+"|"+name+"|"+email+"|"+ssex+"|"+birthday;
					mc.dout.writeUTF(modifyInfo);
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#INFO_MODIFY_SUCCESS#>")){		//返回信息为修改成功
						
						myHandler2.sendEmptyMessage(0);				//发出Handler消息
						Toast.makeText(ModifyActivity.this, "修改个人信息成功！", Toast.LENGTH_LONG).show();
					}
					else{		//修改失败
						Toast.makeText(ModifyActivity.this, "修改失败！请重试！", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ModifyActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ModifyActivity.this, "连接超时", Toast.LENGTH_LONG).show();
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
	
	//方法：获取用户信息
	public void getUser(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//创建MyConnector对象
					mc.dout.writeUTF("<#UserInfo#>"+uno);		//向服务器发出请求	
					
					String fInfo = mc.din.readUTF();		//读取好友信息
					infoList = fInfo.split("\\|");		//分割字符串
			
					
					myHandler.sendEmptyMessage(0);				//发出Handler消息
				
						
//					if(!latitude.equals(null)&&!longitude.equals(null)){
//					//	
//					}
//						
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ModifyActivity.this,LoginActivity.class);	//创建Intent对象
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//启动Activity
		finish();
	}
}
