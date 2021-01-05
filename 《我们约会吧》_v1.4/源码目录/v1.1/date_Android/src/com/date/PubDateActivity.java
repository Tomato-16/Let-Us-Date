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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class PubDateActivity extends Activity{
	MyConnector mc = null;
	ProgressDialog pd;
	
	String uno = null;			//记录当前用户的id
	
	private static final String[] dates_style={"吃饭","逛街","打球","其他"};
	private static final String[] dates_sex={"女","男"};
	private static final String[] dates_age={"70","80","90"};
///	private static final String[] dates_distance={"1","5","10"};
	
	private Spinner spstyle;
	private Spinner spsex;
	private Spinner spage;
///	private Spinner spdistance;
	
	private ArrayAdapter<CharSequence> adapterstyle;
	private ArrayAdapter<CharSequence> adaptersex;
	private ArrayAdapter<CharSequence> adapterage;
///	private ArrayAdapter<CharSequence> adapterdistance;
	/// 声明一个文本框
	private EditText editStatusTest = null;
	
	Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(PubDateActivity.this);
				builder.setTitle("约会提示")
					   .setMessage("约会取消成功，您可以继续发布约会！")
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent
//							intent.putExtra("uno", uno);		//设置Extra字段
//							startActivity(intent);				//启动FunctionTab
//							finish();
						}
						   
					   });
				builder.create();
				builder.show();
				
				
				break;
				
			case 1:		
				
				AlertDialog.Builder builder1 = new AlertDialog.Builder(PubDateActivity.this);
				builder1.setTitle("约会提示")
					   .setMessage("约会取消失败")
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent
//							intent.putExtra("uno", uno);		//设置Extra字段
//							startActivity(intent);				//启动FunctionTab
//							finish();
						}
						   
					   });
				builder1.create();
				builder1.show();
				
				
				break;

			case 2:		
				
				AlertDialog.Builder builder2 = new AlertDialog.Builder(PubDateActivity.this);
				builder2.setTitle("约会提示")
					   .setMessage("您暂无已发布的约会")
					   .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent
//							intent.putExtra("uno", uno);		//设置Extra字段
//							startActivity(intent);				//启动FunctionTab
//							finish();
						}
						   
					   });
				builder2.create();
				builder2.show();
				
				
				break;
			}
			super.handleMessage(msg);
		}
	};
	
    /** Called when the activity is first created. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pub_date1);
        
        Intent intent = getIntent();			//获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno");		//获得当前用户的id
		
		/// 获得当前的EditTest对象
		editStatusTest = (EditText)findViewById(R.id.statusTest);
		editStatusTest.getBackground().setAlpha(210);
		
        spstyle = (Spinner) this.findViewById(R.id.spinnerStyle);
        spstyle.getBackground().setAlpha(150);
        spsex = (Spinner) this.findViewById(R.id.spinnerSex);
        spsex.getBackground().setAlpha(150);
        spage = (Spinner) this.findViewById(R.id.spinnerAge);
        spage.getBackground().setAlpha(150);
///        spdistance = (Spinner) this.findViewById(R.id.spinnerDistance);
///        spdistance.getBackground().setAlpha(150);
        
        adapterstyle = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_style);
        adapterstyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spstyle.setAdapter(adapterstyle);
    
        adaptersex = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_sex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsex.setAdapter(adaptersex);
        
        adapterage = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_age);
        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spage.setAdapter(adapterage);
        
///        adapterdistance = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_distance);
///        adapterdistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
///        spdistance.setAdapter(adapterdistance);
        
//        spstyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { 
//            @Override 
//            public void onItemSelected(AdapterView<?> arg0, View arg1, 
//                    int arg2, long arg3) { 
//                //被选中时候发生的动作 
//            } 
//            @Override 
//            public void onNothingSelected(AdapterView<?> arg0) { 
//            } 
//        });


        ImageButton btnSubmit = (ImageButton)findViewById(R.id.btnSubmit);
        btnSubmit.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnSubmit.setPadding(8, 8, 8, 8);
        btnSubmit.getBackground().setAlpha(100);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				pd = ProgressDialog.show(PubDateActivity.this, "请稍候", "正在连接服务器...", true, true);
				Submit();
			}
		});
        ImageButton btnCancel = (ImageButton)findViewById(R.id.btnCancel);
        btnCancel.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnCancel.setPadding(8, 8, 8, 8);
        btnCancel.getBackground().setAlpha(100);
        btnCancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pd = ProgressDialog.show(PubDateActivity.this, "请稍候", "正在连接服务器...", true, true);
				Cancel();
			}
		});
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);				//获得返回按钮对象
        btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
		
			public void onClick(View v) {
				Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent对象
				intent.putExtra("uno", uno);
				startActivity(intent);										//启动Activity
				finish();
			}
		});
		
		///添加一个按钮进行尝试
		ImageButton btnTest = (ImageButton)findViewById(R.id.btnTest); //获得按钮对象
		btnTest.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnTest.setPadding(8, 8, 8, 8);
		btnTest.getBackground().setAlpha(100);
		btnTest.setOnClickListener(new View.OnClickListener() {        //为返回按钮添加监听器
			
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PubDateActivity.this);
                builder.setTitle("标题");
                builder.setMessage("简单消息框");
                builder.setPositiveButton("确定", null);
                builder.show();
			}
		});
    }
  //方法：连接服务器进行提交
    public void Submit(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					
					
//					Cursor cursorstyle=(Cursor)spstyle.getSelectedItem();
//					String sstyle=cursorstyle.getString(1);
//					Cursor cursorsex=(Cursor)spsex.getSelectedItem();
//					String ssex=cursorsex.getString(1);
//					Cursor cursorage=(Cursor)spage.getSelectedItem();
//					String sage=cursorage.getString(1);
//					Cursor cursordistance=(Cursor)spdistance.getSelectedItem();
//					String sdiatance=cursordistance.getString(1);
					
					String sstyle = (String) spstyle.getSelectedItem();
					String ssex = (String) spsex.getSelectedItem();
					String sage = (String) spage.getSelectedItem();
					String sstatus = editStatusTest.getEditableText().toString().trim();		///获得昵称
///					String sdiatance = (String) spdistance.getSelectedItem();
					
					String msg = "<#PubDate#>"+sstyle+"|"+ssex+"|"+sage+"|"+sstatus+"|"+uno;					//组织要返回的字符串
					mc.dout.writeUTF(msg);										//发出消息
					String receivedMsg = mc.din.readUTF();		//读取服务器发来的消息
					pd.dismiss();
					if(receivedMsg.startsWith("<#PubDate_SUCCESS#>")){	//收到的消息为登录成功消息
//						receivedMsg = receivedMsg.substring(19);
//						String [] sa = receivedMsg.split("\\|");
//						
						//转到功能面板
						Intent intent = new Intent(PubDateActivity.this,DateListActivity.class);
						intent.putExtra("uno", uno);
						startActivity(intent);						//启动功能Activity
						finish();
					}
					else if(receivedMsg.startsWith("<#PubDate_FAIL#>")){					//收到的消息为发布失败
						Toast.makeText(PubDateActivity.this, "发布失败", Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
					else if(receivedMsg.startsWith("<#PubDate_ONCE#>")){					//收到的消息为用户已发布信息
						Toast.makeText(PubDateActivity.this, "您已发布过约会", Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}	
					
				}catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(PubDateActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(PubDateActivity.this, "连接超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();						 
					 }	
				}
    		}
    	}.start();
    }
    public void Cancel(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
										
					String msg = "<#CancelDate#>"+uno;					//组织要返回的字符串
					mc.dout.writeUTF(msg);										//发出消息
					String receivedMsg = mc.din.readUTF();		//读取服务器发来的消息
					pd.dismiss();
					if(receivedMsg.startsWith("<#CancelDate_SUCCESS#>")){	//收到的消息为取消约会成功消息
//						receivedMsg = receivedMsg.substring(19);
//						String [] sa = receivedMsg.split("\\|");
//						
						myHandler.sendEmptyMessage(0);				//发出Handler消息
						
					}
					else if(receivedMsg.startsWith("<#CancelDate_FAIL#>")){					//收到的消息为发布失败
						myHandler.sendEmptyMessage(1);				//发出Handler消息
						
					}
					else if(receivedMsg.startsWith("<#PubDate_NEVER#>")){					//收到的消息为用户已发布信息
						myHandler.sendEmptyMessage(2);				//发出Handler消息
						
					}	
					
				}catch(Exception e){
					e.printStackTrace();
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
    	Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent对象
		intent.putExtra("uno", uno);
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
		Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//创建Intent对象
		intent.putExtra("uno", uno);
		startActivity(intent);										//启动Activity
		finish();
	}
	
	//帮助
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) PubDateActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help_pubdate, null);
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
