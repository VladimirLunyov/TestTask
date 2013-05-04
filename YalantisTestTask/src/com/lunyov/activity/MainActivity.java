package com.lunyov.activity;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.lunyov.adapter.MyAdapter;
import com.lunyov.imageloader.ImageLoader;
import com.lunyov.parser.BaseAsyncTask;
import com.lunyov.parser.BaseParser;
import com.lunyov.sidemenu.SideMenuListener;
import com.lunyov.yalantis.R;

public class MainActivity extends BaseActivity implements SideMenuListener {

	final String TAG_LOG = "myLog";
	ImageLoader imageLoader = new BaseActivity().getImageLoader();

	private FragmentTransaction fragmentTransaction;
	private View content;
	private int contentID = R.id.content;

	private final double RIGTH_BOUND_COFF = 0.75;
	private static int DURATION = 250;
	private boolean isContentShow = true;
	private int rightBound;
	private ContentScrollController menuController;
	private Rect contentHitRect = new Rect();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Fragment TestFragment = getSupportFragmentManager()
				.findFragmentById(contentID);
		if (!isNetworkAvailable()) {
			getToast("Sorry, no internet connection");
			TestFragment.getView().findViewById(R.id.toggle).setEnabled(false);
			return;
		}

		content = findViewById(contentID);
		menuController = new ContentScrollController(new Scroller(
				getApplicationContext(), new DecelerateInterpolator(3)));
		StrictMode.enableDefaults();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		rightBound = (int) (displaymetrics.widthPixels * RIGTH_BOUND_COFF);

		content.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				v.getHitRect(contentHitRect);
				contentHitRect.offset(-v.getScrollX(), v.getScrollY());
				if (contentHitRect.contains((int) event.getX(),
						(int) event.getY()))
					return true;
				return v.onTouchEvent(event);
			}
		});

		BaseAsyncTask Task1 = new BaseAsyncTask();

		Task1.execute(BaseParser.url);
		ListView listView = (ListView) findViewById(R.id.list);

		/**
		 * Updating parsed JSON data into ListView
		 * */
		final MyAdapter sAdapter = new MyAdapter(MainActivity.this,
				new BaseParser().itemsList, R.layout.list, new String[] {
						BaseParser.TAG_TITLE, BaseParser.TAG_BODY,
						BaseParser.TAG_PICTURE }, new int[] { R.id.txt_title,
						R.id.txt_body, R.id.img });

		listView.setAdapter(sAdapter);

		// Launching new screen on Selecting Single ListItem
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toggleMenu();

				TextView TitleItem = (TextView) TestFragment.getView()
						.findViewById(R.id.txtTitle);
				TextView BodyItem = (TextView) TestFragment.getView()
						.findViewById(R.id.txtBody);
				TextView AddressItem = (TextView) TestFragment.getView()
						.findViewById(R.id.txtAddress);
				ImageView image = (ImageView) TestFragment.getView()
						.findViewById(R.id.imgPicture);

				TitleItem.setVisibility(View.VISIBLE);
				BodyItem.setVisibility(View.VISIBLE);
				AddressItem.setVisibility(View.VISIBLE);
				 image.setVisibility(View.VISIBLE);

				HashMap<String, String> stringMap = new BaseParser()
						.getDataHashMap(position + 1);
				TitleItem.setText("Title : " + stringMap.get("title"));
				BodyItem.setText("Body : " + stringMap.get("body"));
				AddressItem.setText("Address : " + stringMap.get("adress"));

				Log.d(TAG_LOG, "Title= " + stringMap.get("title"));
				Log.d(TAG_LOG, "Body= " + stringMap.get("body"));
				Log.d(TAG_LOG, "Address= " + stringMap.get("adress"));
				Log.d(TAG_LOG, "Picture= " + stringMap.get("picture"));

				String URL = stringMap.get("picture");
				imageLoader.DisplayImage(URL, image);

			}
		});
	}

	public void getToast(Object object) {

		Toast.makeText(this, "Message : " + object, Toast.LENGTH_LONG).show();
	}

	@Override
	public void startFragment(Fragment fragment) {
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(contentID, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public boolean toggleMenu() {
		if (isContentShow)
			menuController.openMenu(DURATION);
		else
			menuController.closeMenu(DURATION);
		return isContentShow;
	}

	// Check internet connection
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private class ContentScrollController implements Runnable {
		private final Scroller scroller;
		private int lastX = 0;

		public ContentScrollController(Scroller scroller) {
			this.scroller = scroller;
		}

		public void run() {
			if (scroller.isFinished())
				return;

			final boolean more = scroller.computeScrollOffset();
			final int x = scroller.getCurrX();
			final int diff = lastX - x;

			if (diff != 0) {
				content.scrollBy(diff, 0);
				lastX = x;
			}
			if (more)
				content.post(this);
		}

		public void openMenu(int duration) {
			isContentShow = false;
			final int startX = content.getScrollX();
			final int dx = rightBound + startX;
			fling(startX, dx, duration);
		}

		public void closeMenu(int duration) {
			isContentShow = true;
			final int startX = content.getScrollX();
			final int dx = startX;
			fling(startX, dx, duration);
		}

		private void fling(int startX, int dx, int duration) {
			if (!scroller.isFinished())
				scroller.forceFinished(true);
			if (dx == 0)
				return;
			if (duration <= 0) {
				content.scrollBy(-dx, 0);
				return;
			}
			scroller.startScroll(startX, 0, dx, 0, duration);
			lastX = startX;
			content.post(this);
		}
	}
}
