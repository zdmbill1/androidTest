package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameBackGround {

	private Bitmap bmpBackGround1;// 游戏背景
	private Bitmap bmpBackGround2;

	private int speed = 2;
	private int bg1y, bg2y;

	private float scaleH;

	public GameBackGround(Bitmap bmpBackGround) {
		scaleH=(float) MySurfaceView.screenW / (float) bmpBackGround.getWidth();
		int h = (int) (scaleH * bmpBackGround.getHeight());

		bmpBackGround1 = Bitmap.createScaledBitmap(bmpBackGround,
				(int) MySurfaceView.screenW, (int) h, true);
		bmpBackGround2 = Bitmap.createScaledBitmap(bmpBackGround,
				(int) MySurfaceView.screenW, (int) h, true);

		bg1y = MySurfaceView.screenH - h;
		bg2y = bg1y - bmpBackGround2.getHeight()+(int)(111*scaleH);		
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmpBackGround1, 0, bg1y, paint);
		canvas.drawBitmap(bmpBackGround2, 0, bg2y, paint);
	}

	public void logic() {
		bg1y = bg1y + speed;
		bg2y = bg2y + speed;
		if (bg1y >= MySurfaceView.screenH) {
			bg1y = bg2y - bmpBackGround1.getHeight()+(int)(111*scaleH);
		}

		if (bg2y >= MySurfaceView.screenH) {
			bg2y = bg1y - bmpBackGround2.getHeight()+(int)(111*scaleH);
		}
	}

}
