package com.date;

import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.date.welcome.WelcomeDateActivity;

public class LoginActivity extends Activity {
	MyConnector mc = null;
	ProgressDialog pd;
	
	private EditText etUid = null;
	private EditText etPwd = null;
	private CheckBox cb = null;
	
//	Bitmap head = null;	//存放头像
//	String [] infoList = null;	//存放用户的信息：id、姓名、性别、头像
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null){
        	Intent intent = new Intent(this, WelcomeDateActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		this.startActivity(intent);
        }
        setContentView(R.layout.login);
        
        etUid = (EditText)findViewById(R.id.etUid);	//获得帐号EditText
        etUid.getBackground().setAlpha(210);
		etPwd = (EditText)findViewById(R.id.etPwd);	//获得密码EditText
		etPwd.getBackground().setAlpha(210);
		
		cb = (CheckBox)findViewById(R.id.cbRemember);		//获得CheckBox对象
		
        TextView btnReg = (TextView)findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
//        Button btnLogin = (Button)findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
        checkIfRemember();
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.getBackground().setAlpha(180);
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pd = ProgressDialog.show(LoginActivity.this, "请稍候", "正在连接服务器...", true, true);
				login();
			}
		});
        
        TextView ibAnonymity = (TextView)findViewById(R.id.ibAnonymity);
        ibAnonymity.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				intent.putExtra("uno", "0");
				startActivity(intent);
				finish();
			}
		});
        TextView ibExit = (TextView)findViewById(R.id.ibExit);
        ibExit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				openExitDialog();	//结束进程，退出程序
			}
		});
    }
    //方法：连接服务器进行登录
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String uid = etUid.getEditableText().toString();	//获得输入的帐号
					String pwd = etPwd.getEditableText().toString();	//获得输入的密码
					if(uid.equals("") || pwd.equals("")){		//判断输入是否为空
						pd.dismiss();
						//Toast.makeText(LoginActivity.this, "请输入帐号或密码!", Toast.LENGTH_SHORT).show();//输出提示消息
						new AlertDialog.Builder(LoginActivity.this).setMessage("请输入帐号或密码!") 
					    .setPositiveButton("Ok", null) 
					    .show(); 
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//组织要返回的字符串
					mc.dout.writeUTF(msg);										//发出消息
					String receivedMsg = mc.din.readUTF();		//读取服务器发来的消息
					pd.dismiss();
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//收到的消息为登录成功消息
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
						
//						mc.dout.writeUTF("<#UserInfo#>"+uid);		//向服务器发出请求	
//						
//						String fInfo = mc.din.readUTF();		//读取好友信息
//						infoList = fInfo.split("\\|");		//分割字符串
//							
//						
//								
//						int headSize = mc.din.readInt();		//读取头像大小
//						byte[] buf = new byte[headSize];			//创建缓冲区
//						mc.din.read(buf);						//读取头像信息
//						head = BitmapFactory.decodeByteArray(buf, 0, headSize);
//						
						//转到功能面板
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						intent.putExtra("uno", sa[0]);
						startActivity(intent);						//启动功能Activity
						finish();
//						Intent intent = new Intent(LoginActivity.this,index.class);
//						intent.putExtra("uno", sa[0]);
//						startActivity(intent);						//启动功能Activity
//						finish();
					}
					else if(receivedMsg.startsWith("<#LOGIN_FAIL#>")){					//收到的消息为登录失败
						Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入~~~~~", Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				}
//				catch(SocketTimeoutException se){
//					 if (!mc.socket.isClosed() && mc.socket.isConnected()){
//						 Toast.makeText(LoginActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();
//					 }
//					 else{
//						 Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();						 
//					 }					
//				}
//				catch(InterruptedIOException ie){
//					if (!mc.socket.isClosed() && mc.socket.isConnected()){
//						 Toast.makeText(LoginActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();
//					 }
//					 else{
//						 Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();						 
//					 }	
//				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(LoginActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();						 
					 }	
				}
    		}
    	}.start();
    }
    
    //方法：将用户的id和密码存入Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
    	SharedPreferences.Editor editor = sp.edit();			//获得Editor
    	editor.putString("uid", uid);							//将用户名存入Preferences
    	editor.putString("pwd", pwd);							//将密码存入Preferences
    	editor.commit();
    }
    
    //方法：从Preferences中读取用户名和密码
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
    	String uid = sp.getString("uid", null);
    	String pwd = sp.getString("pwd", null);
    	if(uid != null && pwd!= null){
    		EditText etUid = (EditText)findViewById(R.id.etUid);
    		EditText etPwd = (EditText)findViewById(R.id.etPwd);
    		CheckBox cbRemember = (CheckBox)findViewById(R.id.cbRemember);
    		etUid.setText(uid);
    		etPwd.setText(pwd);
    		cbRemember.setChecked(true);
    	}
    }
    
    //规划菜单栏
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.about).setIcon(R.drawable.about2).setAlphabeticShortcut('A');
		menu.add(0, 2, 2, R.string.app_about).setIcon(R.drawable.help).setAlphabeticShortcut('H');
		menu.add(0, 3, 3, R.string.exit).setIcon(R.drawable.exit1).setAlphabeticShortcut('E');
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:
			openAboutDialog();
			break;
		case 2:
			openHelpDialog();
			break;
		case 3:
			openExitDialog();
			break;
		}
		return true;
	}
	
	//帮助信息显示
	private void openAboutDialog() {
		LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.about, null);
		new AlertDialog.Builder(this)
		.setTitle(R.string.about)
		.setIcon(R.drawable.about2)
		.setView(v)
		.show();
	}
	
	//帮助
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help, null);
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
	
 // 退出对话框
	private void openExitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setIcon(R.drawable.exit)
				.setMessage(R.string.app_exit_msg)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								android.os.Process.killProcess(android.os.Process.myPid());	
								//finish();
							}
						}).show();
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
		// TODO Auto-generated method stub
		openExitDialog();
	}
}
