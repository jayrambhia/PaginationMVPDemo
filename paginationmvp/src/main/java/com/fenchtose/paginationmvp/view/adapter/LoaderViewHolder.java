package com.fenchtose.paginationmvp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.progress_layout)
    LinearLayout mProgressLayout;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}