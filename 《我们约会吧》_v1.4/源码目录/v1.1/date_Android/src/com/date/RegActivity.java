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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegActivity extends Activity {
	MyConnector mc = null;			//����MyConnector����
	String uno = "";				//��¼�û���ID
	ProgressDialog pd= null;		//�������ȶԻ���
	
	private static final String[] dates_sex={"Ů","��"};
	private static final String[] dates_age={"70","80","90"};
	private Spinner spsex;
	private Spinner spage;
	private ArrayAdapter<CharSequence> adaptersex;
	private ArrayAdapter<CharSequence> adapterage;
	private EditText etName = null;
	private EditText etPwd1 = null;
	private EditText etPwd2 = null;
	private EditText etEmail = null;
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(RegActivity.this);
				builder.setTitle("ע��ɹ�")
					   .setMessage("ע��ɹ�������Լ���Ϊ��" + uno)
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(RegActivity.this,MainActivity.class);	//����Intent
							intent.putExtra("uno", uno);		//����Extra�ֶ�
							startActivity(intent);				//����FunctionTab
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
        etName = (EditText)findViewById(R.id.etName);			//����ǳ�EditText����
		etName.getBackground().setAlpha(210);
		etPwd1 = (EditText)findViewById(R.id.etPwd1);			//�������EditText����
		etPwd1.getBackground().setAlpha(210);
		etPwd2 = (EditText)findViewById(R.id.etPwd2);			//���ȷ������EditText����
		etPwd2.getBackground().setAlpha(210);
		etEmail = (EditText)findViewById(R.id.etEmail);		//�������EditText����
		etEmail.getBackground().setAlpha(210);
		
        spsex = (Spinner) findViewById(R.id.spSex);
        spsex.getBackground().setAlpha(150);
        adaptersex = new ArrayAdapter<CharSequence>(RegActivity.this,android.R.layout.simple_spinner_item,dates_sex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsex.setAdapter(adaptersex);
        
        spage = (Spinner) this.findViewById(R.id.spAge);
        spage.getBackground().setAlpha(150);
        adapterage = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,dates_age);
        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spage.setAdapter(adapterage);     
        
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);				//��÷��ذ�ť����
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
			
			public void onClick(View v) {
				Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//����Intent����
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				startActivity(intent);										//����Activity
				finish();
			}
		});
		
		ImageButton btnReg = (ImageButton)findViewById(R.id.btnReg);		//���ע��Button��ť����
		btnReg.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnReg.setPadding(8, 8, 8, 8);
		btnReg.getBackground().setAlpha(100);
		btnReg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pd = ProgressDialog.show(RegActivity.this, "���Ժ�...", "�������ӷ�����...", false);
				register();
			}
		});
    }
  //���������ӷ�����������ע��
	public void register(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//����û���������ݲ�������֤
//				DatePicker daBirthday = (DatePicker) findViewById(R.id.dpBirthday);
				
				//EditText etStatus = (EditText)findViewById(R.id.etStatus);		//�������EditText����
				String name = etName.getEditableText().toString().trim();		//����ǳ�
				String pwd1 = etPwd1.getEditableText().toString().trim();		//�������
				String pwd2 = etPwd2.getEditableText().toString().trim();		//���ȷ������
				String email = etEmail.getEditableText().toString().trim();		//�������
				String ssex = (String) spsex.getSelectedItem();
				String birthday = "19"+(String) spage.getSelectedItem()+"-"+"1-1";
				//String birthday = daBirthday.getYear()+"-"+daBirthday.getMonth()+"-"+daBirthday.getDayOfMonth();
				//String status = etStatus.getEditableText().toString().trim();	//���״̬
			//	String status="0";
				if(name.equals("") || pwd1.equals("") || pwd2.equals("") || email.equals("") ){
					pd.dismiss();
					Toast.makeText(RegActivity.this, "�뽫ע����Ϣ��д����", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//�ж���������������Ƿ�һ��
					pd.dismiss();
					Toast.makeText(RegActivity.this, "������������벻һ�£�", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					String regInfo = "<#REGISTER#>"+name+"|"+pwd1+"|"+email+"|"+ssex+"|"+birthday;
					mc.dout.writeUTF(regInfo);
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#REG_SUCCESS#>")){		//������ϢΪע��ɹ�
						result= result.substring(15);		//ȥ����Ϣͷ
						uno = result;				//��¼�û���ID
						myHandler.sendEmptyMessage(0);				//����Handler��Ϣ
						Toast.makeText(RegActivity.this, "ע��ɹ���", Toast.LENGTH_LONG).show();
					}
					else{		//ע��ʧ��
						Toast.makeText(RegActivity.this, "ע��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(RegActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(RegActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
		Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//����Intent����
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//����Activity
		finish();
	}
	
	//�滮�˵���
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
	
	//������Ϣ��ʾ
	private void openBackDialog() {
		Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//����Intent����
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//����Activity
		finish();
	}
	
	//����
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
