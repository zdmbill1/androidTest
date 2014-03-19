package com.zdm.airplanshot.view;

import android.graphics.Bitmap;

public class BulletEnemy extends Bullet {

	public BulletEnemy(Bitmap bmpBullet, float x, float y) {
		super(bmpBullet, x, y);
		
	}

	@Override
	public void logic() {
		y=y+speed;
		if(y>=MySurfaceView.screenH){
			isDead=true;
		}

	}

}
