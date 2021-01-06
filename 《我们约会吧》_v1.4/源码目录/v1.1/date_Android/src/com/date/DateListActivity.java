package com.date;

import static com.date.ConstantUtil.HEAD_HEIGHT;
import static com.date.ConstantUtil.HEAD_WIDTH;
import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class DateListActivity extends Activity {

	String uno = null; // ��¼��ǰ�û���id
	Bitmap[] headList = null; // ���ͷ�������
	ArrayList<String[]> dateList = new ArrayList<String[]>();
	String[] infoList = null; // ����û�����Ϣ��id���������Ա�ͷ��
	MyConnector mc = null; // ��������������
	ListView lv = null;

	BaseAdapter baContacts = new BaseAdapter() {
		
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(DateListActivity.this); // �������Բ���
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ImageView iv = new ImageView(DateListActivity.this); // ����ImageView����
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setImageBitmap(headList[position]); // ����ͷ��
			iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH,
					HEAD_HEIGHT));
			iv.setPadding(5, 0, 0, 0);
			// LinearLayout ll2 = new LinearLayout(DateListActivity.this);
			// //���������Բ���
			// ll2.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout lnew = new LinearLayout(DateListActivity.this); // �������Բ���
			lnew.setOrientation(LinearLayout.VERTICAL);

			TextView tvName = new TextView(DateListActivity.this); // ����������ʾ������TextView
			// tvName.setText(dateList.get(position)[6]);
			// tvName.setTextAppearance(DateListActivity.this, R.style.title);
			tvName.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			tvName.setText(" ������" + dateList.get(position)[5] + " ");// ����TextView������
			tvName.setTextSize(15.0f); // ���������С
			tvName.setTextColor(Color.BLUE); // ����������ɫ
			tvName.setPadding(5, 0, 0, 0); // ���ñ߽�հ�

			TextView tvStyle = new TextView(DateListActivity.this); // ����������ʾ������TextView
			tvStyle.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			tvStyle.setText(" Լ�����ͣ�" + dateList.get(position)[1] + " ");// ����TextView������
			tvStyle.setTextSize(15.0f); // ���������С
			tvStyle.setTextColor(Color.BLUE); // ����������ɫ
			tvStyle.setPadding(5, 0, 0, 0);

			TextView tvSex = new TextView(DateListActivity.this); // ����������ʾ�����TextView
			tvSex.setTextSize(15.0f); // �����������С
			// tvStatus.setTextAppearance(DateListActivity.this,
			// R.style.content);
			tvSex.setPadding(5, 0, 0, 0);
			tvSex.setText(" �Ա�" + dateList.get(position)[2] + " "); // ����TextView����
			if (dateList.get(position)[2].equals(infoList[4])) {
				tvSex.setTextColor(Color.GREEN);
			} else
				tvSex.setTextColor(Color.RED);

			TextView tvAge = new TextView(DateListActivity.this); // ����������ʾ������TextView
			tvAge.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			tvAge.setText(" ����Σ�" + dateList.get(position)[3] + "��");// ����TextView������
			tvAge.setTextSize(15.0f); // ���������С
			// tvAge.setTextColor(Color.BLUE); //����������ɫ
			tvAge.setPadding(5, 0, 0, 0);
			String age = infoList[5].substring(2, 3) + "0";
			if (dateList.get(position)[3].trim().equals(age.trim())) {
				tvAge.setTextColor(Color.GREEN);
			} else
				tvAge.setTextColor(Color.RED);

			// ����������ʾ��̬��TextView
			TextView tvDistance = new TextView(DateListActivity.this); 
			tvDistance.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			tvDistance.setText(" ״̬��" + dateList.get(position)[4]);// ����TextView������
			tvDistance.setTextSize(15.0f); // ���������С
			tvDistance.setTextColor(Color.BLUE); // ����������ɫ
			tvDistance.setPadding(5, 0, 0, 0);

			lnew.addView(tvName); // ����ʾ������TextView��ӵ����Բ���
			lnew.addView(tvStyle);
			lnew.addView(tvSex); // ����ʾ�����TextView��ӵ����Բ���
			lnew.addView(tvAge);
			lnew.addView(tvDistance);
			ll.addView(iv);
			ll.addView(lnew);
			// lall.addView(tvDistance);
			// ll2.addView(tvName); //����ʾ������TextView��ӵ����Բ���
			// ll2.addView(tvStatus);
			// ll.addView(iv); //����ʾͷ���ImageView��ӵ����󲼾�
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
			return dateList.size();
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
		uno = intent.getStringExtra("uno"); // ��õ�ǰ�û���id

		setContentView(R.layout.datelist);
		lv = (ListView) findViewById(R.id.listDate); // ���ListView���������
		getDateList();
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(DateListActivity.this,
						ChatActivity.class);
				intent.putExtra("uano", uno);
				intent.putExtra("ubno", dateList.get(position)[7]);
				startActivity(intent);
				finish();
			}
		});

		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack); // ��÷��ذ�ť����
		btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnBack.setPadding(8, 8, 8, 8);
		btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() { // Ϊ���ذ�ť��Ӽ�����
			
			public void onClick(View v) {
				Intent intent = new Intent(DateListActivity.this,
						MainActivity.class); // ����Intent����
				intent.putExtra("uno", uno);
				startActivity(intent); // ����Activity
				finish();
			}
		});
	}

	public void getDateList() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
					mc.dout.writeUTF("<#DATE_LIST#>" + uno + "|" + "1"); // ���������������
					int size = mc.din.readInt(); // ��ȡ�б�ĳ���
					headList = null;
					headList = new Bitmap[size];
					dateList = null; // ��ʼ������ͷ���б�
					dateList = new ArrayList<String[]>(size); // ��ʼ��������Ϣ�б�

					String fInfo = mc.din.readUTF();
					String[] list = fInfo.split("\\,");

					for (int i = 0; i < size; i++) { // ѭ������ȡÿ�����ѵ���Ϣ��ͷ��
						// ��ȡ������Ϣ
						String[] sa = list[i].split("\\|"); // �ָ��ַ���
						dateList.add(sa);

						// ��������Ϣ��ӵ���Ӧ���б���
						// int headSize = mc.din.readInt(); //��ȡͷ���С
						// byte[] buf = new byte[headSize]; //����������
						// mc.din.read(buf); //��ȡͷ����Ϣ
						// headList[i] = BitmapFactory.decodeByteArray(buf, 0,
						// headSize);
						// headList[i] =
						// BitmapFactory.decodeFile("/sdcard/date.data/"+sa[7]+".jpg");
						// //�ӱ���ȡͼƬ
						BufferedInputStream bis = new BufferedInputStream(
								getAssets().open(sa[7] + ".jpg"));
						headList[i] = BitmapFactory.decodeStream(bis);
					}

					// ��ȡ�û���Ϣ
					mc.dout.writeUTF("<#UserInfo#>" + uno); // ���������������

					String fInfo1 = mc.din.readUTF(); // ��ȡ�Լ���Ϣ
					infoList = fInfo1.split("\\|"); // �ָ��ַ���

					myHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
//					pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()){
						 Toast.makeText(DateListActivity.this, "��ȡ���ݳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();
					 }
					 else{
						 Toast.makeText(DateListActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
							Looper.loop();
							Looper.myLooper().quit();						 
					 }	
				}

			}
		}.start();
	}

	/**
	 * ���ر���ͼƬ
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
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
		 Intent intent = new Intent(DateListActivity.this,
					MainActivity.class); // ����Intent����
			intent.putExtra("uno", uno);
			startActivity(intent); // ����Activity
			finish();
	}
	
	//����
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) DateListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help_datelist, null);
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
	protected void onDestroy() {
		if (mc != null) {
			mc.sayBye();
		}
		super.onDestroy();
	}
	
	 @Override
		public void onBackPressed() {
		 Intent intent = new Intent(DateListActivity.this,
					MainActivity.class); // ����Intent����
			intent.putExtra("uno", uno);
			startActivity(intent); // ����Activity
			finish();
		}
}
