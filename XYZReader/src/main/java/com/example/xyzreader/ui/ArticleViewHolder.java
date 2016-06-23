package com.example.xyzreader.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public DynamicHeightNetworkImageView thumbnailView;
    public TextView titleView;
    public TextView subtitleView;
    private ImageLoader imageLoader;

    public ArticleViewHolder(View view, ImageLoader imageLoader) {
        super(view);
        thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
        titleView = (TextView) view.findViewById(R.id.article_title);
        subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        this.imageLoader = imageLoader;
    }

    public void bind(String articleTitle, String articleSubtitle, String articleImageUrl, float articleImageAspectRatio) {
        titleView.setText(articleTitle);
        subtitleView.setText(articleSubtitle);
        thumbnailView.setImageUrl(articleImageUrl, imageLoader);
        thumbnailView.setAspectRatio(articleImageAspectRatio);
    }
}
