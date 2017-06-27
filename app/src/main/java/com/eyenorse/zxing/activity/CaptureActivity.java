package com.eyenorse.zxing.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.dialog.ZxingResultDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.zxing.camera.CameraManager;
import com.eyenorse.zxing.decoding.CaptureActivityHandler;
import com.eyenorse.zxing.decoding.InactivityTimer;
import com.eyenorse.zxing.view.ViewfinderView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import butterknife.OnClick;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private List<String>list = new ArrayList<>();
	private ArrayList<String> numberList;
	private int memberId;
	private String token;
	//private Button cancelScanButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture);
		setTop(R.color.black);

		Intent intent = getIntent();
		numberList = intent.getStringArrayListExtra("numberList");
		memberId = intent.getIntExtra("memberId", 0);
		token = intent.getStringExtra("token");
		list.addAll(numberList);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		//cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			/**
			 * SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
			 * 在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。
			 */
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		//quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		Log.e("start","time::" + (new Date()).getTime());
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		final String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
			//Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			final ZxingResultDialog dialog = new ZxingResultDialog(CaptureActivity.this,"出错啦~");
			dialog.setOnClickChoose(new ZxingResultDialog.OnClickChoose() {
				@Override
				public void onClick(boolean f) {
					if (f){
						dialog.dismiss();

						SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
						SurfaceHolder surfaceHolder = surfaceView.getHolder();
						initCamera(surfaceHolder);
						// 恢复活动监控器
						//inactivityTimer.onActivity();
						if (handler != null){
							handler.restartPreviewAndDecode();
						}
					}
				}
			});
			dialog.show();
		}else {
			if (list.size()>0){
				for (String s:list){
					if (s.toUpperCase().equals(resultString.toUpperCase())){
						final ZxingResultDialog dialog = new ZxingResultDialog(CaptureActivity.this,"重复啦~");
						dialog.setOnClickChoose(new ZxingResultDialog.OnClickChoose() {
							@Override
							public void onClick(boolean f) {
								if (f){
									dialog.dismiss();

									SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
									SurfaceHolder surfaceHolder = surfaceView.getHolder();
									initCamera(surfaceHolder);
									// 恢复活动监控器
									//inactivityTimer.onActivity();
									if (handler != null){
										handler.restartPreviewAndDecode();
									}
								}
							}
						});
						dialog.show();
						return;
					}
				}
			}

			getApiRequestData().getCheckCodes(new IOAuthReturnCallBack() {
				@Override
				public void onSuccess(String result, Gson gson) {
					try {
						String error = new JSONObject(result).getString("error");
						if (TextUtil.isNull(error) || error.length() == 0) {
							JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
							int success = new JSONObject(jsonObject.toString()).getInt("issuccess");
							if (success == 1) {
								final ZxingResultDialog dialog = new ZxingResultDialog(CaptureActivity.this,"扫描成功!");
								dialog.setOnClickChoose(new ZxingResultDialog.OnClickChoose() {
									@Override
									public void onClick(boolean f) {
										if (f){
											list.add(resultString);
											Intent resultIntent = new Intent();
											Bundle bundle = new Bundle();
											bundle.putString("result", resultString);
											//bundle.putParcelable("bitmap", barcode);
											resultIntent.putExtras(bundle);
											CaptureActivity.this.setResult(RESULT_OK, resultIntent);
											dialog.dismiss();

											SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
											SurfaceHolder surfaceHolder = surfaceView.getHolder();
											initCamera(surfaceHolder);
											// 恢复活动监控器
											//inactivityTimer.onActivity();
											if (handler != null){
												handler.restartPreviewAndDecode();
											}
										}
									}
								});
								dialog.show();
							} else {
								Toast.makeText(CaptureActivity.this, "追踪码校验失败", Toast.LENGTH_SHORT).show();
							}

							Log.e("end","time::" + (new Date()).getTime());
						}else {
							Log.e("end","time::" + (new Date()).getTime());
							final ZxingResultDialog dialog = new ZxingResultDialog(CaptureActivity.this,"出错啦~");
							dialog.setOnClickChoose(new ZxingResultDialog.OnClickChoose() {
								@Override
								public void onClick(boolean f) {
									if (f){
										dialog.dismiss();
										SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
										SurfaceHolder surfaceHolder = surfaceView.getHolder();
										initCamera(surfaceHolder);
										// 恢复活动监控器
										//inactivityTimer.onActivity();
										if (handler != null){
											handler.restartPreviewAndDecode();
										}
									}
								}
							});
							dialog.show();
						}
					} catch (JSONException e) {
						Log.e("end","time::" + (new Date()).getTime());
						e.printStackTrace();
					}
				}
			}, resultString, memberId + "", token);

		}
		//CaptureActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};


	@OnClick({R.id.image_back})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_back:
				finish();
				break;
		}
	}
}