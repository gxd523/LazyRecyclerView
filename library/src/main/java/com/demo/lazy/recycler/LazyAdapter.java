package com.demo.lazy.recycler;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 核心类
 */
public abstract class LazyAdapter<T> extends RecyclerView.Adapter<LazyViewHolder<T>> {
    private List<T> dataList;
    private Set<LazyViewHolder<T>> viewHolderSet;
    private LazyRunnable<T> lazyRunnable;
    private RecyclerView recyclerView;

    public LazyAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * 如果只有一种类型，holder只会创建一屏个数，然后回收复用
     */
    @Override
    public void onBindViewHolder(@NonNull LazyViewHolder<T> holder, int position) {
        holder.onBindViewHolder(position);
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {// 初始状态
            holder.onLazyBindViewHolder(position);
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
                lazyRunnable = new LazyRunnable<>(viewHolderSet);
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

        LazyRunnable(Set<LazyViewHolder<T>> viewHolderSet) {
            this.viewHolderSetRef = new WeakReference<>(viewHolderSet);
        }

        @Override
        public void run() {
            Set<LazyViewHolder<T>> viewHolderSet = viewHolderSetRef.get();
            if (viewHolderSet == null || viewHolderSet.isEmpty()) {
                return;
            }
            for (LazyViewHolder<T> viewHolder : viewHolderSet) {
                viewHolder.onLazyBindViewHolder(viewHolder.getAdapterPosition());
            }
            viewHolderSet.clear();
        }
    }
}
