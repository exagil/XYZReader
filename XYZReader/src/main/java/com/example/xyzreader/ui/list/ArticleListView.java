package com.example.xyzreader.ui.list;

public interface ArticleListView {
    void showProgressBar();

    void hideProgressBar();

    void showArticleDetails(long articleId);

    void onArticlesUpdateFailed(String errorMessage);
}
