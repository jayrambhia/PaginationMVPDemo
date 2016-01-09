package com.fenchtose.paginationmvp.view;

import com.fenchtose.paginationmvp.model.PaginationFeed;

/**
 * Created by Administrator on 11/24/2015.
 */
public interface PaginationBaseView<T extends PaginationFeed> {

    void loadData();
    void reloadData();
    void setData(T t);
    void showLoading(boolean status);
    void showError(CharSequence text);
    void notifyDataCleared();

}
