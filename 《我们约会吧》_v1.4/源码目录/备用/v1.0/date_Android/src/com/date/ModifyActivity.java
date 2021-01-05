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
	MyConnector mc = null;			//����MyConnector����
	String uno = "";				//��¼�û���ID
	ProgressDialog pd= null;		//�������ȶԻ���
	String [] infoList = null;	//����û�����Ϣ��id�����������䡢�Ա�����
	
	private static final String[] dates_sex={"Ů","��"};
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
				 etName = (EditText)findViewById(R.id.etNameModify);			//����ǳ�EditText����
					etName.getBackground().setAlpha(210);
					
					etEmail = (EditText)findViewById(R.id.etEmailModify);		//�������EditText����
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
				builder.setTitle("������Ϣ�޸ĳɹ�")
					   .setMessage("������Ϣ�޸ĳɹ�")
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(ModifyActivity.this,MainActivity.class);	//����Intent
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
        setContentView(R.layout.modify);
        
        Intent intent = getIntent();			//���������Activity��Intent����
		uno = intent.getStringExtra("uno");		//��õ�ǰ�û���id
        
//        etName = (EditText)findViewById(R.id.etNameModify);			//����ǳ�EditText����
//		etName.getBackground().setAlpha(210);
//
//		etEmail = (EditText)findViewById(R.id.etEmailModify);		//�������EditText����
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
        
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnModifyBack);				//��÷��ذ�ť����
    	btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ModifyActivity.this,MainActivity.class);	//����Intent����
				Bundle bundle = new Bundle();
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				intent.putExtra("uno", uno);
				startActivity(intent);										//����Activity
				finish();
			}
		});
		
		ImageButton btnModify = (ImageButton)findViewById(R.id.btnModify);		//���ע��Button��ť����
		btnModify.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnModify.setPadding(8, 8, 8, 8);
		btnModify.getBackground().setAlpha(100);
		btnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(ModifyActivity.this, "���Ժ�...", "�������ӷ�����...", false);
				modify();
			}
		});
		
		getUser();
    }
  //���������ӷ������������޸�
	public void modify(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//����û���������ݲ�������֤
//			
				String name = etName.getEditableText().toString().trim();		//����ǳ�				
				String email = etEmail.getEditableText().toString().trim();		//�������
				String ssex = (String) spsex.getSelectedItem();
				String birthday = "19"+(String) spage.getSelectedItem()+"-"+"1-1";
				
				if(name.equals("") || email.equals("") ){
					Toast.makeText(ModifyActivity.this, "�뽫�޸���Ϣ��д����", Toast.LENGTH_LONG).show();
					return;
				}
				
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					String modifyInfo = "<#INFO_MODIFY#>"+uno+"|"+name+"|"+email+"|"+ssex+"|"+birthday;
					mc.dout.writeUTF(modifyInfo);
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#INFO_MODIFY_SUCCESS#>")){		//������ϢΪ�޸ĳɹ�
						
						myHandler2.sendEmptyMessage(0);				//����Handler��Ϣ
						Toast.makeText(ModifyActivity.this, "�޸ĸ�����Ϣ�ɹ���", Toast.LENGTH_LONG).show();
					}
					else{		//�޸�ʧ��
						Toast.makeText(ModifyActivity.this, "�޸�ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ModifyActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ModifyActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
	
	//��������ȡ�û���Ϣ
	public void getUser(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//����MyConnector����
					mc.dout.writeUTF("<#UserInfo#>"+uno);		//���������������	
					
					String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
					infoList = fInfo.split("\\|");		//�ָ��ַ���
			
					
					myHandler.sendEmptyMessage(0);				//����Handler��Ϣ
				
						
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
		Intent intent = new Intent(ModifyActivity.this,LoginActivity.class);	//����Intent����
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		startActivity(intent);										//����Activity
		finish();
	}
}
