package com.zdm.airplanshot.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
	
	private int playerHp=3;
	private Bitmap bmpPlayerHp;// 主角飞机血量
	private Bitmap bmpPlayer;// 游戏主角飞机
	
	private int x,y;
	private int speed=4;

	public Player(Bitmap bmpPlayerHp, Bitmap bmpPlayer) {
		super();
		this.bmpPlayerHp = bmpPlayerHp;
		this.bmpPlayer = bmpPlayer;
		x=MySurfaceView.screenW/2-bmpPlayer.getWidth()/2;
		y=MySurfaceView.screenH-bmpPlayer.getHeight()-10;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmpPlayer, x, y, paint);
		for(int i=0;i<playerHp;i++){
			canvas.drawBitmap(bmpPlayerHp, 20+(bmpPlayerHp.getWidth()+5)*i, y, paint);	
		}
	}
	
	public void logic(){
		
	}

}
