package com.example.xyzreader.ui.list;

public class ArticleListPresenter {

    private ArticleListView articleListView;

    public ArticleListPresenter(ArticleListView articleListView) {
        this.articleListView = articleListView;
    }

    public void toggleProgressView(boolean isRefreshing) {
        if (isRefreshing) {
            articleListView.showProgressBar();
        } else {
            articleListView.hideProgressBar();
        }
    }

    public void onArticleListItemClick(long articleId, boolean isRefreshing) {
        if (!isRefreshing)
            articleListView.showArticleDetails(articleId);
    }
}
