package com.lunyov.parser;

import org.json.JSONObject;

import android.os.AsyncTask;

public class BaseAsyncTask extends AsyncTask<String, Void, JSONObject> {

	@Override
	protected JSONObject doInBackground(String... urls) {
		JSONObject res = null;
		for (String url : urls) {
			res = new HttpUtil().getJSONFromUrl(url);
		}
		return res;
	}
}
