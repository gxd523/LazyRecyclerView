package com.demo.lazy.sample;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.activity_main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 999; i++) {
            dataList.add(i);
        }
        recyclerView.setAdapter(new MainAdapter(dataList));
    }
}
