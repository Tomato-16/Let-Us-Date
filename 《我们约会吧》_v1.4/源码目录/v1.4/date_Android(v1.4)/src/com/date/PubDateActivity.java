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
	
	//��¼��ǰ�û���id
	String uno = null;
	
	//Լ�᷽ʽ���Ա��Լ����������
	private static final String[] dates_style={"�Է�","���","�˶�","����Ӱ","����"};
	private static final String[] dates_sex={"Ů","��"};
	private static final String[] dates_age={"70","80","90"};
	
	//���ڿؼ�spinner������ѡȡ��Ϣ
	private Spinner spstyle;
	private Spinner spsex;
	private Spinner spage;
	/// ����һ���ı�������������̬
	private EditText editStatusTest = null;
	
	private ArrayAdapter<CharSequence> adapterstyle;
	private ArrayAdapter<CharSequence> adaptersex;
	private ArrayAdapter<CharSequence> adapterage;
	
	/*
	 * handler ����������Ϣ�����߳��м䷢����Ϣ
	 */
	Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			//���ݽ��յ�����Ϣ������ͬ����ʾ��
			switch(msg.what){
			case 0:		
				
				AlertDialog.Builder builder = new AlertDialog.Builder(PubDateActivity.this);
				builder.setTitle("Լ����ʾ")
					   .setMessage("Լ��ȡ���ɹ��������Լ�������Լ�ᣡ")
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent
//							intent.putExtra("uno", uno);		//����Extra�ֶ�
//							startActivity(intent);				//����FunctionTab
//							finish();
						}
						   
					   });
				builder.create();
				builder.show();
				
				
				break;
				
			case 1:		
				
				AlertDialog.Builder builder1 = new AlertDialog.Builder(PubDateActivity.this);
				builder1.setTitle("Լ����ʾ")
					   .setMessage("Լ��ȡ��ʧ��")
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent
//							intent.putExtra("uno", uno);		//����Extra�ֶ�
//							startActivity(intent);				//����FunctionTab
//							finish();
						}
						   
					   });
				builder1.create();
				builder1.show();
				
				
				break;

			case 2:		
				
				AlertDialog.Builder builder2 = new AlertDialog.Builder(PubDateActivity.this);
				builder2.setTitle("Լ����ʾ")
					   .setMessage("�������ѷ�����Լ��")
					   .setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

					
						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent
//							intent.putExtra("uno", uno);		//����Extra�ֶ�
//							startActivity(intent);				//����FunctionTab
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
        
        Intent intent = getIntent();			//���������Activity��Intent����
		uno = intent.getStringExtra("uno");		//��õ�ǰ�û���id
		
		/// ��õ�ǰ��EditTest����
		editStatusTest = (EditText)findViewById(R.id.statusTest);
		editStatusTest.getBackground().setAlpha(210);
		// ��õ�ǰ��Spinner����
        spstyle = (Spinner) this.findViewById(R.id.spinnerStyle);
        spstyle.getBackground().setAlpha(150);
        spsex = (Spinner) this.findViewById(R.id.spinnerSex);
        spsex.getBackground().setAlpha(150);
        spage = (Spinner) this.findViewById(R.id.spinnerAge);
        spage.getBackground().setAlpha(150);
        //���ArrayAdapter���󣬺�Spinner��������
        adapterstyle = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_style);
        adapterstyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spstyle.setAdapter(adapterstyle);
    
        adaptersex = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_sex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsex.setAdapter(adaptersex);
        
        adapterage = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates_age);
        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spage.setAdapter(adapterage);

        // ��ȡlayout�е��ύ��ť�����ò���
        ImageButton btnSubmit = (ImageButton)findViewById(R.id.btnSubmit);
        btnSubmit.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnSubmit.setPadding(8, 8, 8, 8);
        btnSubmit.getBackground().setAlpha(100);
        /*
                         * �������������ʱ���иú���
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				pd = ProgressDialog.show(PubDateActivity.this, "���Ժ�", "�������ӷ�����...", true, true);
				Submit();
			}
		});
        // ��ȡlayout�е�ȡ����ť�����ò���
        ImageButton btnCancel = (ImageButton)findViewById(R.id.btnCancel);
        btnCancel.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnCancel.setPadding(8, 8, 8, 8);
        btnCancel.getBackground().setAlpha(100);
        /*
		    * �������������ʱ���иú���
		*/
        btnCancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pd = ProgressDialog.show(PubDateActivity.this, "���Ժ�", "�������ӷ�����...", true, true);
				Cancel();
			}
		});
        // ��ȡlayout�еķ��ذ�ť�����ò���
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        btnBack.setPadding(8, 8, 8, 8);
        btnBack.getBackground().setAlpha(100);
        /*
		    * �������������ʱ���иú���
		*/
		btnBack.setOnClickListener(new View.OnClickListener() {	
		
			public void onClick(View v) {
				Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent����
				intent.putExtra("uno", uno);
				startActivity(intent);										//����Activity
				finish();
			}
		});
    }
  //���������ӷ����������ύ
    public void Submit(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					//��������
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					
					// ���ַ�����ʽ��ȡ���������
					String sstyle = (String) spstyle.getSelectedItem();
					String ssex = (String) spsex.getSelectedItem();
					String sage = (String) spage.getSelectedItem();
					String sstatus = editStatusTest.getEditableText().toString().trim(); 
					
					//��֯Ҫ���͵��ַ���
					String msg = "<#PubDate#>"+sstyle+"|"+ssex+"|"+sage+"|"+sstatus+"|"+uno;
					mc.dout.writeUTF(msg);										//������Ϣ
					String receivedMsg = mc.din.readUTF();		//��ȡ��������������Ϣ
					pd.dismiss();
					if(receivedMsg.startsWith("<#PubDate_SUCCESS#>")){	//�յ�����ϢΪ��¼�ɹ���Ϣ
						//ת���������
						Intent intent = new Intent(PubDateActivity.this,DateListActivity.class);
						intent.putExtra("uno", uno);
						startActivity(intent);						//��������Activity
						finish();
					}
					else if(receivedMsg.startsWith("<#PubDate_FAIL#>")){					//�յ�����ϢΪ����ʧ��
						Toast.makeText(PubDateActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();
						// �ȴ��û�
						Looper.loop();
						Looper.myLooper().quit();
					}
					else if(receivedMsg.startsWith("<#PubDate_ONCE#>")){					//�յ�����ϢΪ�û��ѷ�����Ϣ
						Toast.makeText(PubDateActivity.this, "���ѷ�����Լ��", Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}	
					
				}catch(Exception e){ //�������쳣ʱ���׳��쳣��Ȼ������ȴ��û�
					e.printStackTrace();
					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(PubDateActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(PubDateActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
										
					String msg = "<#CancelDate#>"+uno;					//��֯Ҫ���ص��ַ���
					mc.dout.writeUTF(msg);										//������Ϣ
					String receivedMsg = mc.din.readUTF();		//��ȡ��������������Ϣ
					pd.dismiss();
					if(receivedMsg.startsWith("<#CancelDate_SUCCESS#>")){	//�յ�����ϢΪȡ��Լ��ɹ���Ϣ
//						receivedMsg = receivedMsg.substring(19);
//						String [] sa = receivedMsg.split("\\|");
//						
						myHandler.sendEmptyMessage(0);				//����Handler��Ϣ
						
					}
					else if(receivedMsg.startsWith("<#CancelDate_FAIL#>")){					//�յ�����ϢΪ����ʧ��
						myHandler.sendEmptyMessage(1);				//����Handler��Ϣ
						
					}
					else if(receivedMsg.startsWith("<#PubDate_NEVER#>")){					//�յ�����ϢΪ�û��ѷ�����Ϣ
						myHandler.sendEmptyMessage(2);				//����Handler��Ϣ
						
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
    	Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent����
		intent.putExtra("uno", uno);
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
		Intent intent = new Intent(PubDateActivity.this,MainActivity.class);	//����Intent����
		intent.putExtra("uno", uno);
		startActivity(intent);										//����Activity
		finish();
	}
	
	//����
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
