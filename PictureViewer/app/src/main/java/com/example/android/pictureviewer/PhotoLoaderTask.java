package com.example.android.pictureviewer;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.List;

/**
 * Created by zhuangzhili on 2018-01-11.
 */

public class PhotoLoaderTask extends AsyncTask<String, Void, List<Picture>> {

    private static final String LOG_TAG = PhotoLoaderTask.class.getSimpleName();

    private PictureAdapter adapter;

    public PhotoLoaderTask(PictureAdapter picAdapter) {
        adapter = picAdapter;
    }

    @Override
    protected List<Picture> doInBackground(String... strings) {
        String combine = strings[0];
        String[] combines = combine.split(" ");
        URL photoRequestUrl = QueryUtils.buildUrl(combines[0], combines[1]);
        return QueryUtils.extractPictures(photoRequestUrl.toString());
    }

    @Override
    protected void onPostExecute(List<Picture> pictures) {
        if (pictures != null) {
            adapter.addAll(pictures);
        }
    }
}
