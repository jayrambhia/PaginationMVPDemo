package com.fenchtose.paginationmvp.presenter;

import android.os.Bundle;


import com.fenchtose.paginationmvp.model.PaginationFeed;
import com.fenchtose.paginationmvp.view.PaginationBaseView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jay Rambhia on 11/24/2015.
 */
public abstract class PaginationBasePresenterImpl<T extends PaginationFeed, V extends PaginationBaseView> implements PaginationBasePresenter<T, V> {

    protected V mPaginationView;
    protected T mFeed;

    private boolean isDataBeingLoaded = false;
    private boolean isDataLoaded = false;
    private boolean loadMoreEnabled = true;
    private boolean moreDataAvailable = false;

    private static final String BUNDLE_KEY_IS_DATA_LOADED = "is_data_loaded";
    private static final String BUNDLE_LOADE_MORE_ENABLED = "load_more_enabled";

    public PaginationBasePresenterImpl(V view) {
        mPaginationView = view;
    }

    @Override
    public void loadData() {
        if (isDataBeingLoaded) {
            return;
        }

        if (isDataLoaded) {
            if (mFeed != null) {
                mPaginationView.setData(mFeed);
                mPaginationView.showLoading(false);
            }

            return;
        }

        getData(0);
    }

    @Override
    public void reloadData() {
        isDataLoaded = false;
        loadData();
    }

    @Override
    public void loadMoreData() {
        if (isDataBeingLoaded || !moreDataAvailable) {
            return;
        }

        boolean calling = getData(mFeed.getCurrentPage() + 1);
        if (calling) {
            mPaginationView.showLoading(true);
        }
    }

    private boolean getData(int pageNum) {

        Observable<T> observable = callApi(pageNum);

        isDataBeingLoaded = true;

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(T t) {

                    }
                });

        /*observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T tPaginationFeed) {
                        if (mFeed == null) {
                            mFeed = tPaginationFeed;
                        } else {
                            mFeed.concatFeed(tPaginationFeed);
                        }

                        moreDataAvailable = mFeed.getCurrentItemsCount() < mFeed.getTotalItemsCount();
                        isDataBeingLoaded = false;
                        isDataLoaded = true;

                        mPaginationView.setData(mFeed);
//                        onDataAvailable(mFeed);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isDataLoaded = false;
                        onError(throwable);
                    }
                });*/

        return true;

    }

    @Override
    public void saveInstance(Bundle outState) {
        if (outState != null && mFeed != null) {
            outState.putParcelable(KEY_FEED, mFeed);
            outState.putBoolean(BUNDLE_KEY_IS_DATA_LOADED, isDataLoaded);
            outState.putBoolean(BUNDLE_LOADE_MORE_ENABLED, loadMoreEnabled);
        }
    }

    @Override
    public void restoreInstance(Bundle savedState) {
        if (savedState != null) {
            mFeed = savedState.getParcelable(KEY_FEED);
            if (mFeed != null) {
                // TODO finish this
                isDataLoaded = savedState.getBoolean(BUNDLE_KEY_IS_DATA_LOADED);
                loadMoreEnabled = savedState.getBoolean(BUNDLE_LOADE_MORE_ENABLED);
            }
        }
    }

    protected abstract void onError(Throwable throwable);
    protected abstract Observable<T> callApi(int pageNum);
}
