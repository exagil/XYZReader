package com.example.xyzreader.ui.list;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.ui.detail.ArticleDetailActivity;

import java.util.concurrent.TimeUnit;

import static com.example.xyzreader.data.UpdaterService.ARTICLES_STATUS;
import static com.example.xyzreader.data.UpdaterService.ARTICLES_STATUS_SUCCESS;
import static com.example.xyzreader.data.UpdaterService.ArticlesStatus;
import static com.example.xyzreader.data.UpdaterService.BROADCAST_ACTION_STATE_CHANGE;
import static com.example.xyzreader.data.UpdaterService.EXTRA_REFRESHING;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, ArticleListView, ArticleListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArticleListPresenter articleListPresenter;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_article_list, null);
        setContentView(rootView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_accent, R.color.theme_accent_alternate);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        articleListPresenter = new ArticleListPresenter(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        getLoaderManager().initLoader(0, null, this);
        if (savedInstanceState == null) {
            refresh();
        }
    }

    @Override
    public void showProgressBar() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onPause() {
        Intent updaterServiceIntent = new Intent(ArticleListActivity.this, UpdaterService.class);
        stopService(updaterServiceIntent);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(EXTRA_REFRESHING, false);
                @ArticlesStatus int articlesStatus = intent.getIntExtra(ARTICLES_STATUS, ARTICLES_STATUS_SUCCESS);
                articleListPresenter.onArticlesStateChange(articlesStatus);
            }
        }
    };

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, TimeUnit.MILLISECONDS.toMillis(1500));
    }

    @Override
    public void showArticleDetails(long articleId) {
        startActivity(new Intent(Intent.ACTION_VIEW, ItemsContract.Items.buildItemUri(articleId)));
    }

    @Override
    public void onArticlesUpdateFailed(String errorMessage) {
        final Snackbar snackbar = Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.theme_accent_alternate)).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ArticlesAdapter adapter = new ArticlesAdapter(cursor, this, this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(null);
    }

    @Override
    public void onArticleListItemClick(long articleId) {
        articleListPresenter.onArticleListItemClick(articleId, mIsRefreshing);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        showProgressBar();
        Intent updaterServiceIntent = new Intent(ArticleListActivity.this, UpdaterService.class);
        startService(updaterServiceIntent);
    }
}
