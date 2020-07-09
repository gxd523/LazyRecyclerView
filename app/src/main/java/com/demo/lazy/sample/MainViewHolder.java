package com.demo.lazy.sample;

import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.lazy.recycler.LazyViewHolder;

/**
 * Created by guoxiaodong on 2020/4/8 13:16
 */
public class MainViewHolder extends LazyViewHolder<Integer> {
    private TextView textView;

    MainViewHolder(ViewGroup parent, MainAdapter adapter) {
        super(parent, R.layout.adapter_main, adapter);
        itemView.getLayoutParams().height = parent.getContext().getResources().getDisplayMetrics().heightPixels / 5;
        textView = itemView.findViewById(R.id.adapter_main_text_view);
    }

    @Override
    public void onBindViewHolder(int position) {
        textView.setText("缺省Item");
        itemView.setBackgroundColor(0xFF666666);
        textView.setTextColor(0xFFFFFFFF);
    }

    @Override
    public void onLazyBindViewHolder(int position) {
        Integer itemValue = adapter.getItem(position);
        textView.setText(String.format("第%s条Item", itemValue));
        textView.setTextColor(position % 2 == 0 ? 0xFF000000 : 0xFFFFFFFF);
        itemView.setBackgroundColor(position % 2 == 0 ? 0xFFFFFFFF : 0xFF000000);
    }
}
