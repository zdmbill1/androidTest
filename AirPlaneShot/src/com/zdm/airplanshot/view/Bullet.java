package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Bullet {

	public Bitmap bmpBullet;
	public float x = 0, y = 0;
	public int speed = 10;
	public boolean isDead = false;

	public Bullet(Bitmap bmpBullet, float x, float y) {
		super();
		this.bmpBullet = bmpBullet;
		this.x = x;
		this.y = y;
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpBullet, x, y, paint);
	}
	
	public abstract void logic();

}
