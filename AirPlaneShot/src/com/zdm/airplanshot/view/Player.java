package com.zdm.airplanshot.view;

import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Player {

	private int playerHp = 3;
	private Bitmap bmpPlayerHp;// 主角飞机血量
	public Bitmap bmpPlayer;// 游戏主角飞机

	public float x, y;
	private float lastTx = 0, lastTy = 0;
	private long lastCalTime = 0;
	private int speed = 10;

	public Player(Bitmap bmpPlayerHp, Bitmap bmpPlayer) {
		super();
		this.bmpPlayerHp = bmpPlayerHp;
		this.bmpPlayer = bmpPlayer;
		x = MySurfaceView.screenW / 2 - bmpPlayer.getWidth() / 2;
		y = MySurfaceView.screenH - bmpPlayer.getHeight() - 10;
	}

	public void draw(Canvas canvas, Paint paint) {
		if (collsionCount % 2 == 0) {
			canvas.drawBitmap(bmpPlayer, x, y, paint);
		}
		// Log.w("game play draw","x="+x+";y="+y );
		for (int i = 0; i < playerHp; i++) {
			canvas.drawBitmap(bmpPlayerHp, 20 + (bmpPlayerHp.getWidth() + 5)
					* i, MySurfaceView.screenH - bmpPlayerHp.getHeight(), paint);
		}
	}

	public void onTouchEvent(MotionEvent event) {
		long calTime = Calendar.getInstance().getTimeInMillis();
		float tx = event.getX(), ty = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			if ((lastTx != 0 && lastTy != 0) && !(lastTx == tx && lastTy == ty)) {
				x = (float) (x + (tx - lastTx)
						/ Math.sqrt((Math.pow(tx - lastTx, 2) + Math.pow(ty
								- lastTy, 2))) * speed
						* (calTime - lastCalTime) / 50);
				y = (float) (y + (ty - lastTy)
						/ Math.sqrt((Math.pow(tx - lastTx, 2) + Math.pow(ty
								- lastTy, 2))) * speed
						* (calTime - lastCalTime) / 50);
			}
			lastCalTime = calTime;
			lastTx = tx;
			lastTy = ty;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			lastTx = 0;
			lastTy = 0;
			lastCalTime = 0;
		}
		if (x <= 0) {
			x = 0;
		}
		if (x >= MySurfaceView.screenW - bmpPlayer.getWidth()) {
			x = MySurfaceView.screenW - bmpPlayer.getWidth();
		}
		if (y >= MySurfaceView.screenH - bmpPlayer.getHeight() - 10) {
			y = MySurfaceView.screenH - bmpPlayer.getHeight() - 10;
		}
		if (y <= 0) {
			y = 0;
		}
		// Log.w("game play touch", "x=" + x + ";y=" + y);
		// Log.w("game player",
		// "x="+x+";y="+y+";tx="+tx+";ty="+ty+" lastTx="+lastTx+";lastTy="+lastTy);

	}

	private boolean isCollision = false;

	public boolean isCollision(Enemy en) {
		if (!isCollision) {
			// 矩形碰撞检测
			// 飞机在敌机右侧
			if (x >= en.x + en.frameW) {
				return false;
				// 飞机在左侧
			} else if (x + bmpPlayer.getWidth() <= en.x) {
				return false;
				// 飞机在上侧
			} else if (y + bmpPlayer.getHeight() <= en.y) {
				return false;
				// 飞机在下侧
			} else if (y >= en.y + en.frameH) {
				return false;
			}
			isCollision = true;
			Log.w("game player", "isCollsion = true");
			return false;
		} else {
			return false;
		}
	}

	public boolean isCollision(Bullet bullet) {
		if (!isCollision) {
			// 矩形碰撞检测
			// 飞机在敌机右侧
			if (x >= bullet.x + bullet.bmpBullet.getWidth()) {
				return false;
				// 飞机在左侧
			} else if (x + bmpPlayer.getWidth() <= bullet.x) {
				return false;
				// 飞机在上侧
			} else if (y + bmpPlayer.getHeight() <= bullet.y) {
				return false;
				// 飞机在下侧
			} else if (y >= bullet.y + bullet.bmpBullet.getHeight()) {
				return false;
			}
			isCollision = true;
			Log.w("game player", "isCollsion with bullet = true");
			return true;
		} else {
			return false;
		}
	}

	public void setPlayerHp(Enemy en) {
		// TODO 不同敌机伤害不一样。。。
		playerHp = playerHp - 1;
	}

	private int collsionCount = 0;

	public void logic() {
		if (isCollision) {
			collsionCount++;
			// 碰撞过后的无敌时间=20*50ms
			if (collsionCount >= 20) {
				isCollision = false;
				collsionCount = 0;
			}
		}
	}

}
