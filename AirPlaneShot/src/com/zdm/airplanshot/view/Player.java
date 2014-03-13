package com.zdm.airplanshot.view;

import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Player {

	private int playerHp = 3;
	private Bitmap bmpPlayerHp;// 主角飞机血量
	private Bitmap bmpPlayer;// 游戏主角飞机

	private float x, y;
	private float lastTx = 0, lastTy = 0;
	private long lastCalTime = 0;
	private int speed = 10;

	public Player(Bitmap bmpPlayerHp, Bitmap bmpPlayer) {
		super();
		this.bmpPlayerHp = bmpPlayerHp;
		this.bmpPlayer = bmpPlayer;
		x = MySurfaceView.screenW / 2 - bmpPlayer.getWidth() / 2;
		y = MySurfaceView.screenH - bmpPlayer.getHeight() - 10;
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmpPlayer, x, y, paint);
		// Log.w("game play draw","x="+x+";y="+y );
		for (int i = 0; i < playerHp; i++) {
			canvas.drawBitmap(bmpPlayerHp, 20 + (bmpPlayerHp.getWidth() + 5)
					* i, MySurfaceView.screenH - bmpPlayerHp.getHeight(), paint);
		}
	}

	public void onTouchEvent(MotionEvent event) {
		long calTime = Calendar.getInstance().getTimeInMillis();
		float tx = event.getX(), ty = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			if ((lastTx != 0 && lastTy != 0) && !(lastTx == tx && lastTy == ty)) {
				x = (float) (x + (tx - lastTx)
						/ Math.sqrt((Math.pow(tx - lastTx, 2) + Math.pow(ty
								- lastTy, 2))) * speed
						* (calTime - lastCalTime) / 50);
				y = (float) (y + (ty - lastTy)
						/ Math.sqrt((Math.pow(tx - lastTx, 2) + Math.pow(ty
								- lastTy, 2))) * speed
						* (calTime - lastCalTime) / 50);
			}
			lastCalTime = calTime;
			lastTx = tx;
			lastTy = ty;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			lastTx = 0;
			lastTy = 0;
			lastCalTime = 0;
		}
		if (x <= 0) {
			x = 0;
		}
		if (y >= MySurfaceView.screenH - bmpPlayer.getHeight() - 10) {
			y = MySurfaceView.screenH - bmpPlayer.getHeight() - 10;
		}
		// Log.w("game play touch", "x=" + x + ";y=" + y);
		// Log.w("game player",
		// "x="+x+";y="+y+";tx="+tx+";ty="+ty+" lastTx="+lastTx+";lastTy="+lastTy);

	}

	public void logic() {

	}

}
