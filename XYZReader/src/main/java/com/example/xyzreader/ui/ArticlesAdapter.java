package com.example.xyzreader.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;

class ArticlesAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private Cursor mCursor;
    private Context context;
    private ArticleListItemClickListener articleListItemClickListener;

    public ArticlesAdapter(Cursor cursor, Context context, ArticleListItemClickListener articleListItemClickListener) {
        mCursor = cursor;
        this.context = context;
        this.articleListItemClickListener = articleListItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_article, parent, false);
        ImageLoader imageLoader = ImageLoaderHelper.getInstance(context).getImageLoader();
        final ArticleViewHolder viewHolder = new ArticleViewHolder(view, imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long articleId = getItemId(viewHolder.getAdapterPosition());
                articleListItemClickListener.onArticleListItemClick(articleId);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String articleTitle = mCursor.getString(ArticleLoader.Query.TITLE);
        String articleImageUrl = mCursor.getString(ArticleLoader.Query.THUMB_URL);
        float articleImageAspectRatio = mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO);
        String author = mCursor.getString(ArticleLoader.Query.AUTHOR);
        String articleSubtitle = String.format(context.getString(R.string.article_subtitle), author);
        holder.bind(articleTitle, articleSubtitle, articleImageUrl, articleImageAspectRatio);
    }

    @NonNull
    private String date() {
        return DateUtils.getRelativeTimeSpanString(
                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
