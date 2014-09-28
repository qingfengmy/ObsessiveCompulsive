package com.qingfengmy.obsessivecompulsive;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public class BaseActivity extends Activity {

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
