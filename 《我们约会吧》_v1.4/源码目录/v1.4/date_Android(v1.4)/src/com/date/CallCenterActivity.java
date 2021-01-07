package com.date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class CallCenterActivity extends Activity {
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { //��Activity����ʱ��ϵͳ����,��һ��Activity�������ڵĿ�ʼ
        super.onCreate(savedInstanceState);           //���û����е�onCreate����, savedInstanceState�Ǳ��浱ǰActivity��״̬��Ϣ
        requestWindowFeature(Window.FEATURE_NO_TITLE);//�ޱ���
        setContentView(R.layout.callcenter);          //����view��ʾ����
		
		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);//��÷��ذ�ť����
		btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);//��������
		btnBack.setPadding(8, 8, 8, 8);                         //
		btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
		
			public void onClick(View v) {									//����Activity
				finish();
			}
		});
    }
	
}
