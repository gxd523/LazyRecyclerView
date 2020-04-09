package com.demo.lazy.recycler;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by guoxiaodong on 2020/4/8 13:05
 */
public abstract class LazyAdapter<T> extends RecyclerView.Adapter<LazyViewHolder<T>> {
    private List<T> dataList;
    private Set<LazyViewHolder<T>> viewHolderSet;
    private LazyRunnable lazyRunnable;
    private RecyclerView recyclerView;

    public LazyAdapter() {
    }

    public LazyAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(@NonNull LazyViewHolder<T> holder, int position) {
        holder.onBindViewHolder(dataList.get(position), position);
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {// 初始状态
            holder.onLazyBindViewHolder(dataList.get(position), position);
        } else {
            viewHolderSet.add(holder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                viewHolderSet = new HashSet<>();
                lazyRunnable = new LazyRunnable<>(viewHolderSet, dataList);
                ((RecyclerView) v).addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            recyclerView.removeCallbacks(lazyRunnable);
                            recyclerView.postDelayed(lazyRunnable, 200);
                        }
                    }
                });
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                v.removeOnAttachStateChangeListener(this);
                ((RecyclerView) v).clearOnScrollListeners();
                if (viewHolderSet.size() > 0) {
                    viewHolderSet.clear();
                    viewHolderSet = null;
                }
                lazyRunnable = null;
                LazyAdapter.this.recyclerView = null;
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull LazyViewHolder holder) {
        viewHolderSet.remove(holder);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public T getItem(int position) {
        return dataList != null && dataList.size() > position ? dataList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private static class LazyRunnable<T> implements Runnable {
        private WeakReference<Set<LazyViewHolder<T>>> viewHolderSetRef;
        private WeakReference<List<T>> dataListRef;

        LazyRunnable(Set<LazyViewHolder<T>> viewHolderSet, List<T> dataList) {
            this.viewHolderSetRef = new WeakReference<>(viewHolderSet);
            this.dataListRef = new WeakReference<>(dataList);
        }

        @Override
        public void run() {
            Set<LazyViewHolder<T>> viewHolderSet = this.viewHolderSetRef.get();
            List<T> dataList = dataListRef.get();
            if (viewHolderSet != null && dataList != null) {
                for (LazyViewHolder<T> viewHolder : viewHolderSet) {
                    viewHolder.onLazyBindViewHolder(dataList.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
                viewHolderSet.clear();
            }
        }
    }
}
