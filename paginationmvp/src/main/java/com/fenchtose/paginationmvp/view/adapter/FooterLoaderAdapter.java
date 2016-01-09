package com.fenchtose.paginationmvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenchtose.paginationmvp.R;

import java.util.List;

/**
 * Created by Jay Rambhia on 14/09/15.
 */
public abstract class FooterLoaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = -2;

    protected List<T> mItems;
    protected LayoutInflater mInflater;

    public FooterLoaderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {
            View view = mInflater.inflate(R.layout.loader_item_layout, viewGroup, false);
            return new LoaderViewHolder(view);
        } else if (isValidViewType(viewType)) {
            return getYourItemViewHolder(viewGroup, viewType);
        }

        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder)viewHolder;
            if (showLoader) {
                loaderViewHolder.mProgressLayout.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressLayout.setVisibility(View.GONE);
            }

            return;
        }

        bindYourViewHolder(viewHolder, position);

    }

    @Override
    public int getItemCount() {

        if (mItems == null || mItems.size() == 0) {
            return 0;
        }

        return mItems.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return -1;
        }
        return getYourItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return getViewType(position);
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    public void setItems(List<T> items) {
        mItems = items;
    }

    public abstract long getYourItemId(int position);
    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent, int viewType);
    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);
    public abstract boolean isValidViewType(int viewType);
    public abstract int getViewType(int position);

}
