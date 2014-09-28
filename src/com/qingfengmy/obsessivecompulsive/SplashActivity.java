package com.qingfengmy.obsessivecompulsive;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends BaseActivity {

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 初始化
		AdManager.getInstance(this).init("c403757875e8d42f",
				"89e4612c615eb043", false);

		dialog = new ProgressDialog(this);
		dialog.setMessage("玩命加载中，请稍候。。。");
		dialog.show();
		
		SpotManager.getInstance(this).showSpotAds(this,
				new SpotDialogListener() {
					@Override
					public void onShowSuccess() {
						dialog.dismiss();
					}

					@Override
					public void onShowFailed() {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onSpotClosed() {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				});

	}

	@Override
	public void onBackPressed() {
		// 如果有需要，可以点击后退关闭插屏广告（可选）。
		if (!SpotManager.getInstance(this).disMiss(true)) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).disMiss(false);

		super.onStop();
	}
}
