package com.lunyov.activity;

import android.support.v4.app.FragmentActivity;

import com.lunyov.imageloader.ImageLoader;

public class BaseActivity extends FragmentActivity {

	public static ImageLoader imageLoader;

	public ImageLoader getImageLoader() {
		return new ImageLoader(BaseActivity.this);
	}
}
