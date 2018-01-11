package com.example.android.pictureviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

/**
 * Created by zhuangzhili on 2018-01-11.
 */

public class PictureLoader {



    private PullToLoadView mPullToLoadView;

    private Context mContext;

    private PictureAdapter mPictureAdapter;

    private SharedPreferences mSharedPreferences;

    private boolean isLoading = false;

    private boolean hasLoadedAll = false;

    private int nextPageNumber;

    public PictureLoader(Context context, PullToLoadView pullToLoadView) {
        mContext = context;
        mPullToLoadView = pullToLoadView;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        /*Obtain the RecyclerView and set the layout manager*/
        RecyclerView mRecyclerView = mPullToLoadView.getRecyclerView();
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, getSpan());
        //LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);

        mPictureAdapter = new PictureAdapter(mContext, new ArrayList<Picture>());
        mRecyclerView.setAdapter(mPictureAdapter);

    }
    /*Determine the grid layout style based on landscape of mobile device*/
    private int getSpan() {
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 4;
        }
        return 2;
    }
    /*Initialize the PullToLoadView*/
    public void initializePictureLoader() {
        mPullToLoadView.isLoadMoreEnabled(true);
        mPullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                loadPictures(nextPageNumber);
                Toast.makeText(mContext, "Page " + String.valueOf(nextPageNumber), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefresh() {
                mPictureAdapter.clear();
                hasLoadedAll = false;
                nextPageNumber = 1;
                loadPictures(nextPageNumber);
                Toast.makeText(mContext, "Page " + String.valueOf(nextPageNumber), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });
        mPullToLoadView.initLoad();
    }

    private void loadPictures(final int page) {
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String category = mSharedPreferences.getString(mContext.getString(R.string.category_key), mContext.getString(R.string.nature_category));
                String toTask = String.valueOf(nextPageNumber) + " " + category;
                PhotoLoaderTask loaderTask = new PhotoLoaderTask(mPictureAdapter);
                loaderTask.execute(toTask);
                mPullToLoadView.setComplete();
                isLoading = false;
                nextPageNumber = page + 1;
            }
        }, 2000);


    }

}
