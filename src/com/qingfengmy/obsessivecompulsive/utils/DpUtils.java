package com.qingfengmy.obsessivecompulsive.utils;

import android.content.Context;

public class DpUtils {

	private static DpUtils utils;
	private Context context;

	private DpUtils(Context _context) {
		this.context = _context;
	}

	public static DpUtils getInstance() {
		if (null == utils) {
			utils = new DpUtils(null);

		}
		return utils;
	}

	public static DpUtils getInstance(Context _context) {
		if (null == utils) {
			utils = new DpUtils(_context);

		}
		return utils;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
