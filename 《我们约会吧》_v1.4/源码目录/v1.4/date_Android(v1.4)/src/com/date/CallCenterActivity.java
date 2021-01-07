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
    public void onCreate(Bundle savedInstanceState) { //在Activity创建时被系统调用,是一个Activity生命周期的开始
        super.onCreate(savedInstanceState);           //调用基类中的onCreate方法, savedInstanceState是保存当前Activity的状态信息
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.callcenter);          //所有view显示出来
		
		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);//获得返回按钮对象
		btnBack.setScaleType(ImageButton.ScaleType.CENTER_CROP);//设置留白
		btnBack.setPadding(8, 8, 8, 8);                         //
		btnBack.getBackground().setAlpha(100);
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
		
			public void onClick(View v) {									//启动Activity
				finish();
			}
		});
    }
	
}
