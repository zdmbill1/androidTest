package com.zdm.airplanshot.view;

import android.graphics.Bitmap;

public class BulletBoss extends Bullet {

	private int direct = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_DOWN = 2;
	public static final int DIR_LEFT = 3;
	public static final int DIR_RIGHT = 4;
	public static final int DIR_UP_RIGHT = 5;
	public static final int DIR_DOWN_RIGHT = 6;
	public static final int DIR_DOWN_LEFT = 7;
	public static final int DIR_UP_LEFT = 8;

	public BulletBoss(Bitmap bmpBullet, float x, float y, int direct) {
		super(bmpBullet, x, y);
		this.direct = direct;
	}

	// 角度
	private double angle = 0;

	public BulletBoss(Bitmap bmpBullet, float x, float y, double angle) {
		super(bmpBullet, x, y);
		this.angle = angle;
	}

	@Override
	public void logic() {
		if (direct != 0) {
			// 矩形子弹
			switch (direct) {
			case DIR_UP:
				y = y - speed;
				break;
			case DIR_DOWN:
				y = y + speed;
				break;
			case DIR_LEFT:
				x = x - speed;
				break;
			case DIR_RIGHT:
				x = x + speed;
				break;
			case DIR_UP_RIGHT:
				y = y - speed;
				x = x + speed;
				break;
			case DIR_DOWN_RIGHT:
				y = y + speed;
				x = x + speed;
				break;
			case DIR_DOWN_LEFT:
				y = y + speed;
				x = x - speed;
				break;
			case DIR_UP_LEFT:
				y = y - speed;
				x = x - speed;
				break;
			default:
				break;
			}
		} else {
			// 圆形发射子弹			
			x=x+speed*(float)Math.round(Math.sin(Math.toRadians(angle))*1000)/1000;
			y=y+speed*(float)Math.round(Math.cos(Math.toRadians(angle))*1000)/1000;
			
		}

		if (x <= -bmpBullet.getWidth() || x >= MySurfaceView.screenW
				|| y <= -bmpBullet.getHeight() || y >= MySurfaceView.screenH) {
			isDead = true;
		}

	}

}
