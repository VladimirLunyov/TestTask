package com.lunyov.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lunyov.activity.BaseActivity;
import com.lunyov.imageloader.ImageLoader;
import com.lunyov.parser.BaseParser;
import com.lunyov.yalantis.R;

public class MyAdapter extends SimpleAdapter{

		private LayoutInflater inflater = null;
		public static ImageLoader imageLoader;
		private Context mContext;

		public MyAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			mContext = context;
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new BaseActivity().getImageLoader();;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null) {
				vi = inflater.inflate(R.layout.list, null);
			}

			HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

			TextView text1 = (TextView) vi.findViewById(R.id.txt_title);
			TextView text2 = (TextView) vi.findViewById(R.id.txt_body);
			ImageView image = (ImageView) vi.findViewById(R.id.img);

			String title = (String) data.get(BaseParser.TAG_TITLE);
			String body = (String) data.get(BaseParser.TAG_BODY);
			text1.setText(title);
			text2.setText(body);
			String imageUrl = (String) data.get(BaseParser.TAG_PICTURE);

			imageLoader.DisplayImage(imageUrl,image);
			return vi;
		}
	}


