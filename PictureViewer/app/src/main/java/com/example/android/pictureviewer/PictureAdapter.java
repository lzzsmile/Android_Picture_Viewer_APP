package com.example.android.pictureviewer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangzhili on 2018-01-10.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureAdapterViewHolder>{

    private final Context mContext;

    private final ArrayList<Picture> mPictures;

    public PictureAdapter(Context context, ArrayList<Picture> pictures) {
        mContext = context;
        mPictures = pictures;
    }

    @Override
    public PictureAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PictureAdapterViewHolder holder = new PictureAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PictureAdapterViewHolder holder, int position) {
        Picture picture = mPictures.get(position);

        Picasso.with(mContext).load(Uri.parse(picture.getImageUrl())).into(holder.photoView);
        holder.captionView.setText(picture.getCaption());
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    class PictureAdapterViewHolder extends RecyclerView.ViewHolder{

        final ImageView photoView;
        final TextView captionView;

        PictureAdapterViewHolder (View view) {
            super(view);
            photoView = (ImageView) view.findViewById(R.id.picture);
            captionView = (TextView) view.findViewById(R.id.caption);
        }

    }

    public void clear() {
        mPictures.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Picture> pictures) {
        if (pictures.size() == 0) {
            return;
        }

        mPictures.addAll(pictures);
        notifyDataSetChanged();
    }
}
