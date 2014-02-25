package com.zdm.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * @author bill
 *	参考系统短信源代码学习copy过来的。。。
 */
public class NumberPickerDialog extends AlertDialog implements
		DialogInterface.OnClickListener {
	private final OnNumberSetListener mCallback;
	private final NumberPicker mNumberPicker;

	public NumberPickerDialog(Context paramContext, int theme,
			OnNumberSetListener paramOnNumberSetListener, int currentInt,
			int minInt, int maxInt, String title) {
		super(paramContext, theme);
		this.mCallback = paramOnNumberSetListener;
		setTitle(title);
		setButton(-1, "设置", this);
		setButton(-2, "取消", (DialogInterface.OnClickListener) null);
		View localView = ((LayoutInflater) paramContext
				.getSystemService("layout_inflater")).inflate(
				R.layout.number_picker_dialog, null);
		setView(localView);
		this.mNumberPicker = ((NumberPicker) localView
				.findViewById(R.id.number_picker));
		this.mNumberPicker.setMinValue(minInt);
		this.mNumberPicker.setMaxValue(maxInt);
		this.mNumberPicker.setValue(currentInt);
		this.mNumberPicker.setOnLongPressUpdateInterval(100L);
		this.mNumberPicker.setWrapSelectorWheel(false);
		this.setCancelable(false);
	}

	public NumberPickerDialog(Context paramContext,
			OnNumberSetListener paramOnNumberSetListener, int currentInt,
			int minInt, int maxInt, String title) {
		this(paramContext, AlertDialog.THEME_HOLO_LIGHT,
				paramOnNumberSetListener, currentInt, minInt, maxInt, title);
	}

	public void onClick(DialogInterface paramDialogInterface, int paramInt) {
		if (this.mCallback != null) {
			this.mNumberPicker.clearFocus();
			this.mCallback.onNumberSet(this.mNumberPicker.getValue());
			paramDialogInterface.dismiss();
		}
	}

	public void onRestoreInstanceState(Bundle paramBundle) {
		super.onRestoreInstanceState(paramBundle);
		int i = paramBundle.getInt("number");
		this.mNumberPicker.setValue(i);
	}

	public Bundle onSaveInstanceState() {
		Bundle localBundle = super.onSaveInstanceState();
		localBundle.putInt("number", this.mNumberPicker.getValue());
		return localBundle;
	}

	public static abstract interface OnNumberSetListener {
		public abstract void onNumberSet(int currentInt);
	}
}
