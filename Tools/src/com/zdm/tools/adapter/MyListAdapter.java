package com.zdm.tools.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zdm.tools.R;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;

public class MyListAdapter extends BaseAdapter {

	private List<String[]> mData = new ArrayList<String[]>();

	private LayoutInflater mInflater;
	private int listLayout;
//	private Context mContext;

	public MyListAdapter(ArrayList<String[]> mData, Context mContext,
			int listLayout) {
		super();
		this.mData = mData;
		mInflater = LayoutInflater.from(mContext);
		this.listLayout = listLayout;
//		this.mContext = mContext;
	}

	@Override
	public int getCount() {
//		Log.w("fl-MyListAdapter", "getCount");
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
//		Log.w("fl-MyListAdapter", "getItem");
		return null;
	}

	@Override
	public long getItemId(int position) {
//		Log.w("fl-MyListAdapter", "getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(listLayout, null);
			holder.title = (TextView) convertView.findViewById(R.id.textView1);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(mData.get(position)[0]);
		holder.cBox.setChecked(Boolean.parseBoolean(mData.get(position)[1]));
		holder.cBox.setId(position);

		holder.cBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;

				if (cb.getId() == 0) {
					Log.w("fl-MyListAdapter", "setPlayShake=="+cb.isChecked());
					FlashLightSensorListener.getInstance().setPlayShake(
							cb.isChecked());
				} else if (cb.getId() == 1) {
					Log.w("fl-MyListAdapter", "setPlayReg=="+cb.isChecked());
					FlashLightSensorListener.getInstance().setPlayReg(
							cb.isChecked());
				}
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView title;
		public CheckBox cBox;
	}
}
