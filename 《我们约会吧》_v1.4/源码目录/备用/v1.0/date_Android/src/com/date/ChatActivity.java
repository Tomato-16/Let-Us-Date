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

	String uano = null; // ��¼��ǰ�û���id
	String ubno = null; // ��¼�������id
	ArrayList<String[]> chatList = new ArrayList<String[]>();
	ArrayList<String[]> infoList = new ArrayList<String[]>();// ����û�����Ϣ��id���������Ա�ͷ��
	MyConnector mc = null; // ��������������
	ListView lv = null;

	BaseAdapter baContacts = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ChatActivity.this); // �������Բ���
			ll.setOrientation(LinearLayout.HORIZONTAL);

			TextView tvChat = new TextView(ChatActivity.this); // ����������ʾ������TextView
			// tvName.setText(dateList.get(position)[6]);
			// tvName.setTextAppearance(ChatActivity.this, R.style.title);
			tvChat.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			// tvChat.setText(infoList.get(position)[1]+": "+chatList.get(position)[3]+" ");//����TextView������
			tvChat.setTextSize(16.0f); // ���������С
			// tvChat.setTextColor(Color.BLUE); //����������ɫ
			tvChat.setPadding(5, 0, 0, 0); // ���ñ߽�հ�
			if (chatList.get(position)[1].equals(uano)) {
				// RelativeLayout.LayoutParams params = new
				// RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				// tvChat.setLayoutParams(params);
				tvChat.setText(chatList.get(position)[3] + "����  ");// ����TextView������
				tvChat.setTextColor(Color.BLACK);
				ll.setGravity(Gravity.RIGHT);
				// tvChat.setGravity(Gravity.RIGHT);
				ll.addView(tvChat);
			} else {
				// RelativeLayout.LayoutParams params = new
				// RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				tvChat.setText(chatList.get(position)[1] + "��" + chatList.get(position)[3]);// ����TextView������
				// tvChat.setLayoutParams(params);
				tvChat.setTextColor(Color.BLUE);
				ll.setGravity(Gravity.LEFT);
				ll.addView(tvChat);
			}
			// ����ʾ������TextView��ӵ����Բ���

			// ll2.addView(tvName); //����ʾ������TextView��ӵ����Բ���
			// ll2.addView(tvStatus);
			// ll.addView(iv); //����ʾͷ���ImageView��ӵ����󲼾�
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
		Intent intent = getIntent(); // ���������Activity��Intent����
		uano = intent.getStringExtra("uano"); // ��õ�ǰ�û���id
		ubno = intent.getStringExtra("ubno"); // �����������id

		setContentView(R.layout.chat);
		lv = (ListView) findViewById(R.id.listChat); // ���ListView���������
		getchatList();

		Button btnChat = (Button) findViewById(R.id.btnChat); // ��÷��ذ�ť����
		btnChat.getBackground().setAlpha(140);
		btnChat.setOnClickListener(new View.OnClickListener() { // Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				chat();
			}
		});

		Button btnBack = (Button) findViewById(R.id.btnBack); // ��÷��ذ�ť����
		btnBack.getBackground().setAlpha(140);
		btnBack.setOnClickListener(new View.OnClickListener() { // Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this,
						MainActivity.class); // ����Intent����
				intent.putExtra("uno", uano);
				startActivity(intent); // ����Activity
				finish();
			}
		});
	}

	public void chat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����

					EditText etChat = (EditText) findViewById(R.id.etChat);
					String ctext = etChat.getEditableText().toString().trim();

					mc.dout.writeUTF("<#CHAT#>" + uano + "|" + ubno + "|"
							+ ctext); // ���������������
					String receivedMsg = mc.din.readUTF(); // ��ȡ��������������Ϣ

					if (receivedMsg.startsWith("<#WRITECHAT_SUCCESS#>")) { // �յ�����ϢΪ��¼�ɹ���Ϣ
					// receivedMsg = receivedMsg.substring(19);
					// String [] sa = receivedMsg.split("\\|");
					//
					// ת���������
						Intent intent = new Intent(ChatActivity.this,
								ChatActivity.class);
						intent.putExtra("uano", uano);
						intent.putExtra("ubno", ubno);
						startActivity(intent); // ��������Activity
						finish();
					} else if (receivedMsg.startsWith("<#PubDate_FAIL#>")) { // �յ�����ϢΪ����ʧ��
						Toast.makeText(ChatActivity.this, "������Ϣʧ��",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}

					myHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
//					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(ChatActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(ChatActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
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
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
					mc.dout.writeUTF("<#CHAT_LIST#>" + uano + "|" + ubno + "|"
							+ "1"); // ���������������
					int size = mc.din.readInt(); // ��ȡ�б�ĳ���

					chatList = null;
					chatList = new ArrayList<String[]>(size); // ��ʼ�������б�
					infoList = null;
					infoList = new ArrayList<String[]>(size);
					String cInfo = mc.din.readUTF(); // ��ȡ������Ϣ
					String[] list = cInfo.split("\\,");
					for (int i = 0; i < size; i++) { // ѭ������ȡ���˵������¼

						String[] sa = list[i].split("\\|"); // �ָ��ַ���
						chatList.add(sa); // ��������Ϣ��ӵ���Ӧ���б���

						// ��ȡ�û���Ϣ
						// mc.dout.writeUTF("<#UserInfo#>"+sa[1]); //���������������
						//
						// String fInfo = mc.din.readUTF(); //��ȡ������Ϣ
						// String []f = fInfo.split("\\|"); //�ָ��ַ���
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
				MainActivity.class); // ����Intent����
		intent.putExtra("uno", uano);
		startActivity(intent); // ����Activity
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
		Intent intent = new Intent(ChatActivity.this,
				MainActivity.class); // ����Intent����
		intent.putExtra("uno", uano);
		startActivity(intent); // ����Activity
		finish();
	}
	
	//����
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
