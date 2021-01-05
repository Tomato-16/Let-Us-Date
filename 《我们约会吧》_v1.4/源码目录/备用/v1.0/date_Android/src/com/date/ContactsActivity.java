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
    
    String uno = null;			//记录当前用户的id
	Bitmap [] headList = null;	//存放头像的数组
	ArrayList<String []> friendList = new ArrayList<String []>();
//	String [] infoList = null;	//存放用户的信息：id、姓名、性别、头像
	MyConnector mc = null;		//网络连接器对象
	ListView lv = null;
	
	BaseAdapter baContacts=new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ContactsActivity.this);		//创建线性布局
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ImageView iv = new ImageView(ContactsActivity.this);			//创建ImageView对象
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setImageBitmap(headList[position]);		//设置头像
				iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
				iv.setPadding(5, 0, 0, 0);	
//				LinearLayout ll2 = new LinearLayout(DateListActivity.this);		//创建子线性布局
//				ll2.setOrientation(LinearLayout.HORIZONTAL);
				
				LinearLayout lnew = new LinearLayout(ContactsActivity.this); // 创建线性布局
				lnew.setOrientation(LinearLayout.VERTICAL);
				
				TextView tvName = new TextView(ContactsActivity.this);			//创建用于显示姓名的TextView
				//tvName.setText(dateList.get(position)[6]);	
				//tvName.setTextAppearance(DateListActivity.this, R.style.title);
				tvName.setGravity(Gravity.LEFT);			//设置TextView的对齐方式
				tvName.setText(" 姓名：" + friendList.get(position)[1]+" ");//设置TextView的内容
				tvName.setTextSize(15.0f);										//设置字体大小
				tvName.setTextColor(Color.BLUE);								//设置字体颜色
				tvName.setPadding(5, 0, 0, 0);									//设置边界空白
				
				
				TextView tvSex = new TextView(ContactsActivity.this);		//创建用于显示心情的TextView
				tvSex.setTextSize(15.0f);										//设置这字体大小
				//tvStatus.setTextAppearance(friendListActivity.this, R.style.content);
				tvSex.setPadding(5, 0, 0, 0);							
				tvSex.setText(" 性别：" + friendList.get(position)[2]);				//设置TextView内容
				tvSex.setTextColor(Color.GREEN);
				
				TextView tvBirthday = new TextView(ContactsActivity.this);			//创建用于显示姓名的TextView
				tvBirthday.setGravity(Gravity.LEFT);			//设置TextView的对齐方式
				tvBirthday.setText(" 出生年月：" + friendList.get(position)[3]);//设置TextView的内容
				tvBirthday.setTextSize(12.0f);										//设置字体大小
				tvBirthday.setTextColor(Color.BLACK);								//设置字体颜色
				tvBirthday.setPadding(130, 0, 0, 0);
						
				lnew.addView(tvName);									//将显示姓名的TextView添加到线性布局
				lnew.addView(tvSex);											//将显示心情的TextView添加到线性布局
				lnew.addView(tvBirthday);
				ll.addView(iv);
				ll.addView(lnew);
//				ll2.addView(tvName);											//将显示姓名的TextView添加到线性布局
//				ll2.addView(tvStatus);		
//				ll.addView(iv);													//将显示头像的ImageView添加到现象布局
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
        Intent intent = getIntent();			//获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno");		//获得当前用户的id
		lv = (ListView)findViewById(R.id.listFriend);		//获得ListView对象的引用
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
		
		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);				//获得返回按钮对象
		btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnBack.setPadding(8, 8, 8, 8);
		btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ContactsActivity.this,MainActivity.class);	//创建Intent对象
				intent.putExtra("uno", uno);
				startActivity(intent);										//启动Activity
				finish();
			}
		});
    }
    
    public void getfriendList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//创建MyConnector对象
					mc.dout.writeUTF("<#FRIEND_LIST#>"+uno);		//向服务器发出请求	
					int size = mc.din.readInt();					//读取列表的长度
					headList = null;
					headList = new Bitmap[size];
					friendList = null;					//初始化好友头像列表
					friendList = new ArrayList<String []>(size);		//初始化好友信息列表
					
					String fInfo = mc.din.readUTF();		//读取好友信息
					String []list = fInfo.split("\\,");
					
					for(int i=0;i<size;i++){			//循环，获取每个好友的信息和头像
						
						String [] sa = list[i].split("\\|");		//分割字符串
						friendList.add(sa);						//将好友信息添加到相应的列表中
//						int headSize = mc.din.readInt();		//读取头像大小
//						byte[] buf = new byte[headSize];			//创建缓冲区
//						mc.din.read(buf);						//读取头像信息
//						headList[i] = BitmapFactory.decodeByteArray(buf, 0, headSize);
//						headList[i] = BitmapFactory.decodeFile("/sdcard/date.data/"+sa[0]+".jpg"); //从本地取图片
						BufferedInputStream bis = new BufferedInputStream(getAssets()
							      .open(sa[0]+".jpg"));
						headList[i] = BitmapFactory.decodeStream(bis);
					}
					
					//读取用户信息
//					mc.dout.writeUTF("<#UserInfo#>"+uno);		//向服务器发出请求	
//					
//					String fInfo = mc.din.readUTF();		//读取好友信息
//					infoList = fInfo.split("\\|");		//分割字符串
//					
					
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
//					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ContactsActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ContactsActivity.this, "连接超时", Toast.LENGTH_LONG).show();
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
		Intent intent = new Intent(ContactsActivity.this, MainActivity.class); // 创建Intent对象
		intent.putExtra("uno", uno);
		startActivity(intent); // 启动Activity
		finish();
	}
	
	//帮助
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
		 Intent intent = new Intent(ContactsActivity.this,MainActivity.class);	//创建Intent对象
			intent.putExtra("uno", uno);
			startActivity(intent);										//启动Activity
			finish();
		}
}
