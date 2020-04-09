package com.demo.lazy.sample;

import android.view.ViewGroup;

import com.demo.lazy.recycler.LazyAdapter;
import com.demo.lazy.recycler.LazyViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by guoxiaodong on 2020/4/8 13:16
 */
public class MainAdapter extends LazyAdapter<Integer> {
    MainAdapter(List<Integer> dataList) {
        super(dataList);
    }

    @NonNull
    @Override
    public LazyViewHolder<Integer> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(parent);
    }
}
