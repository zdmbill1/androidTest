package com.zdm.tools.audio;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.zdm.tools.R;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;

//soundpool用来缓存了播放比较小的音效等，实际上还是调用MediaPlayer---google api说的
//SoundPool.OnLoadCompleteListener.onLoadComplete(SoundPool soundPool, int sampleId, int status)
//的第二个参数sampleId 和 SoundPool.load 返回的参数是同一 个东西。当媒体初始化完成时候调用
//MediaPlayer 播放较大的音乐

public class BackAudioPlay {

	private static BackAudioPlay instance = new BackAudioPlay();

	private SoundPool sp;
	private Map<String, Integer> spMap = new HashMap<String, Integer>();

	private BackAudioPlay() {
		sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		spMap.put("beep", sp.load(FlashLightSensorListener.getInstance()
				.getmContext(), R.raw.beep, 1));
		spMap.put("shake", sp.load(FlashLightSensorListener.getInstance()
				.getmContext(), R.raw.shake, 1));
	}

	public void playBackAudio(String audioId) {
		Log.w("fl-audioPlay", "play "+audioId);
		sp.play(spMap.get(audioId), 1, 1, 1, 0, 1);
	}

	public static BackAudioPlay getInstance() {
		return instance;
	}

	public void destroy() {
		sp.release();
	}
}
