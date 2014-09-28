package com.qingfengmy.obsessivecompulsive.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.qingfengmy.obsessivecompulsive.R;
import com.qingfengmy.obsessivecompulsive.utils.Constants;
import com.qingfengmy.obsessivecompulsive.utils.DpUtils;

public class MenuPop extends PopupWindow {
	private LayoutInflater inflater;
	private Context context;
	private View popView, parentView;
	private Fragment fragment;
	private Uri imageUri;

	// 构造函数
	public MenuPop(Context context, Fragment fragment, View parentView) {
		super(context);
		this.context = context;
		this.fragment = fragment;
		this.parentView = parentView;
		inflater = LayoutInflater.from(this.context);
		// 宽
		setWidth(LayoutParams.FILL_PARENT);
		// 高
		setHeight(LayoutParams.WRAP_CONTENT); 
		initPopuWindows();
		setContentView(popView);
	}

	public MenuPop(Context context, Fragment fragment, View parentView,
			Uri imageUri) {
		super(context);
		this.context = context;
		this.fragment = fragment;
		this.parentView = parentView;
		this.imageUri = imageUri;
		inflater = LayoutInflater.from(this.context);
		// 宽
		setWidth(LayoutParams.FILL_PARENT);
		// 高
		setHeight(LayoutParams.WRAP_CONTENT);
		initPopuWindows();
		setContentView(popView);

	}

	public void show() {
		this.showAtLocation(parentView, Gravity.BOTTOM, 0,
				DpUtils.dip2px(context, 50));
	}

	/**
	 * 设置PopupWindows
	 */
	private void initPopuWindows() {
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popView = inflater.inflate(R.layout.user_center_pop_layout, null);
		setFocusable(true);
		// 设置显示和隐藏的动画
		setAnimationStyle(R.style.menushow);
		setOutsideTouchable(true);
		update();
		// 设置触摸获取焦点
		popView.setFocusableInTouchMode(true);

		Button btn1 = (Button) popView.findViewById(R.id.button1);
		Button btn2 = (Button) popView.findViewById(R.id.button2);
		Button btn4 = (Button) popView.findViewById(R.id.button4);
		btn1.setOnClickListener(new OnButtonClickListener());
		btn2.setOnClickListener(new OnButtonClickListener());
		btn4.setOnClickListener(new OnButtonClickListener());

	}

	class OnButtonClickListener implements View.OnClickListener {

		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			// 点击选择图库
			case R.id.button1:
				if (imageUri == null) {
					// 初始化未传入uri
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					fragment.startActivityForResult(intent,
							Constants.GET_PIC_FROM_PHOTO_REQUEST);
				}else{
					// 初始化传入了uri
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 16);
					intent.putExtra("aspectY", 10);
					intent.putExtra("outputX", 550);
					intent.putExtra("outputY", 350);
					intent.putExtra("scale", true);
					intent.putExtra("return-data", false);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
					intent.putExtra("noFaceDetection", false); // no face detection
					fragment.startActivityForResult(intent,
							Constants.GET_PIC_FROM_PHOTO_REQUEST);
				}
				break;
				// 点击选择相机
			case R.id.button2:
				if (imageUri == null) {
					// 上传用户图像小图片使用
					Intent cameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					fragment.startActivityForResult(cameraIntent,
							Constants.GET_PIC_FROM_CAMERA_REQUEST);
				} else {
					// 上传大图片使用
					Intent cameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					fragment.startActivityForResult(cameraIntent,
							Constants.GET_PIC_FROM_CAMERA_REQUEST);
				}
				break;
				// 取消
			case R.id.button4:
				break;
			}
			dismiss();
		}
	}

}