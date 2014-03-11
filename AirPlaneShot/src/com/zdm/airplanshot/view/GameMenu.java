package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

public class GameMenu {

	// 菜单背景图
	private Bitmap bmpMenu;
	// 按钮图片资源(按下和未按下图)
	private Bitmap bmpButton, bmpButtonPress;
	// 按钮的坐标
	private int btnX, btnY;
	// 按钮是否按下标识位
	private Boolean isPress;

	// 菜单初始化
	public GameMenu(Bitmap bmpMenu, Bitmap bmpButton, Bitmap bmpButtonPress) {
		// this.bmpMenu = bmpMenu;
		this.bmpButton = bmpButton;
		this.bmpButtonPress = bmpButtonPress;
		// X居中，Y紧接屏幕底部
		btnX = MySurfaceView.screenW / 2 - bmpButton.getWidth() / 2;
		btnY = MySurfaceView.screenH - bmpButton.getHeight();
		isPress = false;
		this.bmpMenu = Bitmap.createScaledBitmap(bmpMenu,
				 MySurfaceView.screenW,  MySurfaceView.screenH, true);
		// this.bmpMenu = Bitmap.createBitmap(bmpMenu, 0, 0,
		// MySurfaceView.screenW, MySurfaceView.screenH);
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmpMenu, 0, 0, paint);
		if (isPress) {
			canvas.drawBitmap(bmpButton, btnX, btnY, paint);
		} else {
			canvas.drawBitmap(bmpButtonPress, btnX, btnY, paint);
		}
	}

	public void onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Rect rect = new Rect(btnX, btnY, btnX + bmpButton.getWidth(), btnY
				+ bmpButton.getHeight());
		Region r = new Region(rect);
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			if (r.contains(x, y)) {
				isPress = true;
			} else {
				isPress = false;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (r.contains(x, y)) {
				isPress = false;
				MySurfaceView.gameState = MySurfaceView.GAME_PLAY;
			}
		}
	}
}
