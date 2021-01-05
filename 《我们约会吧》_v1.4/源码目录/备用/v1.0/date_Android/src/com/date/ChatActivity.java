package com.date;

import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {

	String uano = null; // 记录当前用户的id
	String ubno = null; // 记录聊天对象id
	ArrayList<String[]> chatList = new ArrayList<String[]>();
	ArrayList<String[]> infoList = new ArrayList<String[]>();// 存放用户的信息：id、姓名、性别、头像
	MyConnector mc = null; // 网络连接器对象
	ListView lv = null;

	BaseAdapter baContacts = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ChatActivity.this); // 创建线性布局
			ll.setOrientation(LinearLayout.HORIZONTAL);

			TextView tvChat = new TextView(ChatActivity.this); // 创建用于显示姓名的TextView
			// tvName.setText(dateList.get(position)[6]);
			// tvName.setTextAppearance(ChatActivity.this, R.style.title);
			tvChat.setGravity(Gravity.LEFT); // 设置TextView的对齐方式
			// tvChat.setText(infoList.get(position)[1]+": "+chatList.get(position)[3]+" ");//设置TextView的内容
			tvChat.setTextSize(16.0f); // 设置字体大小
			// tvChat.setTextColor(Color.BLUE); //设置字体颜色
			tvChat.setPadding(5, 0, 0, 0); // 设置边界空白
			if (chatList.get(position)[1].equals(uano)) {
				// RelativeLayout.LayoutParams params = new
				// RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				// tvChat.setLayoutParams(params);
				tvChat.setText(chatList.get(position)[3] + "：我  ");// 设置TextView的内容
				tvChat.setTextColor(Color.BLACK);
				ll.setGravity(Gravity.RIGHT);
				// tvChat.setGravity(Gravity.RIGHT);
				ll.addView(tvChat);
			} else {
				// RelativeLayout.LayoutParams params = new
				// RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				tvChat.setText(chatList.get(position)[1] + "：" + chatList.get(position)[3]);// 设置TextView的内容
				// tvChat.setLayoutParams(params);
				tvChat.setTextColor(Color.BLUE);
				ll.setGravity(Gravity.LEFT);
				ll.addView(tvChat);
			}
			// 将显示姓名的TextView添加到线性布局

			// ll2.addView(tvName); //将显示姓名的TextView添加到线性布局
			// ll2.addView(tvStatus);
			// ll.addView(iv); //将显示头像的ImageView添加到现象布局
			// ll.addView(ll2);
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
			super.handleMessage(msg);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent(); // 获得启动该Activity的Intent对象
		uano = intent.getStringExtra("uano"); // 获得当前用户的id
		ubno = intent.getStringExtra("ubno"); // 获得聊天对象的id

		setContentView(R.layout.chat);
		lv = (ListView) findViewById(R.id.listChat); // 获得ListView对象的引用
		getchatList();

		Button btnChat = (Button) findViewById(R.id.btnChat); // 获得返回按钮对象
		btnChat.getBackground().setAlpha(140);
		btnChat.setOnClickListener(new View.OnClickListener() { // 为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				chat();
			}
		});

		Button btnBack = (Button) findViewById(R.id.btnBack); // 获得返回按钮对象
		btnBack.getBackground().setAlpha(140);
		btnBack.setOnClickListener(new View.OnClickListener() { // 为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this,
						MainActivity.class); // 创建Intent对象
				intent.putExtra("uno", uano);
				startActivity(intent); // 启动Activity
				finish();
			}
		});
	}

	public void chat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象

					EditText etChat = (EditText) findViewById(R.id.etChat);
					String ctext = etChat.getEditableText().toString().trim();

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
						startActivity(intent); // 启动功能Activity
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
