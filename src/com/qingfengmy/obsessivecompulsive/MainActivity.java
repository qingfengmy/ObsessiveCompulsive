package com.qingfengmy.obsessivecompulsive;

import java.io.FileNotFoundException;

import net.youmi.android.diy.DiyManager;
import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qingfengmy.obsessivecompulsive.utils.Constants;
import com.qingfengmy.obsessivecompulsive.utils.ImageTools;
import com.readystatesoftware.viewbadger.BadgeView;

public class MainActivity extends BaseActivity implements OnClickListener {

	public static final String TO = "to";
	public static final int MY = 1;
	public static final int OTHER = 2;
	ImageView img;
	ImageView img1;
	ImageView icLauncher,github;
	LinearLayout layout;
	RelativeLayout showPhoto;
	Button create, lookAtMy, lookAtOther, ok, cancle, more;

	private Bitmap bitmap;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;

	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
	Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
	public static final String LOCATION = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/obsessiveCompulsive";

	private BadgeView bv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 广告预加载
		SpotManager.getInstance(this).loadSpotAds();

		img = (ImageView) findViewById(R.id.img);
		img1 = (ImageView) findViewById(R.id.img1);
		layout = (LinearLayout) findViewById(R.id.layout);
		showPhoto = (RelativeLayout) findViewById(R.id.showPhoto);
		icLauncher = (ImageView) findViewById(R.id.icLauncher);
		github = (ImageView) findViewById(R.id.github);

		create = (Button) findViewById(R.id.create);
		lookAtMy = (Button) findViewById(R.id.lookAtMy);
		lookAtOther = (Button) findViewById(R.id.lookAtOther);
		ok = (Button) findViewById(R.id.ok);
		cancle = (Button) findViewById(R.id.cancle);
		more = (Button) findViewById(R.id.more);

		create.setOnClickListener(this);
		lookAtMy.setOnClickListener(this);
		lookAtOther.setOnClickListener(this);
		ok.setOnClickListener(this);
		cancle.setOnClickListener(this);
		more.setOnClickListener(this);
		github.setOnClickListener(this);
		RotateAnimation rotate = new RotateAnimation(0, 360, 0.5f, 0.5f);
		rotate.setDuration(1500);
		rotate.setInterpolator(new AnticipateOvershootInterpolator());
		rotate.setRepeatMode(Animation.REVERSE);
		rotate.setRepeatCount(-1);
		icLauncher.startAnimation(rotate);
	}

	/**
	 * 截取view图片
	 * 
	 * @param context
	 * @return
	 */
	private Bitmap captureScreen(View cv, Activity context) {
		Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		cv.draw(canvas);
		return bmp;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create:
			showPicturePicker(MainActivity.this, true);
			break;
		case R.id.lookAtMy:
			Intent intent = new Intent();
			intent.setClass(this, ViewPagerMultiFragment.class);
			intent.putExtra(TO, MY);
			startActivity(intent);
			break;
		case R.id.lookAtOther:
			intent = new Intent();
			intent.setClass(this, ViewPagerMultiFragment.class);
			intent.putExtra(TO, OTHER);
			startActivity(intent);
			break;
		case R.id.ok:
			// 下载到本地
			long currentTimeMillis = System.currentTimeMillis();
			ImageTools.savePhotoToSDCard(this, bitmap, LOCATION,
					currentTimeMillis + "");
			showPhoto.setVisibility(View.INVISIBLE);
			break;
		case R.id.cancle:
			// 返回
			showPhoto.setVisibility(View.INVISIBLE);
			break;
		case R.id.more:
			DiyManager.showRecommendWall(this); // 展示所有无积分推荐墙
			break;
		case R.id.github:
			intent = new Intent();
			intent.setData(Uri.parse("https://github.com/qingfengmy"));
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent); 
			break;
		default:
			break;
		}
	}

	public void showPicturePicker(Context context, boolean isCrop) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent,
									Constants.GET_PIC_FROM_CAMERA_REQUEST);
							break;
						case CHOOSE_PICTURE:
							intent = new Intent(Intent.ACTION_GET_CONTENT, null);
							intent.setType("image/*");
							intent.putExtra("crop", "true");
							intent.putExtra("aspectX", 1);
							intent.putExtra("aspectY", 1);
							intent.putExtra("outputX", 200);
							intent.putExtra("outputY", 200);
							intent.putExtra("scale", true);
							intent.putExtra("return-data", false);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							intent.putExtra("outputFormat",
									Bitmap.CompressFormat.JPEG.toString());
							intent.putExtra("noFaceDetection", false);
							startActivityForResult(intent,
									Constants.GET_PIC_FROM_PHOTO_REQUEST);
							break;
						default:
							break;
						}
					}
				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.GET_PIC_FROM_PHOTO_REQUEST) {
			createCropImg(data);
		} else if (requestCode == Constants.GET_PIC_FROM_CAMERA_REQUEST) {
			cropImageUri(imageUri, 200, 200);
		} else if (requestCode == Constants.CROP_PIC_REQUEST) {
			createCropImg(data);
		}
	}

	private void cropImageUri(Uri uri, int outputX, int outputY) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, Constants.CROP_PIC_REQUEST);
	}

	private void createCropImg(Intent data) {
		Bitmap photo = decodeUriAsBitmap(imageUri);
		img.setImageBitmap(photo);

		bv = new BadgeView(this, img);
		bv.setText("1");
		bv.setTextColor(Color.WHITE);
		bv.setTextSize(15);
		bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		bv.show();

		layout.setDrawingCacheEnabled(true);

		layout.post(new Runnable() {

			@Override
			public void run() {
				bitmap = captureScreen(layout, MainActivity.this);
				if (bitmap != null) {
					img1.setImageBitmap(bitmap);
					layout.setDrawingCacheEnabled(false);
					showPhoto.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(MainActivity.this, "制作失败，请重新制作", 0).show();
				}

			}
		});

		// 广告
		// if (SpotManager.getInstance(this).checkLoadComplete()) {
		SpotManager.getInstance(this).showSpotAds(this);
		// }
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (showPhoto.getVisibility() != View.VISIBLE) {
				// 退出程序
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
					SpotManager.getInstance(this).showSpotAds(this);
				} else {
					finish();
					System.exit(0);
				}
				return true;
			}
		}
		return true;
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
