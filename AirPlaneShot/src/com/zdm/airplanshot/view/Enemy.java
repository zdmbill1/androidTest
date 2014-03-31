package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public abstract class Enemy {

	protected Bitmap bmp;
	protected float x = 0, y = 0;
	protected int frameW, frameH, frameIndex = 0;
	protected int speed = 5;

	protected boolean isDead = false;

	public Enemy(Bitmap bmp, float x, float y) {
		this.bmp = bmp;
		this.x = x;
		this.y = y;
		frameW = bmp.getWidth() / 10;
		frameH = bmp.getHeight();
		if (x < 0) {
			x = 0;
		} else if (x > MySurfaceView.screenW - frameW - 10) {
			x = MySurfaceView.screenW - frameW - 10;
		}
		// Log.w("game enemy", "x=" + x);
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(x, y, x + frameW, y + frameH);
		canvas.drawBitmap(bmp, x - frameW * frameIndex, y, paint);
		canvas.restore();
	}

	public void logic() {
		frameIndex = frameIndex + 1;
		if (frameIndex > 9) {
			frameIndex = 0;
		}
	}

	public boolean isCollision(Bullet bullet) {
		// 矩形碰撞检测
		// 敌机在子弹右侧
		if (x >= bullet.x + bullet.bmpBullet.getWidth()) {
			return false;
			// 敌机在左侧
		} else if (x + bmp.getWidth()/10 <= bullet.x) {
			return false;
			// 敌机在上侧
		} else if (y + bmp.getHeight() <= bullet.y) {
			return false;
			// 敌机在下侧
		} else if (y >= bullet.y + bullet.bmpBullet.getHeight()) {
			return false;
		}
		isDead = true;
		return true;
	}
}
