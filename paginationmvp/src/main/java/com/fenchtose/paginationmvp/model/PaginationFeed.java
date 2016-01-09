package com.fenchtose.paginationmvp.model;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by Jay Rambhia on 28/11/15.
 */
public interface PaginationFeed<T extends Parcelable> extends Parcelable {

    int getCurrentPage();
    int getTotalItemsCount();
    int getCurrentItemsCount();
    List<T> getItems();
    void concatFeed(T t);
}
