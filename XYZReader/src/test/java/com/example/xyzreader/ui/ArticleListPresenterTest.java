package com.example.xyzreader.ui;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ArticleListPresenterTest {
    @Test
    public void testThatArticleListIsNotClickableWhenProgressBarIsShown() {
        ArticleListView articleListView = mock(ArticleListView.class);
        ArticleListPresenter articleListPresenter = new ArticleListPresenter(articleListView);
        boolean isRefreshing = true;
        articleListPresenter.toggleProgressView(isRefreshing);
        verify(articleListView).showProgressBar();
        verify(articleListView).disableArticleClick();
    }

    @Test
    public void testThatArticleListIsClickableWhenProgressBarIsNotShown() {
        ArticleListView articleListView = mock(ArticleListView.class);
        ArticleListPresenter articleListPresenter = new ArticleListPresenter(articleListView);
        boolean isRefreshing = false;
        articleListPresenter.toggleProgressView(isRefreshing);
        verify(articleListView).hideProgressBar();
        verify(articleListView).enableArticleClick();
    }
}
