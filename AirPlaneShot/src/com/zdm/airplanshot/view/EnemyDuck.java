package com.zdm.airplanshot.view;

import java.util.Random;

import android.graphics.Bitmap;

public class EnemyDuck extends Enemy {

	public EnemyDuck(Bitmap bmp, float x, float y) {
		super(bmp, x, y);
		speed = 3;
	}

	@Override
	public void logic() {
		super.logic();
		if (!isDead) {
			Random r = new Random();
			if (r.nextBoolean()) {
				x = x + speed / 2;
			} else {
				x = x - speed / 2;
			}
			if (x > MySurfaceView.screenW - frameW) {
				x = MySurfaceView.screenW - frameW;
			} else if (x < 0) {
				x = 0;
			}

			y = y + speed;
		}
		if (y >= MySurfaceView.screenH) {
			isDead = true;
		}
	}

}
