package com.sp;

import android.app.Service;
import android.content.Context;
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

/**
 * 
 * @author Himi
 * 
 */

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
	// ����һ��������������
	private SensorManager sm;
	// ����һ��������
	private Sensor sensor;
	// ����һ��������������
	private SensorEventListener mySensorListener;
	// Բ�ε�X,Y����
	private int arc_x, arc_y;
	// ��������xyzֵ
	private float x = 0, y = 0, z = 0;

	/**
	 * SurfaceView��ʼ������
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
		// ��ȡ������������ʵ��
		sm = (SensorManager) MainActivity.instance
				.getSystemService(Service.SENSOR_SERVICE);
		// ʵ��һ������������ʵ��
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// ʵ��������������
		mySensorListener = new SensorEventListener() {
			@Override
			// ��������ȡֵ�����ı�ʱ����Ӧ�˺���
			public void onSensorChanged(SensorEvent event) {
				x = event.values[0];
				// x>0 ˵����ǰ�ֻ��� x<0�ҷ�
				y = event.values[1];
				// y>0 ˵����ǰ�ֻ��·� y<0�Ϸ�
				z = event.values[2];
				// z>0 �ֻ���Ļ���� z<0 �ֻ���Ļ����
				if (x > 1 || x < -1) {
					arc_x -= x;
				}
				if (y > 1 || y < -1) {
					arc_y += y;
				}

				if (arc_x >= screenW - 25) {
					arc_x = screenW - 25;
				} else if (arc_x <= 25) {
					arc_x = 25;
				}
				if (arc_y >= screenH - 25) {
					arc_y = screenH - 25;
				} else if (arc_y <= 25) {
					arc_y = 25;
				}
			}

			@Override
			// �������ľ��ȷ����ı�ʱ��Ӧ�˺���
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		// Ϊ������ע�������
		sm.registerListener(mySensorListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);

	}

	/**
	 * SurfaceView��ͼ��������Ӧ�˺���
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		arc_x = screenW / 2;
		arc_y = screenH / 2;
		flag = true;
		// ʵ���߳�
		th = new Thread(this);
		// �����߳�
		th.start();
	}

	/**
	 * ��Ϸ��ͼ
	 */
	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				paint.setColor(Color.RED);
				// canvas.drawArc(new RectF(arc_x, arc_y, arc_x + 50, arc_y +
				// 50), 0, 270, true, paint);
				canvas.drawCircle(arc_x, arc_y, 25, paint);
				paint.setColor(Color.YELLOW);
				canvas.drawText("��ǰ������������ֵ:", arc_x - 50, arc_y - 30, paint);
				canvas.drawText("x=" + x + ",y=" + y + ",z=" + z, arc_x - 50,
						arc_y, paint);
				String temp_str = "Himi��ʾ�� ";
				String temp_str2 = "";
				String temp_str3 = "";
				if (x < 1 && x > -1 && y < 1 && y > -1) {
					temp_str += "��ǰ�ֻ�����ˮƽ���õ�״̬";
					if (z > 0) {
						temp_str2 += "������Ļ����";
					} else {
						temp_str2 += "������Ļ����,��ʾ���������ֻ������۾�����Ӵ~";
					}
				} else {
					if (x > 1) {
						temp_str2 += "��ǰ�ֻ��������󷭵�״̬";
					} else if (x < -1) {
						temp_str2 += "��ǰ�ֻ��������ҷ���״̬";
					}
					if (y > 1) {
						temp_str2 += "��ǰ�ֻ��������·���״̬";
					} else if (y < -1) {
						temp_str2 += "��ǰ�ֻ��������Ϸ���״̬";
					}
					if (z > 0) {
						temp_str3 += "������Ļ����";
					} else {
						temp_str3 += "������Ļ����,��ʾ���������ֻ������۾�����Ӵ~";
					}
				}
				paint.setTextSize(20);
				canvas.drawText(temp_str, 0, 50, paint);
				canvas.drawText(temp_str2, 0, 80, paint);
				canvas.drawText(temp_str3, 0, 110, paint);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * �����¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	/**
	 * �����¼�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Ϸ�߼�
	 */
	private void logic() {
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

	/**
	 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
