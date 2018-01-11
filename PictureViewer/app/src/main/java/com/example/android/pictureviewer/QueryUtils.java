package com.example.android.pictureviewer;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangzhili on 2018-01-10.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String PHOTOS_REQUEST_BASE_URL =
            "https://api.500px.com/v1/photos?consumer_key=AJHjroGYgXAl3OabA1SjMw180lakiWUOIyJBIodC" +
                    "&sort=rating" +
                    "&image_size=3";

    private QueryUtils() {}

    public static List<Picture> extractPictures(String Url) {
        URL requestUrl = createUrl(Url);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Picture> pictures = extractDataFromJson(jsonResponse);
        return pictures;
    }
    /*Extract photos information from the JSON response*/
    private static List<Picture> extractDataFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Picture> pictures = new ArrayList<>();
        try {
            JSONObject picInfo = new JSONObject(jsonResponse);
            JSONArray photos = picInfo.getJSONArray("photos");
            for (int i = 0; i < photos.length(); i++) {
                JSONObject photo = photos.getJSONObject(i);
                String name = photo.getString("name");
                String imageUrl = photo.getString("image_url");
                Picture picture = new Picture(imageUrl, name);
                pictures.add(picture);
            }
            return pictures;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the photos JSON result", e);
        }
        return null;
    }

    private static URL createUrl(String Url) {
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }
    /*Make a Http Request and form the JSON response string from obtained input stream*/
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code." + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Picture JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /*Build the URL for to fetch photos from the 500px API*/
    public static URL buildUrl(String page, String category) {
        Uri baseUri = Uri.parse(PHOTOS_REQUEST_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("page", page);
        uriBuilder.appendQueryParameter("only", category);
        URL url = null;
        try {
            url = new URL(uriBuilder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Build Photo URL", e);
        }
        return url;
    }

}
