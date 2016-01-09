package com.fenchtose.paginationmvp.presenter;

import com.fenchtose.paginationmvp.model.PaginationFeed;
import com.fenchtose.paginationmvp.view.PaginationBaseView;

/**
 * Created by Administrator on 11/24/2015.
 */
public interface PaginationBasePresenter<T extends PaginationFeed, V extends PaginationBaseView> extends BasePresenter {

    String KEY_FEED = "feed";

    void loadData();
    void reloadData();
    void loadMoreData();
}
