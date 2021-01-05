package com.date;

import static com.date.ConstantUtil.HEAD_HEIGHT;
import static com.date.ConstantUtil.HEAD_WIDTH;
import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import java.io.BufferedInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends Activity {
    
    String uno = null;			//��¼��ǰ�û���id
	Bitmap [] headList = null;	//���ͷ�������
	ArrayList<String []> friendList = new ArrayList<String []>();
//	String [] infoList = null;	//����û�����Ϣ��id���������Ա�ͷ��
	MyConnector mc = null;		//��������������
	ListView lv = null;
	
	BaseAdapter baContacts=new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ContactsActivity.this);		//�������Բ���
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ImageView iv = new ImageView(ContactsActivity.this);			//����ImageView����
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setImageBitmap(headList[position]);		//����ͷ��
				iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
				iv.setPadding(5, 0, 0, 0);	
//				LinearLayout ll2 = new LinearLayout(DateListActivity.this);		//���������Բ���
//				ll2.setOrientation(LinearLayout.HORIZONTAL);
				
				LinearLayout lnew = new LinearLayout(ContactsActivity.this); // �������Բ���
				lnew.setOrientation(LinearLayout.VERTICAL);
				
				TextView tvName = new TextView(ContactsActivity.this);			//����������ʾ������TextView
				//tvName.setText(dateList.get(position)[6]);	
				//tvName.setTextAppearance(DateListActivity.this, R.style.title);
				tvName.setGravity(Gravity.LEFT);			//����TextView�Ķ��뷽ʽ
				tvName.setText(" ������" + friendList.get(position)[1]+" ");//����TextView������
				tvName.setTextSize(15.0f);										//���������С
				tvName.setTextColor(Color.BLUE);								//����������ɫ
				tvName.setPadding(5, 0, 0, 0);									//���ñ߽�հ�
				
				
				TextView tvSex = new TextView(ContactsActivity.this);		//����������ʾ�����TextView
				tvSex.setTextSize(15.0f);										//�����������С
				//tvStatus.setTextAppearance(friendListActivity.this, R.style.content);
				tvSex.setPadding(5, 0, 0, 0);							
				tvSex.setText(" �Ա�" + friendList.get(position)[2]);				//����TextView����
				tvSex.setTextColor(Color.GREEN);
				
				TextView tvBirthday = new TextView(ContactsActivity.this);			//����������ʾ������TextView
				tvBirthday.setGravity(Gravity.LEFT);			//����TextView�Ķ��뷽ʽ
				tvBirthday.setText(" �������£�" + friendList.get(position)[3]);//����TextView������
				tvBirthday.setTextSize(12.0f);										//���������С
				tvBirthday.setTextColor(Color.BLACK);								//����������ɫ
				tvBirthday.setPadding(130, 0, 0, 0);
						
				lnew.addView(tvName);									//����ʾ������TextView��ӵ����Բ���
				lnew.addView(tvSex);											//����ʾ�����TextView��ӵ����Բ���
				lnew.addView(tvBirthday);
				ll.addView(iv);
				ll.addView(lnew);
//				ll2.addView(tvName);											//����ʾ������TextView��ӵ����Բ���
//				ll2.addView(tvStatus);		
//				ll.addView(iv);													//����ʾͷ���ImageView��ӵ����󲼾�
//				ll.addView(ll2);
				return ll;
			}
			@Override
			public long getItemId(int position) {
				return 0;
			}
			@Override
			public Object getItem(int position) {
				return null;
			}
			@Override
			public int getCount() {
				return friendList.size();
			}
		}; 
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lv.setAdapter(baContacts);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts);
        Intent intent = getIntent();			//���������Activity��Intent����
		uno = intent.getStringExtra("uno");		//��õ�ǰ�û���id
		lv = (ListView)findViewById(R.id.listFriend);		//���ListView���������
		getfriendList();
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(ContactsActivity.this,ChatActivity.class);
				intent.putExtra("uano", uno);
				intent.putExtra("ubno", friendList.get(position)[0]);
				startActivity(intent);
				finish();
			} 
		});
		
		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);				//��÷��ذ�ť����
		btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnBack.setPadding(8, 8, 8, 8);
		btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ContactsActivity.this,MainActivity.class);	//����Intent����
				intent.putExtra("uno", uno);
				startActivity(intent);										//����Activity
				finish();
			}
		});
    }
    
    public void getfriendList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//����MyConnector����
					mc.dout.writeUTF("<#FRIEND_LIST#>"+uno);		//���������������	
					int size = mc.din.readInt();					//��ȡ�б�ĳ���
					headList = null;
					headList = new Bitmap[size];
					friendList = null;					//��ʼ������ͷ���б�
					friendList = new ArrayList<String []>(size);		//��ʼ��������Ϣ�б�
					
					String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
					String []list = fInfo.split("\\,");
					
					for(int i=0;i<size;i++){			//ѭ������ȡÿ�����ѵ���Ϣ��ͷ��
						
						String [] sa = list[i].split("\\|");		//�ָ��ַ���
						friendList.add(sa);						//��������Ϣ��ӵ���Ӧ���б���
//						int headSize = mc.din.readInt();		//��ȡͷ���С
//						byte[] buf = new byte[headSize];			//����������
//						mc.din.read(buf);						//��ȡͷ����Ϣ
//						headList[i] = BitmapFactory.decodeByteArray(buf, 0, headSize);
//						headList[i] = BitmapFactory.decodeFile("/sdcard/date.data/"+sa[0]+".jpg"); //�ӱ���ȡͼƬ
						BufferedInputStream bis = new BufferedInputStream(getAssets()
							      .open(sa[0]+".jpg"));
						headList[i] = BitmapFactory.decodeStream(bis);
					}
					
					//��ȡ�û���Ϣ
//					mc.dout.writeUTF("<#UserInfo#>"+uno);		//���������������	
//					
//					String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
//					infoList = fInfo.split("\\|");		//�ָ��ַ���
//					
					
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
//					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ContactsActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ContactsActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
		Intent intent = new Intent(ContactsActivity.this, MainActivity.class); // ����Intent����
		intent.putExtra("uno", uno);
		startActivity(intent); // ����Activity
		finish();
	}
	
	//����
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) ContactsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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
	
	 @Override
		public void onBackPressed() {
		 Intent intent = new Intent(ContactsActivity.this,MainActivity.class);	//����Intent����
			intent.putExtra("uno", uno);
			startActivity(intent);										//����Activity
			finish();
		}
}
