package com.zdm.airplanshot.view;

import android.graphics.Bitmap;

public class EnemyFly extends Enemy {

	public EnemyFly(Bitmap bmp, float x, float y) {
		super(bmp, x, y);
		speed = 8;
	}

	@Override
	public void logic() {
		super.logic();
		if (!isDead) {
			y = y + speed;
		}
		if (y >= MySurfaceView.screenH) {
			isDead = true;
		}
	}

}
