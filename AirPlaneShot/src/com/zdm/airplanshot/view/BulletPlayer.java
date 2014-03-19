package com.zdm.airplanshot.view;

import android.graphics.Bitmap;

public class BulletPlayer extends Bullet {

	public BulletPlayer(Bitmap bmpBullet, float x, float y) {
		super(bmpBullet, x, y);
		speed=5;
	}

	@Override
	public void logic() {
		y=y-speed;
		if(y<=-bmpBullet.getHeight()){
			isDead=true;
		}
	}

}
