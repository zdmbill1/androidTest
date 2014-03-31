package com.zdm.airplanshot.view;

import java.util.HashSet;

import android.graphics.Bitmap;

public class EnemyBoss extends Enemy {

	public boolean isRight = true;
	boolean crazy = false;
	private int maxBossHp=10;
	private int bossHp = maxBossHp;	
	private int fireType=-1; 

	public int getBossHp() {
		return bossHp;
	}

	public void setBossHp(int delHp) {
		bossHp = bossHp - delHp;
		if (bossHp <= maxBossHp / 2) {
			crazy = true;
		}
		if (bossHp <= 0) {
			isDead = true;
		}
	}

	public EnemyBoss(Bitmap bmp, float x, float y) {
		super(bmp, x, y);

	}

	@Override
	public void logic() {
		super.logic();
		if (x + frameW >= MySurfaceView.screenW - 100) {
			isRight = false;
			// x = MySurfaceView.screenW - frameW;
		} else if (x <= 100) {
			isRight = true;
			// x = 0;
		}
		if (isRight) {
			x = x + speed;
		} else {
			x = x - speed;
		}
	}

	@Override
	public boolean isCollision(Bullet bullet) {
		boolean flag = super.isCollision(bullet);
		isDead = false;
		return flag;
	}

	public HashSet<Bullet> addRectBullets() {

		HashSet<Bullet> rectBullets = new HashSet<Bullet>();
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x + frameW
				/ 2, y, BulletBoss.DIR_UP));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x + frameW
				/ 2, y + frameH, BulletBoss.DIR_DOWN));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x, y
				+ frameH / 2, BulletBoss.DIR_LEFT));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x + frameW,
				y + frameH / 2, BulletBoss.DIR_RIGHT));

		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x + frameW,
				y, BulletBoss.DIR_UP_RIGHT));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x + frameW,
				y + frameH, BulletBoss.DIR_DOWN_RIGHT));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x, y
				+ frameH, BulletBoss.DIR_DOWN_LEFT));
		rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x, y,
				BulletBoss.DIR_UP_LEFT));
		return rectBullets;

	}

	public HashSet<Bullet> addCircleBullets() {
		//r=frameW/2,圆心x=x+frameW/2,y=y+frameH/2
		HashSet<Bullet> rectBullets = new HashSet<Bullet>();
		double angle=0;
		for(int i=0;i<8;i++){
			angle=45*i;
			float x0=x+frameW/2+(frameW/2)*(float)Math.round(Math.sin(Math.toRadians(angle))*1000)/1000;
			float y0=y+frameH/2+(frameW/2)*(float)Math.round(Math.cos(Math.toRadians(angle))*1000)/1000;
			
			rectBullets.add(new BulletBoss(MySurfaceView.bmpBossBullet, x0,y0, angle));
		}
		return rectBullets;
	}
	
	public HashSet<Bullet> addBullets() {
		fireType = fireType + 1;
		if (fireType == 2) {
			fireType = 0;
		}
		if (fireType % 2 == 0) {
			return addRectBullets();
		} else if (fireType % 2 == 1) {
			return addCircleBullets();
		} else {
			return addRectBullets();
		}
	}
}
