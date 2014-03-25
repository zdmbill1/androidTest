package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boom {

	public Bitmap bmpBoom;
	public int totalFrames = 7;
	public int currectFram = 0;
	public float x, y;
	public boolean ended = false;

	// 传过来的为爆炸中心点
	public Boom(Bitmap bmpBoom, float x, float y) {
		super();
		this.bmpBoom = bmpBoom;
		this.x = x - (bmpBoom.getWidth() / totalFrames) / 2;
		this.y = y - bmpBoom.getHeight() / 2;
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(x, y, x + bmpBoom.getWidth() / totalFrames,
				y + bmpBoom.getHeight());
		canvas.drawBitmap(bmpBoom, x - bmpBoom.getWidth() / totalFrames
				* currectFram, y, paint);
		canvas.restore();
	}

	public void logic() {
		currectFram = currectFram + 1;
		if (currectFram == totalFrames) {
			ended = true;
		}
	}

}
