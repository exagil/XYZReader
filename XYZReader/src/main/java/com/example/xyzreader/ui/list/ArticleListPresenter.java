package com.example.xyzreader.ui.list;

import com.example.xyzreader.data.UpdaterService;

import static com.example.xyzreader.data.UpdaterService.ArticlesStatus;

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

    public void onArticlesStateChange(@ArticlesStatus int articlesStatus) {
        switch (articlesStatus) {
            case UpdaterService.ARTICLES_STATUS_UNKNOWN:
                articleListView.onArticlesLoadingStarted();
                break;
        }
    }
}
