package com.date;

import static com.date.ConstantUtil.HEAD_HEIGHT;
import static com.date.ConstantUtil.HEAD_WIDTH;
import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	BufferedInputStream bis = null;
	Bitmap headList = null;	//存放头像的数组
	String uano = null; // 记录当前用户的id
	String ubno = null; // 记录聊天对象id
	ArrayList<String[]> chatList = new ArrayList<String[]>();
	ArrayList<String[]> infoList = new ArrayList<String[]>();// 存放用户的信息：id、姓名、性别、头像
	MyConnector mc = null; // 网络连接器对象
	ListView lv = null;
    String photoname=null;
	@SuppressLint("InlinedApi")
	BaseAdapter baContacts = new BaseAdapter() {
	//getView方法中的三个参数，第一position是指现在是第几个条目；第二convertView是已经绘制好了的视图；parent是ListView之类的View视图。
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ChatActivity.this); 
			// 创建线性布局 ,设置该Activity显示layout
			ll.setOrientation(LinearLayout.HORIZONTAL);            /*setOrientation设置水平方向  LinearLayout.HORIZONTAL表示水平布局  */
			//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			//此处相当于布局文件中的Android:layout_gravity属性
			//lp.gravity = Gravity.BOTTOM;		
			//lv = (ListView) findViewById(R.id.listChat); // 获得ListView对象的引用
			//lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			//lv.setStackFromBottom(true);		


	        
			TextView tvChat = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
			tvChat.setGravity(Gravity.LEFT); // 设置TextView的对齐方式
			
			// tvChat.setText(infoList.get(position)[1]+": "+chatList.get(position)[3]+" ");//设置TextView的内容
			tvChat.setTextSize(16.0f); // 设置字体大小
			// tvChat.setTextColor(Color.BLUE); //设置字体颜色
			tvChat.setPadding(20, 0, 0, 0); // 设置边界空白
			if (chatList.get(position)[1].equals(uano)) {

/*				Drawable drawable= getResources().getDrawable(R.raw.p2004);
				/// 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, 30, 30);
				TextView tvTu = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
				tvTu.setCompoundDrawables(null,null,drawable,null);*/
				ImageView iv = new ImageView(ChatActivity.this);			//创建ImageView对象
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				try {
					bis = new BufferedInputStream(
							getAssets().open(chatList.get(position)[1]+ ".jpg"));
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				// 将字节流转换为Bitmap,使用BitmapFactory工厂类进行转换
				headList = BitmapFactory.decodeStream(bis);
				iv.setImageBitmap(headList);		//设置头像
				iv.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
				iv.setPadding(0, 0, 0, 0);	
				
				LinearLayout ll1 = new LinearLayout(ChatActivity.this); // 创建线性布局
				ll1.setOrientation(LinearLayout.VERTICAL);
				
				tvChat.setText(chatList.get(position)[3]);// 设置TextView的内容                
                tvChat.setBackgroundDrawable(getResources().getDrawable(R.drawable.txt_radiuborder));               
				tvChat.setTextColor(Color.BLACK);/*设置字体黑色*/
				ll.setGravity(Gravity.RIGHT);/*设置右对齐*/
				
				TextView tvStyle = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
				tvStyle.setGravity(Gravity.RIGHT); // 设置TextView的对齐方式
				tvStyle.setText( ":"+chatList.get(position)[1]+"(我 )");// 设置TextView的内容
				tvStyle.setTextSize(10.0f); // 设置字体大小
				tvStyle.setTextColor(Color.BLACK); // 设置字体颜色
				tvStyle.setPadding(20, 0, 0, 0);
				// tvChat.setGravity(Gravity.RIGHT);
				ll1.addView(tvStyle);
				ll1.addView(tvChat);
				ll.addView(ll1);
				//ll.addView(tvTu);
                ll.addView(iv);
			} else {
/*				Drawable drawable= getResources().getDrawable(R.drawable.girl);
				/// 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, 30, 30);
				TextView tvTu1 = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
				tvTu1.setCompoundDrawables(drawable,null,null,null);*/
				ImageView iv1 = new ImageView(ChatActivity.this);			//创建ImageView对象
				iv1.setScaleType(ImageView.ScaleType.FIT_CENTER);
				try {
					bis = new BufferedInputStream(
							getAssets().open(chatList.get(position)[1]+ ".jpg"));
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				headList = BitmapFactory.decodeStream(bis);
				headList = toRoundBitmap(headList);
				iv1.setImageBitmap(headList);		//设置头像
				iv1.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
				iv1.setPadding(0, 0, 0, 0);	
				
				LinearLayout ll2 = new LinearLayout(ChatActivity.this); // 创建线性布局
				ll2.setOrientation(LinearLayout.VERTICAL);
				
				TextView tvStyle1 = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
				tvStyle1.setGravity(Gravity.LEFT); // 设置TextView的对齐方式
				tvStyle1.setText( chatList.get(position)[1]+":");// 设置TextView的内容
				tvStyle1.setTextSize(10.0f); // 设置字体大小
				tvStyle1.setTextColor(Color.BLUE); // 设置字体颜色
				tvStyle1.setPadding(20, 0, 0, 0);
				
				tvChat.setText(chatList.get(position)[3]);// 设置TextView的内容
	
                tvChat.setBackgroundDrawable(getResources().getDrawable(R.drawable.txt_rectborder));
				tvChat.setTextColor(Color.BLUE);/*设置字体蓝色*/
				ll.setGravity(Gravity.LEFT);/*设置左对齐*/
				ll2.addView(tvStyle1);				
				ll2.addView(tvChat);
				ll.addView(iv1);
                //ll.addView(tvTu1);
                ll.addView(ll2);

			}
			// 将显示姓名的TextView添加到线性布局

			// ll2.addView(tvName); //将显示姓名的TextView添加到线性布局
			// ll2.addView(tvStatus);
			// ll.addView(iv); //将显示头像的ImageView添加到现象布局
			// ll.addView(ll2);
			return ll;
		}

	
		public long getItemId(int position) {
			return 0;
		}

		
		public Object getItem(int position) {
			return null;
		}

		
		public int getCount() {
			return chatList.size();
		}
	};
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				lv.setAdapter(baContacts);
				break;
			}
			super.handleMessage(msg);//父类handleMessage方法，子类重写的handleMessage方法。子类的方法调用了“super.handleMessage（msg）”，子类就具备了“父类能够处理一些它已经写好的消息处理”的能力，然后子类再写一点自己需要处理的消息。
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//启用窗体的扩展特性,无标题
		Intent intent = getIntent(); // 获得启动该Activity的Intent对象
		uano = intent.getStringExtra("uano"); // 获得当前用户的id
		ubno = intent.getStringExtra("ubno"); // 获得聊天对象的id

		setContentView(R.layout.chat);
//setContentView就是设置一个Activity的显示界面，这句话就是设置这个这句话所再的Activity采用R.layout下的chat布局文件进行布局
		lv = (ListView) findViewById(R.id.listChat); // 获得ListView对象的引用
		getchatList();

		Button btnChat = (Button) findViewById(R.id.btnChat); // 获得返回按钮对象
		btnChat.getBackground().setAlpha(140);
		btnChat.setOnClickListener(new View.OnClickListener() { // 为返回按钮添加监听器
			
			public void onClick(View v) {
				chat();
			}
		});

		Button btnBack = (Button) findViewById(R.id.btnBack); // 获得返回按钮对象
		btnBack.getBackground().setAlpha(140);
		btnBack.setOnClickListener(new View.OnClickListener() { // 为返回按钮添加监听器
		
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this,
						MainActivity.class); // 创建Intent对象
				intent.putExtra("uno", uano);

				startActivity(intent); // 启动Activity
				finish();
			}
		});
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {  
        //圆形图片宽高  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //正方形的边长  
        int r = 0;  
        //取最短边做边长  
        if(width > height) {  
            r = height;  
        } else {  
            r = width;  
        }  
        //构建一个bitmap ,createBitmap该函数创建一个带有特定宽度、高度和颜色格式的位图,ARGB_8888——32位ARGB位图
        Bitmap backgroundBmp = Bitmap.createBitmap(width,height, Config.ARGB_8888);  
        Canvas canvas = new Canvas(backgroundBmp);  //new一个Canvas，在backgroundBmp上画图 .通过传入装载画布Bitmap对象创建Canvas对象
        Paint paint = new Paint();  //创建一个画笔      
        paint.setAntiAlias(true); //设置边缘光滑，去掉锯齿  
        paint.setDither(true);  //防抖动 
        paint.setStrokeWidth(1);//设置画笔的宽度     
        RectF rect = new RectF(0, 0, r, r);   //宽高相等，即正方形  
        //RectF(float left, float top, float right, float bottom),矩形的宽度width=right-left，高度height=bottom-top
        canvas.drawRoundRect(rect, r/2, r/2, paint);  
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时,且都等于r/2时，画出来的圆角矩形就是圆形          
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        canvas.drawBitmap(bitmap, null, rect, paint);
        //canvas将bitmap画在backgroundBmp上         
        return backgroundBmp;  
        //返回已经绘画好的backgroundBmp         
    } 

	public void chat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象
					EditText etChat = (EditText) findViewById(R.id.etChat);
					String ctext = etChat.getEditableText().toString().trim();
//getStringValue()则是获取当前节点的子孙节点中所有文本内容连接成的字符串
//获得对象的字段的值，然后转成string类型，并且去掉前后空白~~ToString()是转化为字符串的方法 Trim()是去两边空格的方法
					mc.dout.writeUTF("<#CHAT#>" + uano + "|" + ubno + "|"
							+ ctext); // 向服务器发出请求
					String receivedMsg = mc.din.readUTF(); // 读取服务器发来的消息

					if (receivedMsg.startsWith("<#WRITECHAT_SUCCESS#>")) { // 收到的消息为登录成功消息
					// receivedMsg = receivedMsg.substring(19);
					// String [] sa = receivedMsg.split("\\|");
					//
					// 转到功能面板
						Intent intent = new Intent(ChatActivity.this,
								ChatActivity.class);
						intent.putExtra("uano", uano);
						intent.putExtra("ubno", ubno);
		//putExtra("A",B)中，AB为键值对，第一个参数为键名，第二个参数为键对应的值。
						startActivity(intent); //   //启动该Intent对象，实现跳转 启动功能Activity
						finish();
					} else if (receivedMsg.startsWith("<#PubDate_FAIL#>")) { // 收到的消息为发布失败
						Toast.makeText(ChatActivity.this, "发送消息失败",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}

					myHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
//					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ChatActivity.this, "读取数据超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ChatActivity.this, "连接超时", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();						 
					 }	
				}

			}
		}.start();
	}

	public void getchatList() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象
					mc.dout.writeUTF("<#CHAT_LIST#>" + uano + "|" + ubno + "|"
							+ "1"); // 向服务器发出请求
					int size = mc.din.readInt(); // 读取列表的长度

					chatList = null;
					chatList = new ArrayList<String[]>(size); // 初始化聊天列表
					infoList = null;
					infoList = new ArrayList<String[]>(size);
					String cInfo = mc.din.readUTF(); // 读取聊天信息
					String[] list = cInfo.split("\\,");
					for (int i = 0; i < size; i++) { // 循环，获取两人的聊天记录

						String[] sa = list[i].split("\\|"); // 分割字符串
						chatList.add(sa); // 将聊天信息添加到相应的列表中

						// 读取用户信息
						// mc.dout.writeUTF("<#UserInfo#>"+sa[1]); //向服务器发出请求
						//
						// String fInfo = mc.din.readUTF(); //读取好友信息
						// String []f = fInfo.split("\\|"); //分割字符串
						// infoList.add(f);

					}

					myHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		if (mc != null) {
			mc.sayBye();
		}
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ChatActivity.this,
				MainActivity.class); // 创建Intent对象
		intent.putExtra("uno", uano);
		startActivity(intent); // 启动Activity
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
		Intent intent = new Intent(ChatActivity.this,
				MainActivity.class); // 创建Intent对象
		intent.putExtra("uno", uano);
		startActivity(intent); // 启动Activity
		finish();
	}
	
	//帮助
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) ChatActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help_chat, null);
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
