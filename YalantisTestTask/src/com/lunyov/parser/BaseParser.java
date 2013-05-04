package com.lunyov.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BaseParser {

	final String TAG_LOG = "myLog";
	private final String LOG_TAG = getClass().toString();

	// Urls to make request
	public static String url = "http://apitest.yalantis.com/test/";
	public static String url2 = "http://apitest.yalantis.com/test/?id=";

	// JSON Node names
	public static final String TAG_ITEMS = "items";
	public static final String TAG_TITLE = "title";
	public static final String TAG_BODY = "body";
	public static final String TAG_PICTURE = "picture";

	// items JSONArray
	JSONArray items = null;
	BaseAsyncTask Task2 = new BaseAsyncTask();

	// Constructor
	public BaseParser() {
	}

	// Hashmap for ListView
	public ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();

	// Creating HttpUtil instance
	HttpUtil myHttpUtil = new HttpUtil();

	// getting JSON string from URL
	JSONObject json = myHttpUtil.getJSONFromUrl(url);
	{

		try {
			// Getting Array of items
			items = json.getJSONArray(TAG_ITEMS);

			// looping through All items
			for (int i = 0; i < items.length(); i++) {
				JSONObject c = items.getJSONObject(i);

				// Storing each json item in variable
				String title = c.getString(TAG_TITLE);
				String body = c.getString(TAG_BODY);
				String picture = c.getString(TAG_PICTURE);
				Log.d(TAG_LOG, "Title = " + title);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_TITLE, title);
				map.put(TAG_BODY, body);
				map.put(TAG_PICTURE, picture);

				// adding HashList to ArrayList
				itemsList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getting detailed information by clicking on one of the items

	public HashMap<String, String> getDataHashMap(int position) {
		String URL = url2 + position;
		Task2.execute(URL);
		JSONObject res = getJSONObject();
		JSONObject incomingJsonObj = getJSONObjectFromJSONObject(res, "data");

		// Creating new HashMap
		HashMap<String, String> stringMap = new HashMap<String, String>();
		try {
			stringMap.put("title", incomingJsonObj.getString("title"));
			stringMap.put("body", incomingJsonObj.getString("body"));
			stringMap.put("adress", incomingJsonObj.getString("adress"));
			stringMap.put("picture", incomingJsonObj.getString("picture"));
			Log.d(LOG_TAG, "String = " + incomingJsonObj.getString("adress"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stringMap;
	}

	private JSONObject getJSONObject() {
		if (Task2 == null) {
			Log.d(LOG_TAG, "AsyncTask is null");
		}
		JSONObject res = null;
		try {
			res = Task2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return res;
	}

	public JSONObject getJSONObjectFromJSONObject(JSONObject jsonObject,
			String data) {
		if (jsonObject == null) {
			Log.d(LOG_TAG, "ERROR:JSONObject jsonObject is null");
			return null;
		}
		if (data == null) {
			Log.d(LOG_TAG, "ERROR: String data is null");
			return null;
		}
		try {
			jsonObject = jsonObject.getJSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
