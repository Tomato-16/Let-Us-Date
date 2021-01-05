package com.date;

import static com.date.ConstantUtil.SERVER_ADDRESS;
import static com.date.ConstantUtil.SERVER_PORT;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	String uno = null; // ��¼��ǰ�û���id
	// int type = -1; //Ϊ0��ʾ��ʾ�����б�Ϊ1��ʾ��ʾ�ÿ��б�
	Bitmap head = null; // ���ͷ��
	String[] infoList = null; // ����û�����Ϣ��id���������Ա�ͷ��
	// int headSize=0;
	// byte[] buf=null;
	MyConnector mc = null; // ��������������
	String latitude = null;// ����
	String longitude = null;// γ��

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				TextView tvID = (TextView) findViewById(R.id.tvID);
				TextView tvName = (TextView) findViewById(R.id.tvName);
				TextView tvSex = (TextView) findViewById(R.id.tvSex);
				TextView tvBirthday = (TextView) findViewById(R.id.tvBirthday);
				TextView tvMail = (TextView) findViewById(R.id.tvMail);

				String id = getString(R.string.tvID);
				String name = getString(R.string.tvName);
				String sex = getString(R.string.tvSex);
				String birthday = getString(R.string.tvBirthday);
				String mail = getString(R.string.tvMail);

				tvID.setText(id + infoList[0]); // ����TextView������
				tvName.setText(name + infoList[1]);
				tvSex.setText(sex + infoList[4]);
				tvBirthday.setText(birthday + infoList[5]);
				tvMail.setText(mail + infoList[2]);

				ImageButton iv = (ImageButton) findViewById(R.id.ivHead);
				// iv.setScaleType(ImageButton.ScaleType.CENTER_CROP);
				iv.setPadding(8, 8, 8, 8);
				iv.getBackground().setAlpha(50);

				iv.setScaleType(ImageButton.ScaleType.FIT_CENTER);
				iv.setLayoutParams(new LinearLayout.LayoutParams(150, 200));
				iv.setImageBitmap(head); // ����ͷ��
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
		setContentView(R.layout.main1);
		Intent intent = getIntent(); // ���������Activity��Intent����
		uno = intent.getStringExtra("uno"); // ��õ�ǰ�û���id
		// infoList = intent.getStringArrayExtra("infoList");
		// buf = intent.getByteArrayExtra("buf");
		// headSize = intent.getIntExtra("headSize", 0);
		//
		// head = BitmapFactory.decodeByteArray(buf, 0, headSize);
		// // head = intent.getByteArrayExtra("head");

		ImageButton btnPubDate = (ImageButton) findViewById(R.id.btnDatePub);
		btnPubDate.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnPubDate.setPadding(8, 8, 8, 8);
		btnPubDate.getBackground().setAlpha(100);
		btnPubDate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PubDateActivity.class);
				intent.putExtra("uno", uno);
				startActivity(intent);
				finish();
			}
		});

		ImageButton btnDateList = (ImageButton) findViewById(R.id.btnDateList);
		btnDateList.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnDateList.setPadding(8, 8, 8, 8);
		btnDateList.getBackground().setAlpha(100);
		btnDateList.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						DateListActivity.class);
				intent.putExtra("uno", uno);
				startActivity(intent);
				finish();
			}
		});

		ImageButton btnFriendList = (ImageButton) findViewById(R.id.btnFriendList);
		btnFriendList.setScaleType(ImageButton.ScaleType.CENTER_CROP);
		btnFriendList.setPadding(8, 8, 8, 8);
		btnFriendList.getBackground().setAlpha(100);
		btnFriendList.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ContactsActivity.class);
				intent.putExtra("uno", uno);
				startActivity(intent);
				finish();
			}
		});

		TextView btnModify = (TextView) findViewById(R.id.btnModify);
		btnModify.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ModifyActivity.class);
				intent.putExtra("uno", uno);
				startActivity(intent);
				finish();
			}
		});
		TextView btnModifyPwd = (TextView) findViewById(R.id.btnModifyPwd);
		btnModifyPwd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PwdModifyActivity.class);
				intent.putExtra("uno", uno);
				startActivity(intent);
				finish();
			}
		});

		LinearLayout operateLayout = (LinearLayout) findViewById(R.id.operateLayout);
		if (uno.equals("0")) {
			operateLayout.setVisibility(View.INVISIBLE);
			btnPubDate.setClickable(false);
		}
		getUser();
		openGPSSettings();
		timer.schedule(task, 50000, 50000);
	}

	// ��������ȡ�û���Ϣ
	public void getUser() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
					mc.dout.writeUTF("<#UserInfo#>" + uno); // ���������������

					String fInfo = mc.din.readUTF(); // ��ȡ������Ϣ
					infoList = fInfo.split("\\|"); // �ָ��ַ���

					int headSize = mc.din.readInt(); // ��ȡͷ���С
					byte[] buf = new byte[headSize]; // ����������
					mc.din.read(buf); // ��ȡͷ����Ϣ
					// head = BitmapFactory.decodeByteArray(buf, 0, headSize);

					// head =
					// BitmapFactory.decodeFile("/sdcard/date.data/"+uno+".jpg");
					// head =
					// getLoacalBitmap("/data/data/com.date/"+uno+".jpg");
					// //�ӱ���ȡͼƬ
					// head =BitmapFactory.decodeResource(this.getResource(),
					// R.drawable.head1);

					BufferedInputStream bis = new BufferedInputStream(
							getAssets().open(uno + ".jpg"));
					head = BitmapFactory.decodeStream(bis);

					myHandler.sendEmptyMessage(0); // ����Handler��Ϣ

					// if(!latitude.equals(null)&&!longitude.equals(null)){
					// //
					// }
					//
				} catch (Exception e) {
					e.printStackTrace();
					// pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()) {
						Toast.makeText(MainActivity.this, "��ȡ���ݳ�ʱ",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					} else {
						Toast.makeText(MainActivity.this, "���ӳ�ʱ",
								Toast.LENGTH_LONG).show();
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

	private void openGPSSettings() {

		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPSģ������", Toast.LENGTH_SHORT).show();
			getLocation();
			return;
		}
		Toast.makeText(this, "�뿪��GPS��", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0); // ��Ϊ������ɺ󷵻ص���ȡ���� }

		//
		// new Thread(){
		// public void run(){
		// try{
		// mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); //����MyConnector����
		// mc.dout.writeUTF("<#UPDATE_GPS#>"+uno+latitude+longitude); //���������������
		//
		// String result = mc.din.readUTF();
		// if(result.startsWith("<#UPDATE_GPS_SUCCESS#>")){ //������ϢΪ���³ɹ�
		//
		// }
		// else{ //����ʧ��
		// Toast.makeText(MainActivity.this, "��γ��ʧ�ܣ���鿴GPS���ӣ�",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		// }.start();

	}

	private void getLocation() {
		// ��ȡλ�ù������
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
		// ���ҵ�������Ϣ
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �߾���
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// �͹���
		String provider = locationManager.getBestProvider(criteria, true);
		// ��ȡGPS��Ϣ
		Location location = locationManager.getLastKnownLocation(provider);
		// Location location =
		// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// ͨ��GPS��ȡλ��
		updateToNewLocation(location);
		// ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N��
		locationManager.requestLocationUpdates(provider, 2 * 1000, 500,
				locationListener);
	}

	private void updateToNewLocation(Location location) {

		if (location != null) {
			latitude = Double.toString((double) location.getLatitude());// ����
			longitude = Double.toString((double) location.getLongitude());// γ��
			// double altitude = location.getAltitude(); //����
			mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
			try {
				mc.dout.writeUTF("<#UPDATE_GPS#>" + uno + "|" + latitude + "|"
						+ longitude);
				String result = mc.din.readUTF();
				if (result.startsWith("<#UPDATE_GPS_SUCCESS#>")) { // ������ϢΪ���³ɹ�

				} else { // ����ʧ��
					Toast.makeText(MainActivity.this, "��γ��ʧ�ܣ���鿴GPS���ӣ�",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // ���������������

		} else {

		}
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// ������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
			if (location != null) {
				updateToNewLocation(location);
			}
		}

		public void onProviderDisabled(String provider) {
			// Provider��disableʱ�����˺���������GPS���ر�
		}

		public void onProviderEnabled(String provider) {
			// Provider��enableʱ�����˺���������GPS����
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
		}
	};

	@Override
	protected void onDestroy() {
		if (mc != null) {
			mc.sayBye();
		}
		super.onDestroy();
	}

	// ����֪ͨ����Ϣ��������
	NotificationManager m_NotificationManager;
	Intent m_Intent;
	PendingIntent m_PendingIntent;
	// ����Notification����
	Notification m_Notification;

	void showNotification() {
		// ��ʼ��NotificationManager����
		m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// ���֪ͨʱת������
		// m_Intent = new Intent(MainActivity.this, ChatActivity.class);

		// ��Ҫ�����õ��֪ͨʱ��ʾ���ݵ���
		m_PendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
				getIntent(), 0); // ����D�ƃ��݄t��m_Intent();

		// ����Notification����
		m_Notification = new Notification();

		// ����֪ͨ��״̬����ʾ��ͼ��
		m_Notification.icon = R.drawable.icon;
		// �����ǵ��֪ͨʱ��ʾ������
		m_Notification.tickerText = "����Լ��� ������֪ͨ";
		// ֪ͨʱ����Ĭ�ϵ�����
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		// ����֪ͨ��ʾ�Ĳ���
		m_Notification.setLatestEventInfo(MainActivity.this, "����Լ���", "����֪ͨ",
				m_PendingIntent);
		// �������Ϊִ�����֪ͨ
		m_NotificationManager.notify(0, m_Notification);
	}

	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.what = 1;
			mhandler.sendMessage(message);
		}
	};;
	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// Ҫ��������
			super.handleMessage(msg);
			getIfChat();
		}
	};

	public void getIfChat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
					mc.dout.writeUTF("<#CHAT_IF#>" + uno); // ���������������

					int size = mc.din.readInt();
					if (size > 0) {
						showNotification();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onBackPressed() {
		openExitDialog();
	}

	// �滮�˵���
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.aboutus_infos).setIcon(R.drawable.about2)
				.setAlphabeticShortcut('A');
		menu.add(0, 2, 2, R.string.app_about).setIcon(R.drawable.help)
				.setAlphabeticShortcut('H');
		menu.add(0, 3, 3, R.string.exit).setIcon(R.drawable.exit1)
				.setAlphabeticShortcut('E');
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:
			openAboutDialog();
			break;
		case 2:
			openHelpDialog();
			break;
		case 3:
			openExitDialog();
			break;
		}
		return true;
	}

	// ������Ϣ��ʾ
	private void openAboutDialog() {
		LayoutInflater inflater = (LayoutInflater) MainActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(R.string.aboutus_infos)
				.setIcon(R.drawable.about2).setView(v).show();
	}

	// ����
	private void openHelpDialog() {
		LayoutInflater inflater = (LayoutInflater) MainActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.help_main, null);
		new AlertDialog.Builder(this)
				.setTitle(R.string.app_about)
				.setView(v)
				.setIcon(R.drawable.help)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).show();
	}

	// �˳��Ի���
	private void openExitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setIcon(R.drawable.exit)
				.setMessage(R.string.app_exit_msg)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								android.os.Process
										.killProcess(android.os.Process.myPid());
								// finish();
							}
						}).show();
	}

}
