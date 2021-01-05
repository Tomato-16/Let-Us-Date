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
	String uno = null; // 记录当前用户的id
	// int type = -1; //为0表示显示好友列表，为1表示显示访客列表
	Bitmap head = null; // 存放头像
	String[] infoList = null; // 存放用户的信息：id、姓名、性别、头像
	// int headSize=0;
	// byte[] buf=null;
	MyConnector mc = null; // 网络连接器对象
	String latitude = null;// 经度
	String longitude = null;// 纬度

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

				tvID.setText(id + infoList[0]); // 设置TextView的内容
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
				iv.setImageBitmap(head); // 设置头像
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
		Intent intent = getIntent(); // 获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno"); // 获得当前用户的id
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

	// 方法：获取用户信息
	public void getUser() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象
					mc.dout.writeUTF("<#UserInfo#>" + uno); // 向服务器发出请求

					String fInfo = mc.din.readUTF(); // 读取好友信息
					infoList = fInfo.split("\\|"); // 分割字符串

					int headSize = mc.din.readInt(); // 读取头像大小
					byte[] buf = new byte[headSize]; // 创建缓冲区
					mc.din.read(buf); // 读取头像信息
					// head = BitmapFactory.decodeByteArray(buf, 0, headSize);

					// head =
					// BitmapFactory.decodeFile("/sdcard/date.data/"+uno+".jpg");
					// head =
					// getLoacalBitmap("/data/data/com.date/"+uno+".jpg");
					// //从本地取图片
					// head =BitmapFactory.decodeResource(this.getResource(),
					// R.drawable.head1);

					BufferedInputStream bis = new BufferedInputStream(
							getAssets().open(uno + ".jpg"));
					head = BitmapFactory.decodeStream(bis);

					myHandler.sendEmptyMessage(0); // 发出Handler消息

					// if(!latitude.equals(null)&&!longitude.equals(null)){
					// //
					// }
					//
				} catch (Exception e) {
					e.printStackTrace();
					// pd.dismiss();
					if (!mc.socket.isClosed() && mc.socket.isConnected()) {
						Toast.makeText(MainActivity.this, "读取数据超时",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					} else {
						Toast.makeText(MainActivity.this, "连接超时",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				}
			}
		}.start();
	}

	/**
	 * 加载本地图片
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
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
			getLocation();
			return;
		}
		Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面 }

		//
		// new Thread(){
		// public void run(){
		// try{
		// mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); //创建MyConnector对象
		// mc.dout.writeUTF("<#UPDATE_GPS#>"+uno+latitude+longitude); //向服务器发出请求
		//
		// String result = mc.din.readUTF();
		// if(result.startsWith("<#UPDATE_GPS_SUCCESS#>")){ //返回信息为更新成功
		//
		// }
		// else{ //更新失败
		// Toast.makeText(MainActivity.this, "经纬度失败！请查看GPS连接！",
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
		// 获取位置管理服务
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 低功耗
		String provider = locationManager.getBestProvider(criteria, true);
		// 获取GPS信息
		Location location = locationManager.getLastKnownLocation(provider);
		// Location location =
		// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// 通过GPS获取位置
		updateToNewLocation(location);
		// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
		locationManager.requestLocationUpdates(provider, 2 * 1000, 500,
				locationListener);
	}

	private void updateToNewLocation(Location location) {

		if (location != null) {
			latitude = Double.toString((double) location.getLatitude());// 经度
			longitude = Double.toString((double) location.getLongitude());// 纬度
			// double altitude = location.getAltitude(); //海拔
			mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象
			try {
				mc.dout.writeUTF("<#UPDATE_GPS#>" + uno + "|" + latitude + "|"
						+ longitude);
				String result = mc.din.readUTF();
				if (result.startsWith("<#UPDATE_GPS_SUCCESS#>")) { // 返回信息为更新成功

				} else { // 更新失败
					Toast.makeText(MainActivity.this, "经纬度失败！请查看GPS连接！",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 向服务器发出请求

		} else {

		}
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			if (location != null) {
				updateToNewLocation(location);
			}
		}

		public void onProviderDisabled(String provider) {
			// Provider被disable时触发此函数，比如GPS被关闭
		}

		public void onProviderEnabled(String provider) {
			// Provider被enable时触发此函数，比如GPS被打开
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		}
	};

	@Override
	protected void onDestroy() {
		if (mc != null) {
			mc.sayBye();
		}
		super.onDestroy();
	}

	// 声明通知（消息）管理器
	NotificationManager m_NotificationManager;
	Intent m_Intent;
	PendingIntent m_PendingIntent;
	// 声明Notification对象
	Notification m_Notification;

	void showNotification() {
		// 初始化NotificationManager对象
		m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 点击通知时转移内容
		// m_Intent = new Intent(MainActivity.this, ChatActivity.class);

		// 主要是设置点击通知时显示内容的类
		m_PendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
				getIntent(), 0); // 如果轉移內容則用m_Intent();

		// 构造Notification对象
		m_Notification = new Notification();

		// 设置通知在状态栏显示的图标
		m_Notification.icon = R.drawable.icon;
		// 当我们点击通知时显示的内容
		m_Notification.tickerText = "我们约会吧 新留言通知";
		// 通知时发出默认的声音
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		// 设置通知显示的参数
		m_Notification.setLatestEventInfo(MainActivity.this, "我们约会吧", "留言通知",
				m_PendingIntent);
		// 可以理解为执行这个通知
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
			// 要做的事情
			super.handleMessage(msg);
			getIfChat();
		}
	};

	public void getIfChat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // 创建MyConnector对象
					mc.dout.writeUTF("<#CHAT_IF#>" + uno); // 向服务器发出请求

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

	// 规划菜单栏
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

	// 帮助信息显示
	private void openAboutDialog() {
		LayoutInflater inflater = (LayoutInflater) MainActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(R.string.aboutus_infos)
				.setIcon(R.drawable.about2).setView(v).show();
	}

	// 帮助
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

	// 退出对话框
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
