package com.zdm.tools.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zdm.tools.R;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;

public class MyListAdapter extends BaseAdapter {

	private List<String[]> mData = new ArrayList<String[]>();

	private LayoutInflater mInflater;
	private int listLayout;
	private Context mContext;
	private Editor editor;

	public MyListAdapter(ArrayList<String[]> mData, Context mContext, int listLayout) {
		super();
		this.mData = mData;
		mInflater = LayoutInflater.from(mContext);
		this.listLayout = listLayout;
		this.mContext=mContext;
		SharedPreferences sp = mContext.getSharedPreferences("SP", Context.MODE_PRIVATE);
		this.editor=sp.edit();
	}

	@Override
	public int getCount() {
		Log.w("fl-MyListAdapter", "getCount");
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.w("fl-MyListAdapter", "getItem");
		return null;
	}

	@Override
	public long getItemId(int position) {
		Log.w("fl-MyListAdapter", "getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.w("fl-MyListAdapter", "getView");
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
		
		holder.cBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Toast.makeText(mContext, buttonView.getId()+"=="+isChecked, Toast.LENGTH_SHORT).show();
				if(buttonView.getId()==0){
					if(isChecked){
						editor.putBoolean("playShake", true);						
					}else{
						editor.putBoolean("playShake", false);
					}
					FlashLightSensorListener.getInstance().setPlayShake(isChecked);
				}else if(buttonView.getId()==1){
					if(isChecked){
						editor.putBoolean("playReg", true);
					}else{
						editor.putBoolean("playReg", false);
					}
					FlashLightSensorListener.getInstance().setPlayReg(isChecked);
				}
				editor.commit();
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public TextView title;
		public CheckBox cBox;
	}
}
