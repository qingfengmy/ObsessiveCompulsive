package com.qingfengmy.obsessivecompulsive;

import java.io.File;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengmy.obsessivecompulsive.utils.ImageTools;

public class ViewPagerMultiFragment extends BaseActivity {
	private static int TOTAL_COUNT = 30;

	private RelativeLayout viewPagerContainer;
	private ViewPager viewPager;
	private TextView indexText;
	private int from;
	private Button rightButton;
	private String[] lists;
	private int currentPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpagermultifragment);

		Intent intent = getIntent();
		from = intent.getIntExtra(MainActivity.TO, MainActivity.OTHER);
		if (from == MainActivity.MY) {
			File parent = new File(MainActivity.LOCATION);
			lists = parent.list();
			TOTAL_COUNT = lists.length;
		} else {
			TOTAL_COUNT = 30;
		}

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		indexText = (TextView) findViewById(R.id.view_pager_index);
		viewPagerContainer = (RelativeLayout) findViewById(R.id.pager_layout);
		rightButton = (Button) findViewById(R.id.rightButton);

		if (TOTAL_COUNT != 0) {
			viewPager.setAdapter(new MyPagerAdapter());
			// to cache all page, or we will see the right item delayed
			viewPager.setOffscreenPageLimit(TOTAL_COUNT);
			viewPager.setPageMargin(getResources().getDimensionPixelSize(
					R.dimen.page_margin));
			MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
			viewPager.setOnPageChangeListener(myOnPageChangeListener);

			viewPagerContainer.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// dispatch the events to the ViewPager, to solve the
					// problem
					// that we can swipe only the middle view.
					return viewPager.dispatchTouchEvent(event);
				}
			});

			if (from == MainActivity.MY) {
				rightButton.setText("删除");
			} else {
				rightButton.setText("保存");
			}

			rightButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ProgressDialog dialog = new ProgressDialog(
							ViewPagerMultiFragment.this);
					dialog.show();
					if (from == MainActivity.MY) {
						// 删除
						File file = new File(MainActivity.LOCATION
								+ File.separator + lists[currentPosition]);
						if (file.delete()) {
							File parent = new File(MainActivity.LOCATION);
							lists = parent.list();
							TOTAL_COUNT = lists.length;
							viewPager.setAdapter(new MyPagerAdapter());
							// to cache all page, or we will see the right item
							// delayed
							viewPager.setOffscreenPageLimit(TOTAL_COUNT);
							viewPager
									.setPageMargin(getResources()
											.getDimensionPixelSize(
													R.dimen.page_margin));
							MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
							viewPager
									.setOnPageChangeListener(myOnPageChangeListener);

							Toast.makeText(ViewPagerMultiFragment.this, "删除成功",
									0).show();
						}
					} else {
						// 下载
						Bitmap bm = BitmapFactory.decodeResource(
								getResources(), R.drawable.image1
										+ currentPosition);
						ImageTools.savePhotoToSDCard(
								ViewPagerMultiFragment.this, bm,
								MainActivity.LOCATION,
								System.currentTimeMillis() + "");
					}
					dialog.hide();
				}
			});
			indexText.setText(new StringBuilder().append("1/").append(
					TOTAL_COUNT));
			rightButton.setVisibility(View.VISIBLE);
		} else {
			indexText.setText("空空如也");
			rightButton.setVisibility(View.GONE);
		}

		
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	/**
	 * this is a example fragment, just a imageview, u can replace it with your
	 * needs
	 * 
	 * @author Trinea 2013-04-03
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return TOTAL_COUNT;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(ViewPagerMultiFragment.this);
			if (from == MainActivity.MY) {
				Bitmap bm = BitmapFactory.decodeFile(MainActivity.LOCATION
						+ File.separator + lists[position]);
				imageView.setImageBitmap(bm);
			} else {
				imageView.setImageResource(R.drawable.image1 + position);
			}
			((ViewPager) container).addView(imageView, position);
			return imageView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			indexText.setText(new StringBuilder().append(position + 1)
					.append("/").append(TOTAL_COUNT));
			currentPosition = position;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
