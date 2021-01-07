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
	Bitmap headList = null;	//���ͷ�������
	String uano = null; // ��¼��ǰ�û���id
	String ubno = null; // ��¼�������id
	ArrayList<String[]> chatList = new ArrayList<String[]>();
	ArrayList<String[]> infoList = new ArrayList<String[]>();// ����û�����Ϣ��id���������Ա�ͷ��
	MyConnector mc = null; // ��������������
	ListView lv = null;
    String photoname=null;
	@SuppressLint("InlinedApi")
	BaseAdapter baContacts = new BaseAdapter() {
	//getView�����е�������������һposition��ָ�����ǵڼ�����Ŀ���ڶ�convertView���Ѿ����ƺ��˵���ͼ��parent��ListView֮���View��ͼ��
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(ChatActivity.this); 
			// �������Բ��� ,���ø�Activity��ʾlayout
			ll.setOrientation(LinearLayout.HORIZONTAL);            /*setOrientation����ˮƽ����  LinearLayout.HORIZONTAL��ʾˮƽ����  */
			//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			//�˴��൱�ڲ����ļ��е�Android:layout_gravity����
			//lp.gravity = Gravity.BOTTOM;		
			//lv = (ListView) findViewById(R.id.listChat); // ���ListView���������
			//lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			//lv.setStackFromBottom(true);		


	        
			TextView tvChat = new TextView(ChatActivity.this); // ����������ʾ������TextView
			tvChat.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
			
			// tvChat.setText(infoList.get(position)[1]+": "+chatList.get(position)[3]+" ");//����TextView������
			tvChat.setTextSize(16.0f); // ���������С
			// tvChat.setTextColor(Color.BLUE); //����������ɫ
			tvChat.setPadding(20, 0, 0, 0); // ���ñ߽�հ�
			if (chatList.get(position)[1].equals(uano)) {

/*				Drawable drawable= getResources().getDrawable(R.raw.p2004);
				/// ��һ������Ҫ��,���򲻻���ʾ.
				drawable.setBounds(0, 0, 30, 30);
				TextView tvTu = new TextView(ChatActivity.this); // ����������ʾ������TextView
				tvTu.setCompoundDrawables(null,null,drawable,null);*/
				ImageView iv = new ImageView(ChatActivity.this);			//����ImageView����
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				try {
					bis = new BufferedInputStream(
							getAssets().open(chatList.get(position)[1]+ ".jpg"));
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				// ���ֽ���ת��ΪBitmap,ʹ��BitmapFactory���������ת��
				headList = BitmapFactory.decodeStream(bis);
				iv.setImageBitmap(headList);		//����ͷ��
				iv.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
				iv.setPadding(0, 0, 0, 0);	
				
				LinearLayout ll1 = new LinearLayout(ChatActivity.this); // �������Բ���
				ll1.setOrientation(LinearLayout.VERTICAL);
				
				tvChat.setText(chatList.get(position)[3]);// ����TextView������                
                tvChat.setBackgroundDrawable(getResources().getDrawable(R.drawable.txt_radiuborder));               
				tvChat.setTextColor(Color.BLACK);/*���������ɫ*/
				ll.setGravity(Gravity.RIGHT);/*�����Ҷ���*/
				
				TextView tvStyle = new TextView(ChatActivity.this); // ����������ʾ������TextView
				tvStyle.setGravity(Gravity.RIGHT); // ����TextView�Ķ��뷽ʽ
				tvStyle.setText( ":"+chatList.get(position)[1]+"(�� )");// ����TextView������
				tvStyle.setTextSize(10.0f); // ���������С
				tvStyle.setTextColor(Color.BLACK); // ����������ɫ
				tvStyle.setPadding(20, 0, 0, 0);
				// tvChat.setGravity(Gravity.RIGHT);
				ll1.addView(tvStyle);
				ll1.addView(tvChat);
				ll.addView(ll1);
				//ll.addView(tvTu);
                ll.addView(iv);
			} else {
/*				Drawable drawable= getResources().getDrawable(R.drawable.girl);
				/// ��һ������Ҫ��,���򲻻���ʾ.
				drawable.setBounds(0, 0, 30, 30);
				TextView tvTu1 = new TextView(ChatActivity.this); // ����������ʾ������TextView
				tvTu1.setCompoundDrawables(drawable,null,null,null);*/
				ImageView iv1 = new ImageView(ChatActivity.this);			//����ImageView����
				iv1.setScaleType(ImageView.ScaleType.FIT_CENTER);
				try {
					bis = new BufferedInputStream(
							getAssets().open(chatList.get(position)[1]+ ".jpg"));
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				headList = BitmapFactory.decodeStream(bis);
				headList = toRoundBitmap(headList);
				iv1.setImageBitmap(headList);		//����ͷ��
				iv1.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
				iv1.setPadding(0, 0, 0, 0);	
				
				LinearLayout ll2 = new LinearLayout(ChatActivity.this); // �������Բ���
				ll2.setOrientation(LinearLayout.VERTICAL);
				
				TextView tvStyle1 = new TextView(ChatActivity.this); // ����������ʾ������TextView
				tvStyle1.setGravity(Gravity.LEFT); // ����TextView�Ķ��뷽ʽ
				tvStyle1.setText( chatList.get(position)[1]+":");// ����TextView������
				tvStyle1.setTextSize(10.0f); // ���������С
				tvStyle1.setTextColor(Color.BLUE); // ����������ɫ
				tvStyle1.setPadding(20, 0, 0, 0);
				
				tvChat.setText(chatList.get(position)[3]);// ����TextView������
	
                tvChat.setBackgroundDrawable(getResources().getDrawable(R.drawable.txt_rectborder));
				tvChat.setTextColor(Color.BLUE);/*����������ɫ*/
				ll.setGravity(Gravity.LEFT);/*���������*/
				ll2.addView(tvStyle1);				
				ll2.addView(tvChat);
				ll.addView(iv1);
                //ll.addView(tvTu1);
                ll.addView(ll2);

			}
			// ����ʾ������TextView��ӵ����Բ���

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
			super.handleMessage(msg);//����handleMessage������������д��handleMessage����������ķ��������ˡ�super.handleMessage��msg����������;߱��ˡ������ܹ�����һЩ���Ѿ�д�õ���Ϣ������������Ȼ��������дһ���Լ���Ҫ�������Ϣ��
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���ô������չ����,�ޱ���
		Intent intent = getIntent(); // ���������Activity��Intent����
		uano = intent.getStringExtra("uano"); // ��õ�ǰ�û���id
		ubno = intent.getStringExtra("ubno"); // �����������id

		setContentView(R.layout.chat);
//setContentView��������һ��Activity����ʾ���棬��仰�������������仰���ٵ�Activity����R.layout�µ�chat�����ļ����в���
		lv = (ListView) findViewById(R.id.listChat); // ���ListView���������
		getchatList();

		Button btnChat = (Button) findViewById(R.id.btnChat); // ��÷��ذ�ť����
		btnChat.getBackground().setAlpha(140);
		btnChat.setOnClickListener(new View.OnClickListener() { // Ϊ���ذ�ť��Ӽ�����
			
			public void onClick(View v) {
				chat();
			}
		});

		Button btnBack = (Button) findViewById(R.id.btnBack); // ��÷��ذ�ť����
		btnBack.getBackground().setAlpha(140);
		btnBack.setOnClickListener(new View.OnClickListener() { // Ϊ���ذ�ť��Ӽ�����
		
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this,
						MainActivity.class); // ����Intent����
				intent.putExtra("uno", uano);

				startActivity(intent); // ����Activity
				finish();
			}
		});
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {  
        //Բ��ͼƬ���  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //�����εı߳�  
        int r = 0;  
        //ȡ��̱����߳�  
        if(width > height) {  
            r = height;  
        } else {  
            r = width;  
        }  
        //����һ��bitmap ,createBitmap�ú�������һ�������ض���ȡ��߶Ⱥ���ɫ��ʽ��λͼ,ARGB_8888����32λARGBλͼ
        Bitmap backgroundBmp = Bitmap.createBitmap(width,height, Config.ARGB_8888);  
        Canvas canvas = new Canvas(backgroundBmp);  //newһ��Canvas����backgroundBmp�ϻ�ͼ .ͨ������װ�ػ���Bitmap���󴴽�Canvas����
        Paint paint = new Paint();  //����һ������      
        paint.setAntiAlias(true); //���ñ�Ե�⻬��ȥ�����  
        paint.setDither(true);  //������ 
        paint.setStrokeWidth(1);//���û��ʵĿ��     
        RectF rect = new RectF(0, 0, r, r);   //�����ȣ���������  
        //RectF(float left, float top, float right, float bottom),���εĿ��width=right-left���߶�height=bottom-top
        canvas.drawRoundRect(rect, r/2, r/2, paint);  
        //ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ,�Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��          
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��
        canvas.drawBitmap(bitmap, null, rect, paint);
        //canvas��bitmap����backgroundBmp��         
        return backgroundBmp;  
        //�����Ѿ��滭�õ�backgroundBmp         
    } 

	public void chat() {
		new Thread() {
			public void run() {
				try {
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT); // ����MyConnector����
					EditText etChat = (EditText) findViewById(R.id.etChat);
					String ctext = etChat.getEditableText().toString().trim();
//getStringValue()���ǻ�ȡ��ǰ�ڵ������ڵ��������ı��������ӳɵ��ַ���
//��ö�����ֶε�ֵ��Ȼ��ת��string���ͣ�����ȥ��ǰ��հ�~~ToString()��ת��Ϊ�ַ����ķ��� Trim()��ȥ���߿ո�ķ���
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
		//putExtra("A",B)�У�ABΪ��ֵ�ԣ���һ������Ϊ�������ڶ�������Ϊ����Ӧ��ֵ��
						startActivity(intent); //   //������Intent����ʵ����ת ��������Activity
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
