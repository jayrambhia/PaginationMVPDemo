package com.fenchtose.paginationmvp.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fenchtose.paginationmvp.model.PaginationFeed;
import com.fenchtose.paginationmvp.presenter.PaginationBasePresenter;
import com.fenchtose.paginationmvp.view.adapter.FooterLoaderAdapter;
import com.fenchtose.paginationmvp.view.adapter.RecyclerViewLinearScrollListener;

/**
 * Created by Jay Rambhia on 11/24/2015.
 */
public abstract class PaginationBaseActivity<T extends PaginationFeed> extends AppCompatActivity implements PaginationBaseView<T> {

    protected PaginationBasePresenter mPresenter;
    protected FooterLoaderAdapter mAdapter;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mErrorView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mRecyclerView = getRecyclerView();
        mToolbar = getToolbar();
        mErrorView = getErrorView();
        mProgressBar = getProgressBar();

        mPresenter = getPresenter();
        mPresenter.attachView(savedInstanceState);

        setToolbarTitle(getToolbarTitle());
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter = getAdapter();
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(getLayoutManager());

        mRecyclerView.addOnScrollListener(scrollListener);
        loadData();

        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        if (mPresenter != null) {
            mPresenter.loadData();
        }
    }

    @Override
    public void reloadData() {
        if (mPresenter != null) {
            mPresenter.reloadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setData(T t) {
        mErrorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        if (mAdapter != null) {
            mAdapter.setItems(t.getItems());
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void showLoading(boolean status) {
        if (mAdapter != null) {

            mAdapter.showLoading(status);
            int count = mAdapter.getItemCount();
            if (count != 0) {
                mAdapter.notifyItemChanged(count - 1);
            }
        }
    }

    @Override
    public void showError(CharSequence text) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setText(text);
    }

    @Override
    public void notifyDataCleared() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    protected RecyclerViewLinearScrollListener scrollListener = new RecyclerViewLinearScrollListener() {
        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onLoadMore() {
            if (mPresenter != null) {
                mPresenter.loadMoreData();
            }
        }
    };

    public void setToolbarTitle(CharSequence text) {
        mToolbar.setTitle(text);
    }

    protected abstract @LayoutRes int getLayoutId();
    protected abstract RecyclerView getRecyclerView();
    protected abstract ProgressBar getProgressBar();
    protected abstract TextView getErrorView();
    protected abstract Toolbar getToolbar();

    protected abstract PaginationBasePresenter getPresenter();
    protected abstract CharSequence getToolbarTitle();
    protected abstract FooterLoaderAdapter getAdapter();
    protected abstract LinearLayoutManager getLayoutManager();
}
