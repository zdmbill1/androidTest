package com.zdm.airplanshot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.zdm.airplanshot.R;

public class MySurfaceView extends SurfaceView implements Runnable, Callback {

	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;
	private boolean flag;

	// 声明游戏需要用到的图片资源(图片声明)
	private Bitmap bmpBackGround;// 游戏背景
	private Bitmap bmpBoom;// 爆炸效果
	private Bitmap bmpBoosBoom;// Boos爆炸效果
	private Bitmap bmpButton;// 游戏开始按钮
	private Bitmap bmpButtonPress;// 游戏开始按钮被点击
	private Bitmap bmpEnemyDuck;// 怪物鸭子
	private Bitmap bmpEnemyFly;// 怪物苍蝇
	private Bitmap bmpEnemyBoos;// 怪物猪头Boos
	private Bitmap bmpGameWin;// 游戏胜利背景
	private Bitmap bmpGameLost;// 游戏失败背景
	private Bitmap bmpPlayer;// 游戏主角飞机
	private Bitmap bmpPlayerHp;// 主角飞机血量
	private Bitmap bmpMenu;// 菜单背景
	public static Bitmap bmpBullet;// 子弹
	public static Bitmap bmpEnemyBullet;// 敌机子弹
	public static Bitmap bmpBossBullet;// Boss子弹

	public static int screenW = 0, screenH = 0;

	private GameMenu gameMenu;

	private static final int GAME_MENU = 0;
	public static int gameState = GAME_MENU;
	public static final int GAME_PLAY = 1;
	public static final int END_GOOD = 2;
	public static final int END_BAD = 3;

	private GameBackGround gameBg;
	private Player player;

	public MySurfaceView(Context context) {
		super(context);
		sh = getHolder();
		// 添加监听
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		// 设置无锯齿
		paint.setAntiAlias(true);

		// 确保我们的View能获得输入焦点
		setFocusable(true);
		// 确保能接收到触屏事件
		setFocusableInTouchMode(true);
		// 设置背景常亮
		setKeepScreenOn(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = getWidth();
		screenH = getHeight();
		initGame();
		flag = true;
		new Thread(this).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (gameState) {
		case GAME_MENU:
			gameMenu.onTouchEvent(event);
			break;

		default:
			break;
		}

		return true;
	}

	public void initGame() {
		// 加载游戏资源
		bmpBackGround = BitmapFactory.decodeResource(getResources(),
				R.drawable.background);
		bmpBoom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
		bmpBoosBoom = BitmapFactory.decodeResource(getResources(),
				R.drawable.boos_boom);
		bmpButton = BitmapFactory.decodeResource(getResources(),
				R.drawable.button);
		bmpButtonPress = BitmapFactory.decodeResource(getResources(),
				R.drawable.button_press);
		bmpEnemyDuck = BitmapFactory.decodeResource(getResources(),
				R.drawable.enemy_duck);
		bmpEnemyFly = BitmapFactory.decodeResource(getResources(),
				R.drawable.enemy_fly);
		bmpEnemyBoos = BitmapFactory.decodeResource(getResources(),
				R.drawable.enemy_pig);
		bmpGameWin = BitmapFactory.decodeResource(getResources(),
				R.drawable.gamewin);
		bmpGameLost = BitmapFactory.decodeResource(getResources(),
				R.drawable.gamelost);
		bmpPlayer = BitmapFactory.decodeResource(getResources(),
				R.drawable.player);
		bmpPlayerHp = BitmapFactory.decodeResource(getResources(),
				R.drawable.hp);
		bmpMenu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
		bmpBullet = BitmapFactory.decodeResource(getResources(),
				R.drawable.bullet);
		bmpEnemyBullet = BitmapFactory.decodeResource(getResources(),
				R.drawable.bullet_enemy);
		bmpBossBullet = BitmapFactory.decodeResource(getResources(),
				R.drawable.boosbullet);

		gameMenu = new GameMenu(bmpMenu, bmpButton, bmpButtonPress);
		gameBg = new GameBackGround(bmpBackGround);
		player = new Player(bmpPlayerHp, bmpPlayer);
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void myDraw() {
		try {
			canvas = sh.lockCanvas();

			switch (gameState) {
			case GAME_MENU:
				gameMenu.draw(canvas, paint);
				break;
			case GAME_PLAY:
				gameBg.draw(canvas, paint);
				player.draw(canvas, paint);
				break;
			default:
				break;
			}

		} catch (Exception e) {
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);
			}
		}
	}

	private void logic() {

		switch (gameState) {
		case GAME_PLAY:
			gameBg.logic();
			break;

		default:
			break;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

}
