package com.date.welcome;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class WelcomeDateActivity extends Activity implements Runnable {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyView mv = new MyView(this);
		setContentView(mv);
		new Thread(this).start();
	}

	public void run() {
		try {
			Thread.sleep(3000L);
			//进过上面的设定的时间，结束这个activity
			finish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class MyView extends View {

		private Bitmap splash;

		public MyView(Context context) {
			super(context);
			InputStream is = getClass().getResourceAsStream("/com/date/welcome/splash1.png");
			splash = BitmapFactory.decodeStream(is);
		}

		public void onDraw(Canvas c) {
			c.drawColor(0xFFFFFFFF);
			
			//这里里面的 left 和 top 的设置  为了给图片一个比较好的显示
			c.drawBitmap(splash, getWidth() /2  - splash.getWidth() / 2,
					getHeight() / 2 - splash.getHeight() / 2, null);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

}
