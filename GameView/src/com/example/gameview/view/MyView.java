package com.example.gameview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyView extends View {

	private float x = 20, y = 20;

	public MyView(Context context) {
		super(context);
		// 让本视图可以被监听,仅仅是keydown/up事件
//		setFocusable(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawText("game test", x, y, paint);
		super.onDraw(canvas);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 通过event.getAction()与不同MotionEvent匹配

		Toast.makeText(getContext(), "action="+event.getAction()+" x="+x+" y="+y, Toast.LENGTH_SHORT).show();
		x = event.getX();
		y = event.getY();
		// 调用ondraw方法，可以在子线程中调用
		// Invalidate不可以在子线程
		postInvalidate();
		return true;
	}

}
