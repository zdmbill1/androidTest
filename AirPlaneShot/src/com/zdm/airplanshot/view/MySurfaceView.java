package com.zdm.airplanshot.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
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

	public final int ENEMY_DUCK = 0;
	public final int ENEMY_FLY = 1;

	// 新增根据动力感应移动
	// 声明一个传感器管理器
	private SensorManager sm;
	// 声明一个传感器
	private Sensor sensor;
	// 声明一个传感器监听器
	private SensorEventListener mySensorListener;
	private float sensorX = 0, sensorY = 0;

	private int enemyArray[][] = {
			{ ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK,
					ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_FLY, ENEMY_DUCK, ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK },
			{ ENEMY_FLY, ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK, ENEMY_DUCK,
					ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK,
					ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_DUCK, ENEMY_FLY, ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_DUCK, ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY,
					ENEMY_FLY, },
			{ ENEMY_DUCK, ENEMY_DUCK, ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_DUCK, ENEMY_FLY, ENEMY_DUCK },
			{ ENEMY_DUCK, ENEMY_FLY, ENEMY_DUCK, ENEMY_DUCK },
			{ ENEMY_FLY, ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK, ENEMY_DUCK,
					ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY, },
			{ ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK, ENEMY_DUCK, ENEMY_FLY,
					ENEMY_FLY, ENEMY_FLY, ENEMY_DUCK, ENEMY_FLY, ENEMY_FLY,
					ENEMY_FLY, ENEMY_FLY }, };
	private HashSet<Enemy> enemys = new HashSet<Enemy>();

	private int enemyArrayIndex = 0;
	// 计数器，每次thread循环+1
	private int count = 0;

	/** 敌机子弹 */
	private HashSet<Bullet> bEnemys = new HashSet<Bullet>();
	private int countBulletEnemy = 0;
	private HashSet<Bullet> bPlayers = new HashSet<Bullet>();
	private int countBulletPlayer = 0;

	private HashSet<Boom> booms = new HashSet<Boom>();

	private EnemyBoss enBoss;
	public static long startTime = 0;

	public MySurfaceView(Context context) {
		super(context);
		sh = getHolder();
		// 添加监听
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setColor(Color.WHITE);
		// 设置无锯齿
		paint.setAntiAlias(true);

		// 确保我们的View能获得输入焦点
		setFocusable(true);
		// 确保能接收到触屏事件
		setFocusableInTouchMode(true);
		// 设置背景常亮
		setKeepScreenOn(true);

		// 获取传感器管理类实例
		sm = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = getWidth();
		screenH = getHeight();
		initGame();
		flag = true;
		new Thread(this).start();
		setKeepScreenOn(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (gameState) {
		case GAME_MENU:
			gameMenu.onTouchEvent(event);
			break;
		case GAME_PLAY:
			player.onTouchEvent(event);
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

		enBoss = null;

		bmpGameWin = Bitmap.createScaledBitmap(bmpGameWin,
				MySurfaceView.screenW, MySurfaceView.screenH, true);
		bmpGameLost = Bitmap.createScaledBitmap(bmpGameLost,
				MySurfaceView.screenW, MySurfaceView.screenH, true);

		enemyArrayIndex = 0;
		count = 0;
		countBulletEnemy = 0;
		countBulletPlayer = 0;

		enemys = new HashSet<Enemy>();
		bEnemys = new HashSet<Bullet>();
		bPlayers = new HashSet<Bullet>();
		booms = new HashSet<Boom>();

		// 实例一个重力传感器实例
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 实例传感器监听器
		mySensorListener = new SensorEventListener() {
			@Override
			// 传感器获取值发生改变时在响应此函数
			public void onSensorChanged(SensorEvent event) {
				if (!player.isMoveByTouch) {
					sensorX = event.values[0];
					// x>0 说明当前手机左翻 x<0右翻
					sensorY = event.values[1];
					// y>0 说明当前手机下翻 y<0上翻
					// z = event.values[2];
					// z>0 手机屏幕朝上 z<0 手机屏幕朝下
					if (sensorX > 1 || sensorX < -1) {
						player.x -= sensorX;
					}
					if (sensorY > 1 || sensorY < -1) {
						player.y += sensorY;
					}

					if (player.x >= screenW - bmpPlayer.getWidth()) {
						player.x = screenW - bmpPlayer.getWidth();
					} else if (player.x <= 0) {
						player.x = 0;
					}
					if (player.y >= screenH - bmpPlayer.getHeight()) {
						player.y = screenH - bmpPlayer.getHeight();
					} else if (player.y <= 0) {
						player.y = 0;
					}
				}
			}

			@Override
			// 传感器的精度发生改变时响应此函数
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		// 为传感器注册监听器
		sm.registerListener(mySensorListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			count++;
			countBulletEnemy++;
			countBulletPlayer++;
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
				if (enBoss != null) {
					enBoss.draw(canvas, paint);

					canvas.drawText("bossHp=" + enBoss.getBossHp(), 50, 50,
							paint);
				}
				for (Enemy e : enemys) {
					e.draw(canvas, paint);
				}
				for (Bullet b : bEnemys) {
					b.draw(canvas, paint);
				}
				for (Bullet b : bPlayers) {
					b.draw(canvas, paint);
				}
				for (Boom b : booms) {
					b.draw(canvas, paint);
				}

				break;
			case END_GOOD:
				canvas.drawBitmap(bmpGameWin, 0, 0, paint);
				break;
			case END_BAD:
				canvas.drawBitmap(bmpGameLost, 0, 0, paint);
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

			Iterator<Boom> itBoom = booms.iterator();
			while (itBoom.hasNext()) {
				Boom boom = (Boom) itBoom.next();
				boom.logic();
				if (boom.ended) {
					itBoom.remove();
				}
			}

			Iterator<Enemy> it = enemys.iterator();
			while (it.hasNext()) {
				Enemy en = (Enemy) it.next();
				if (player.isCollision(en)) {
					player.setPlayerHp(en);
					if (player.isDead) {
						gameState = END_BAD;
						return;
					}
				}
				// 判断子弹是否击中敌机
				Iterator<Bullet> itBp = bPlayers.iterator();
				while (itBp.hasNext()) {
					Bullet bullet = (Bullet) itBp.next();
					if (en.isCollision(bullet)) {
						itBp.remove();
					}
				}
				if (en.isDead) {
					it.remove();
					Boom b = new Boom(bmpBoom, en.x + en.frameW / 2, en.y
							+ en.frameH / 2);
					booms.add(b);
				} else {
					en.logic();
				}

			}

			Iterator<Bullet> itB = bEnemys.iterator();
			while (itB.hasNext()) {
				Bullet bullet = (Bullet) itB.next();
				if (player.isCollision(bullet)) {
					player.setPlayerHp(bullet);
					if (player.isDead) {
						gameState = END_BAD;
						return;
					}
				}
				bullet.logic();
				if (bullet.isDead) {
					itB.remove();
				}
			}

			Iterator<Bullet> itBp = bPlayers.iterator();
			while (itBp.hasNext()) {
				Bullet bullet = (Bullet) itBp.next();
				bullet.logic();
				if (bullet.isDead) {
					itBp.remove();
					// 判断是否打中boss
				} else if (enBoss != null && enBoss.isCollision(bullet)) {
					itBp.remove();
					// 暂定攻击力为1
					enBoss.setBossHp(1);
					// 击中就爆炸次
					if (enBoss.isRight) {
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 25
								+ enBoss.speed * 5, enBoss.y + 30, 5));
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 35
								+ enBoss.speed * 5, enBoss.y + 40, 5));
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 45
								+ enBoss.speed * 5, enBoss.y + 50, 5));
					} else {
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 25,
								enBoss.y + 30, 5));
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 35,
								enBoss.y + 40, 5));
						booms.add(new Boom(bmpBoosBoom, enBoss.x + 45,
								enBoss.y + 50, 5));
					}

					if (enBoss.isDead) {
						gameState = END_GOOD;
					}
				}
			}
			// 敌人每2秒发射个新子弹
			if (countBulletEnemy % 40 == 0) {
				countBulletEnemy = 0;
				for (Enemy e : enemys) {
					bEnemys.add(new BulletEnemy(bmpEnemyBullet, e.x + e.frameW
							/ 2, e.y + e.frameH));
				}

			}
			// 自己每1秒发射个子弹
			if (countBulletPlayer % 20 == 0) {
				countBulletPlayer = 0;
				bPlayers.add(new BulletPlayer(bmpBullet, player.x
						+ player.bmpPlayer.getWidth() / 2, player.y
						- player.bmpPlayer.getHeight()));

				if (enBoss != null) {
					// boss刚开始普通子弹
					if (!enBoss.crazy) {
						bEnemys.add(new BulletEnemy(bmpBossBullet, enBoss.x
								+ enBoss.frameW / 2, enBoss.y + enBoss.frameH));
					} else {
						bEnemys.addAll(enBoss.addBullets());
					}
				}

			}

			player.logic();
			// 每次刷新间隔时间=50*100ms
			if (count % 100 == 0) {
				count = 0;
				for (int enemyType : enemyArray[enemyArrayIndex]) {
					Random r = new Random();
					switch (enemyType) {
					case ENEMY_DUCK:

						enemys.add(new EnemyDuck(bmpEnemyDuck, r
								.nextInt(screenW), r.nextInt(50)));
						break;
					case ENEMY_FLY:
						enemys.add(new EnemyFly(bmpEnemyFly,
								r.nextInt(screenW), r.nextInt(50)));
						break;
					default:
						break;
					}
				}

				if (enemyArrayIndex == enemyArray.length - 1) {
					enemyArrayIndex = 0;
				} else {
					enemyArrayIndex++;
				}
			}

			if (enBoss == null) {
				// 开始游戏大于5秒刷boss
				if (System.currentTimeMillis() - startTime >= 5000) {
					enBoss = new EnemyBoss(bmpEnemyBoos, screenW / 2
							- bmpEnemyBoos.getWidth() / (2 * 10), 200);
				}
			} else {
				enBoss.logic();
				if (player.isCollision(enBoss)) {
					player.setPlayerHp(enBoss);
					if (player.isDead) {
						gameState = END_BAD;
						return;
					}
				}
			}

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (gameState == GAME_PLAY || gameState == END_BAD
					|| gameState == END_GOOD) {
				gameState = GAME_MENU;
				initGame();

			} else if (gameState == GAME_MENU) {
				System.exit(0);
			}
		}
		return true;
	}

}
