package com.zdm.tictactoe.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @author bill 三点连一线只有九宫格时候才有意义，棋盘大了只要先手下中间，有4个方向怎么都堵不住
 */
public class MyView extends View {

	private int screenX = 0, screenY = 0;
	private float x, y, frameX, frameY = 0;
	private Paint paint;
	private final int empty = 0;
	private final int CicleType = 1;
	private final int ForkType = 2;
	private final int rowNum = 7;
	private final int colNum = 5;
	private int count = 0;

	private int bestValue = -1;

	private int[] pieces = new int[rowNum * colNum];

	public MyView(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		paint.setColor(Color.WHITE);
		if (screenX == 0) {
			screenX = getWidth();
			frameX = screenX / colNum;
		}
		if (screenY == 0) {
			screenY = getHeight();
			frameY = screenY / rowNum;
		}

		for (int i = 1; i < colNum; i++) {
			canvas.drawLine(frameX * i, 0, frameX * i, screenY, paint);
		}
		for (int i = 1; i < rowNum; i++) {
			canvas.drawLine(0, frameY * i, screenX, frameY * i, paint);
		}

		paint.setColor(Color.RED);

		for (int i = 0; i < pieces.length; i++) {
			int type = pieces[i];
			// 中心点坐标x0,y0
			float x0 = i % colNum * frameX + frameX / 2;
			float y0 = i / colNum * frameY + frameY / 2;
			float sinR = (float) (frameX / 4 * Math.sin(Math.toRadians(30)));
			float cosR = (float) (frameX / 4 * Math.cos(Math.toRadians(30)));
			if (type == CicleType) {
				canvas.drawCircle(x0, y0, frameX / 4, paint);
			} else if (type == ForkType) {
				// 2条直线组成X，角度暂定60度
				canvas.drawLine(x0 - sinR, y0 - cosR, x0 + sinR, y0 + cosR,
						paint);
				canvas.drawLine(x0 + sinR, y0 - cosR, x0 - sinR, y0 + cosR,
						paint);
			}

		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

			x = event.getX();
			y = event.getY();
			if (count == colNum * rowNum) {
				pieces = new int[rowNum * colNum];
				count = 0;
			}

			int touchNum = (int) (x / frameX) + (int) (y / frameY) * colNum;

			if (pieces[touchNum] == empty) {
				pieces[touchNum] = CicleType;
				count = count + 1;
				int result = checkWin(pieces, CicleType, touchNum);
				if (result == -1) {
					Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT)
							.show();
					count = colNum * rowNum;
					return true;
				}
				if (count == rowNum * colNum) {
					Toast.makeText(getContext(), "平局", Toast.LENGTH_SHORT)
							.show();
					return true;
				}
				count = count + 1;
				if (bestValue >= 0 && bestValue != touchNum) {
					pieces[bestValue] = ForkType;
					if (PcGo(bestValue)) {
						bestValue = -1;
						return true;
					}
				}
				// 有2点在一起的棋子，必须走，不走就输了
				if (result >= 0) {
					pieces[result] = ForkType;
					if (PcGo(result)) {
						return true;
					}
					// 没有2点一起的棋子,优先抢占中心点
				} else {
					int randomI = new Random().nextInt(colNum * rowNum);
					while (!(randomI / colNum > 0 && randomI / colNum < rowNum - 1)
							&& (randomI % colNum > 0 && randomI % colNum < colNum - 1)) {
						randomI = new Random().nextInt(colNum * rowNum);
					}
					if (pieces[randomI] == empty) {
						pieces[randomI] = ForkType;
						if (PcGo(randomI)) {
							return true;
						}
					} else {
						for (int i = 0; i < pieces.length; i++) {
							if (pieces[i] == empty) {
								pieces[i] = ForkType;
								if (PcGo(i)) {
									return true;
								}
								break;
							}
						}
					}
				}
			}

		}
		return true;
	}

	private boolean PcGo(int num) {
		int resultPc = checkWin(pieces, ForkType, num);
		if (resultPc == -1) {
			Toast.makeText(getContext(), "PC Win!", Toast.LENGTH_SHORT).show();
			count = colNum * rowNum;
			return true;
		}

		if (resultPc >= 0) {
			bestValue = resultPc;
		}
		if (count == rowNum * colNum) {
			Toast.makeText(getContext(), "平局", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;

	}

	// 下了某步棋后获胜返回-1
	private int checkWin(int[] ps, int type, int touchNum) {
		String tmp = "";
		for (int n = 0; n < colNum * rowNum; n++) {
			tmp = tmp + n;
		}
		Log.w("9gg", "pieces=" + tmp);
		tmp = "";
		for (int n : pieces) {
			tmp = tmp + n;
		}

		Log.w("9gg", "pieces=" + tmp);
		postInvalidate();
		int result = -2;
		int len = pieces.length;

		// 以touchNum为中点竖线
		if (touchNum - colNum >= 0 && touchNum + colNum < len) {
			if (ps[touchNum - colNum] == type && ps[touchNum + colNum] == type) {
				Log.w("9gg", "su");
				return -1;
			}
			if (ps[touchNum - colNum] == type) {
				if (ps[touchNum + colNum] == empty) {
					result = touchNum + colNum;
				}
			} else if (ps[touchNum + colNum] == type) {
				if (ps[touchNum - colNum] == empty) {
					result = touchNum - colNum;
				}
			}
		}
		// 以touchNum为端点的竖线
		if (touchNum + 2 * colNum < len) {
			if (ps[touchNum + colNum] == type
					&& ps[touchNum + 2 * colNum] == type) {
				Log.w("9gg", "suduan");
				return -1;
			}
			if (ps[touchNum + colNum] == type) {
				if (ps[touchNum + 2 * colNum] == empty) {
					result = touchNum + 2 * colNum;
				}
			} else if (ps[touchNum + 2 * colNum] == type) {
				if (ps[touchNum + colNum] == empty) {
					result = touchNum + colNum;
				}
			}
		}

		if (touchNum - 2 * colNum > -1) {
			if (ps[touchNum - colNum] == type
					&& ps[touchNum - 2 * colNum] == type) {
				Log.w("9gg", "suduan1");
				return -1;
			}

			if (ps[touchNum - colNum] == type) {
				if (ps[touchNum - 2 * colNum] == empty) {
					result = touchNum - 2 * colNum;
				}
			} else if (ps[touchNum - 2 * colNum] == type) {
				if (ps[touchNum - colNum] == empty) {
					result = touchNum - colNum;
				}
			}

		}

		// 左边界
		if (touchNum % colNum == 0) {
			// 横线
			if (ps[touchNum + 1] == type && ps[touchNum + 2] == type) {
				Log.w("9gg", "zuobianheng");
				return -1;
			}

			if (ps[touchNum + 1] == type) {
				if (ps[touchNum + 2] == empty) {
					result = touchNum + 2;
				}
			} else if (ps[touchNum + 2] == type) {
				if (ps[touchNum + 1] == empty) {
					result = touchNum + 1;
				}
			}

			// 右边界
		} else if (touchNum % colNum == colNum - 1) {
			// 横线
			if (ps[touchNum - 1] == type && ps[touchNum - 2] == type) {
				Log.w("9gg", "youbianheng");
				return -1;
			}

			if (ps[touchNum - 1] == type) {
				if (ps[touchNum - 2] == empty) {
					result = touchNum - 2;
				}
			} else if (ps[touchNum - 2] == type) {
				if (ps[touchNum - 1] == empty) {
					result = touchNum - 1;
				}
			}

			// 非边界
		} else {
			// 横线
			if (ps[touchNum - 1] == type && ps[touchNum + 1] == type) {
				Log.w("9gg", "zhongxinheng");
				return -1;
			} else if (ps[touchNum - 1] == type && ps[touchNum + 1] == empty) {
				result = touchNum + 1;
			} else if (ps[touchNum + 1] == type && ps[touchNum - 1] == empty) {
				result = touchNum - 1;
			}

			if (touchNum + 2 < len
					&& touchNum / colNum == (touchNum + 2) / colNum) {
				if (ps[touchNum + 1] == type && ps[touchNum + 2] == type) {
					Log.w("9gg", "heng1");
					return -1;
				}

				if (ps[touchNum + 1] == type) {
					if (ps[touchNum + 2] == empty) {
						result = touchNum + 2;
					}
				} else if (ps[touchNum + 2] == type) {
					if (ps[touchNum + 1] == empty) {
						result = touchNum + 1;
					}
				}
			}

			if (touchNum - 2 > -1
					&& touchNum / colNum == (touchNum - 2) / colNum) {
				if (ps[touchNum - 1] == type && ps[touchNum - 2] == type) {
					Log.w("9gg", "heng2");
					return -1;
				}

				if (ps[touchNum - 1] == type) {
					if (ps[touchNum - 2] == empty) {
						result = touchNum - 2;
					}
				} else if (ps[touchNum - 2] == type) {
					if (ps[touchNum - 1] == empty) {
						result = touchNum - 1;
					}
				}
			}

		}

		// 以touchNum为中心点的斜线
		if (touchNum % colNum != 0 && touchNum % colNum != colNum - 1
				&& touchNum / colNum != 0 && touchNum / colNum != rowNum - 1) {
			if (ps[touchNum - colNum - 1] == type
					&& ps[touchNum + colNum + 1] == type) {
				Log.w("9gg", "zhongxie1");
				return -1;
			} else if (ps[touchNum - colNum - 1] == type
					&& ps[touchNum + colNum + 1] == empty) {
				result = touchNum + colNum + 1;
			} else if (ps[touchNum + colNum + 1] == type
					&& ps[touchNum - colNum - 1] == empty) {
				result = touchNum - colNum - 1;
			}

			if (ps[touchNum - colNum + 1] == type
					&& ps[touchNum + colNum - 1] == type) {
				Log.w("9gg", "zhongxie2");
				return -1;
			} else if (ps[touchNum - colNum + 1] == type
					&& ps[touchNum + colNum - 1] == empty) {
				result = touchNum + colNum - 1;
			} else if (ps[touchNum + colNum - 1] == type
					&& ps[touchNum - colNum + 1] == empty) {
				result = touchNum - colNum + 1;
			}
		}

		// 以为touchNum端点的斜线
		// 右下斜线
		if (touchNum / colNum < rowNum - 2 && touchNum % colNum < colNum - 2
				&& touchNum + 2 * colNum + 2 <= len - 1) {
			if (ps[touchNum + 2 * colNum + 2] == type
					&& ps[touchNum + colNum + 1] == type) {
				Log.w("9gg", "duanxie");
				return -1;
			}
			if (ps[touchNum + 2 * colNum + 2] == type
					&& ps[touchNum + colNum + 1] == empty) {
				result = touchNum + colNum + 1;
			} else if (ps[touchNum + 2 * colNum + 2] == empty
					&& ps[touchNum + colNum + 1] == type) {
				result = touchNum + 2 * colNum + 2;
			}
		}
		// 右上斜线
		if (touchNum / colNum > 1 && touchNum % colNum <= colNum - 3
				&& touchNum - 2 * colNum + 2 > 0) {
			if (ps[touchNum - 2 * colNum + 2] == type
					&& ps[touchNum - colNum + 1] == type) {
				Log.w("9gg", "duanxie1");
				return -1;
			}

			if (ps[touchNum - 2 * colNum + 2] == type
					&& ps[touchNum - colNum + 1] == empty) {
				result = touchNum - colNum + 1;
			} else if (ps[touchNum - 2 * colNum + 2] == empty
					&& ps[touchNum - colNum + 1] == type) {
				result = touchNum - 2 * colNum + 2;
			}

		}
		// 左下斜线
		if (touchNum / colNum < rowNum - 2 && touchNum % colNum > 1
				&& touchNum + 2 * colNum - 2 < len) {
			if (ps[touchNum + 2 * colNum - 2] == type
					&& ps[touchNum + colNum - 1] == type) {
				Log.w("9gg", "duanxie2");
				return -1;
			}

			if (ps[touchNum + 2 * colNum - 2] == type
					&& ps[touchNum + colNum - 1] == empty) {
				result = touchNum + colNum - 1;
			} else if (ps[touchNum + 2 * colNum - 2] == empty
					&& ps[touchNum + colNum - 1] == type) {
				result = touchNum + 2 * colNum - 2;
			}

		}
		// 左上斜线
		if (touchNum / colNum > 1 && touchNum % colNum > 1
				&& touchNum - 2 * colNum - 2 >= 0) {
			if (ps[touchNum - 2 * colNum - 2] == type
					&& ps[touchNum - colNum - 1] == type) {
				Log.w("9gg", "duanxie3");
				return -1;
			}

			if (ps[touchNum - 2 * colNum - 2] == type
					&& ps[touchNum - colNum - 1] == empty) {
				result = touchNum - colNum - 1;
			} else if (ps[touchNum - 2 * colNum - 2] == empty
					&& ps[touchNum - colNum - 1] == type) {
				result = touchNum - 2 * colNum - 2;
			}
		}

		return result;
	}
}
