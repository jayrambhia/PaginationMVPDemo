package com.fenchtose.paginationmvp.presenter;

import android.os.Bundle;

public interface BasePresenter {

    void attachView(Bundle args);
    void detachView();
    void resume();
    void pause();

    void saveInstance(Bundle outState);
    void restoreInstance(Bundle savedState);
}