package com.fenchtose.paginationmvp.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fenchtose.paginationmvp.model.PaginationFeed;
import com.fenchtose.paginationmvp.presenter.PaginationBasePresenter;
import com.fenchtose.paginationmvp.view.adapter.FooterLoaderAdapter;
import com.fenchtose.paginationmvp.view.adapter.RecyclerViewLinearScrollListener;

import butterknife.ButterKnife;

/**
 * Created by Jay Rambhia on 28/11/15.
 */
public abstract class PaginationBaseFragment<T extends PaginationFeed> extends Fragment implements PaginationBaseView<T> {

    protected PaginationBasePresenter mPresenter;
    protected FooterLoaderAdapter mAdapter;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mErrorView;

    public PaginationBaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        mRecyclerView = getRecyclerView(view);
        mProgressBar = getProgressBar(view);
        mErrorView = getErrorView(view);

        mRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.attachView(getArguments());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.saveInstance(outState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.restoreInstance(savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.removeOnScrollListener(scrollListener);
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
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

    protected abstract @LayoutRes int getLayoutId();
    protected abstract ProgressBar getProgressBar(View parent);
    protected abstract RecyclerView getRecyclerView(View parent);
    protected abstract TextView getErrorView(View parent);

    protected abstract PaginationBasePresenter getPresenter();
    protected abstract FooterLoaderAdapter getAdapter();
    protected abstract LinearLayoutManager getLayoutManager();
}
