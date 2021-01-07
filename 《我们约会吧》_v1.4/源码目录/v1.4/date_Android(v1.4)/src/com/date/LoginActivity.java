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
	
//	Bitmap head = null;	//���ͷ��
//	String [] infoList = null;	//����û�����Ϣ��id���������Ա�ͷ��
	
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
        
        etUid = (EditText)findViewById(R.id.etUid);	//����ʺ�EditText
        etUid.getBackground().setAlpha(210);
		etPwd = (EditText)findViewById(R.id.etPwd);	//�������EditText
		etPwd.getBackground().setAlpha(210);
		
		cb = (CheckBox)findViewById(R.id.cbRemember);		//���CheckBox����
		
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
				pd = ProgressDialog.show(LoginActivity.this, "���Ժ�", "�������ӷ�����...", true, true);
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
				openExitDialog();	//�������̣��˳�����
			}
		});
    }
    //���������ӷ��������е�¼
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String uid = etUid.getEditableText().toString();	//���������ʺ�
					String pwd = etPwd.getEditableText().toString();	//������������
					if(uid.equals("") || pwd.equals("")){		//�ж������Ƿ�Ϊ��
						pd.dismiss();
						//Toast.makeText(LoginActivity.this, "�������ʺŻ�����!", Toast.LENGTH_SHORT).show();//�����ʾ��Ϣ
						new AlertDialog.Builder(LoginActivity.this).setMessage("�������ʺŻ�����!") 
					    .setPositiveButton("Ok", null) 
					    .show(); 
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//��֯Ҫ���ص��ַ���
					mc.dout.writeUTF(msg);										//������Ϣ
					String receivedMsg = mc.din.readUTF();		//��ȡ��������������Ϣ
					pd.dismiss();
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//�յ�����ϢΪ��¼�ɹ���Ϣ
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
						
//						mc.dout.writeUTF("<#UserInfo#>"+uid);		//���������������	
//						
//						String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
//						infoList = fInfo.split("\\|");		//�ָ��ַ���
//							
//						
//								
//						int headSize = mc.din.readInt();		//��ȡͷ���С
//						byte[] buf = new byte[headSize];			//����������
//						mc.din.read(buf);						//��ȡͷ����Ϣ
//						head = BitmapFactory.decodeByteArray(buf, 0, headSize);
//						
						//ת���������
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						intent.putExtra("uno", sa[0]);
						startActivity(intent);						//��������Activity
						finish();
//						Intent intent = new Intent(LoginActivity.this,index.class);
//						intent.putExtra("uno", sa[0]);
//						startActivity(intent);						//��������Activity
//						finish();
					}
					else if(receivedMsg.startsWith("<#LOGIN_FAIL#>")){					//�յ�����ϢΪ��¼ʧ��
						Toast.makeText(LoginActivity.this, "�û����������������������~~~~~", Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				}
//				catch(SocketTimeoutException se){
//					 if (!mc.socket.isClosed() && mc.socket.isConnected()){
//						 Toast.makeText(LoginActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();
//					 }
//					 else{
//						 Toast.makeText(LoginActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();						 
//					 }					
//				}
//				catch(InterruptedIOException ie){
//					if (!mc.socket.isClosed() && mc.socket.isConnected()){
//						 Toast.makeText(LoginActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();
//					 }
//					 else{
//						 Toast.makeText(LoginActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
//							Looper.loop();
//							Looper.myLooper().quit();						 
//					 }	
//				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(LoginActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(LoginActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();						 
					 }	
				}
    		}
    	}.start();
    }
    
    //���������û���id���������Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
    	SharedPreferences.Editor editor = sp.edit();			//���Editor
    	editor.putString("uid", uid);							//���û�������Preferences
    	editor.putString("pwd", pwd);							//���������Preferences
    	editor.commit();
    }
    
    //��������Preferences�ж�ȡ�û���������
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
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
    
    //�滮�˵���
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
	
	//������Ϣ��ʾ
	private void openAboutDialog() {
		LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.about, null);
		new AlertDialog.Builder(this)
		.setTitle(R.string.about)
		.setIcon(R.drawable.about2)
		.setView(v)
		.show();
	}
	
	//����
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
	
 // �˳��Ի���
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
