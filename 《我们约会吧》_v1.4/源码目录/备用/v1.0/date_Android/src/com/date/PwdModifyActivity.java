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
	MyConnector mc = null;			//����MyConnector����
	String uno = "";				//��¼�û���ID
	ProgressDialog pd= null;		//�������ȶԻ���
	String [] infoList = null;	//����û�����Ϣ��id���������Ա�ͷ��
	
	private EditText etOldPwd = null;
	private EditText etPwd1 = null;
	private EditText etPwd2 = null;

	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(PwdModifyActivity.this);
				builder.setTitle("�޸�����ɹ�")
					   .setMessage("�޸�����ɹ�")
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(PwdModifyActivity.this,MainActivity.class);	//����Intent
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
        setContentView(R.layout.modify_pwd);
        
        Intent intent = getIntent();			//���������Activity��Intent����
		uno = intent.getStringExtra("uno");		//��õ�ǰ�û���id
		
        etOldPwd = (EditText)findViewById(R.id.etOldPwd);			//����ǳ�EditText����
        etOldPwd.getBackground().setAlpha(210);
		etPwd1 = (EditText)findViewById(R.id.etPwd1);			//�������EditText����
		etPwd1.getBackground().setAlpha(210);
		etPwd2 = (EditText)findViewById(R.id.etPwd2);			//���ȷ������EditText����
		etPwd2.getBackground().setAlpha(210);
		
               
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnPwdBack);				//��÷��ذ�ť����
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PwdModifyActivity.this,MainActivity.class);	//����Intent����
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				intent.putExtra("uno", uno);
				startActivity(intent);										//����Activity
				finish();
			}
		});
		
		ImageButton btnModifyPwd = (ImageButton)findViewById(R.id.btnPwdModify);		//���ע��Button��ť����
		btnModifyPwd.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnModifyPwd.setPadding(8, 8, 8, 8);
		btnModifyPwd.getBackground().setAlpha(100);
		btnModifyPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(PwdModifyActivity.this, "���Ժ�...", "�������ӷ�����...", false);
				modify_pwd();
			}
		});
    }
  //���������ӷ������������޸�����
	public void modify_pwd(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//����û���������ݲ�������֤
//				DatePicker daBirthday = (DatePicker) findViewById(R.id.dpBirthday);
				
				//EditText etStatus = (EditText)findViewById(R.id.etStatus);		//�������EditText����
				String oldPwd = etOldPwd.getEditableText().toString().trim();		//���ԭ����
				String pwd1 = etPwd1.getEditableText().toString().trim();		//�������
				String pwd2 = etPwd2.getEditableText().toString().trim();		//���ȷ������
					//String birthday = daBirthday.getYear()+"-"+daBirthday.getMonth()+"-"+daBirthday.getDayOfMonth();
				//String status = etStatus.getEditableText().toString().trim();	//���״̬
			//	String status="0";
				if(oldPwd.equals("") || pwd1.equals("") || pwd2.equals("") ){
					Toast.makeText(PwdModifyActivity.this, "�뽫�޸���Ϣ��д����", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//�ж���������������Ƿ�һ��
					Toast.makeText(PwdModifyActivity.this, "������������벻һ�£�", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#UserInfo#>"+uno);		//���������������	
					
					String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
					infoList = fInfo.split("\\|");		//�ָ��ַ���
					
					String pwd = infoList[8].toString().trim();
					if(oldPwd.equals(pwd)){
						String modifyInfo = "<#PWD_MODIFY#>"+uno+"|"+pwd1;
						MyConnector mc2 = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
						mc2.dout.writeUTF(modifyInfo);
						String result = mc2.din.readUTF();
						pd.dismiss();
						if(result.equals("<#PWD_MODIFY_SUCCESS#>")){		//������ϢΪ�޸ĳɹ�
//							result= result.substring(15);		//ȥ����Ϣͷ
//							
							myHandler.sendEmptyMessage(0);				//����Handler��Ϣ
							Toast.makeText(PwdModifyActivity.this, "�޸ĳɹ���", Toast.LENGTH_LONG).show();
							
//							Intent intent = new Intent(PwdModifyActivity.this, MainActivity.class);
//							intent.putExtra("uno", uno);
//							startActivity(intent);
//							finish();
						}
						else{		//�޸�ʧ��
							Toast.makeText(PwdModifyActivity.this, "�޸�ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
						}
					}
					
				
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(PwdModifyActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(PwdModifyActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
		Intent intent = new Intent(PwdModifyActivity.this,LoginActivity.class);	//����Intent����
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//����Activity
		finish();
	}
	
	
}
