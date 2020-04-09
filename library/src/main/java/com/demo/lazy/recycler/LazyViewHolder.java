package com.demo.lazy.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by guoxiaodong on 2020/4/8 13:04
 */
public abstract class LazyViewHolder<T> extends RecyclerView.ViewHolder {
    public LazyViewHolder(ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public abstract void onBindViewHolder(T t, int position);

    public abstract void onLazyBindViewHolder(T t, int position);
}
