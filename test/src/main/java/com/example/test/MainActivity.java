package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filterlayout.FilterLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements FilterLayout.MenuSelectListener {

    private FilterLayout mFilterLayout;
    private ArrayList[] arrayLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    private void initData() {
        LinkedHashMap<List<String>, Integer> map = Util.getMap();
        Set<List<String>> lists = map.keySet();
        arrayLists = lists.toArray(new ArrayList[0]);

        mFilterLayout.setMenuList(map);

        mFilterLayout.setMenuSelectListener(this);

        ListView textView = new ListView(this);
        final ArrayList<String> objects = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                objects);
        textView.setAdapter(arrayAdapter);
        mFilterLayout.setContentView(textView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    objects.add("item num " + i);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        mFilterLayout = (FilterLayout) findViewById(R.id.main_filterlayout);
    }

    @Override
    public void onMenuSelect(int tabPosition, int menuPosition) {
        String s = (String) arrayLists[tabPosition].get(menuPosition);
        Toast.makeText(this, "s: " + s, Toast.LENGTH_SHORT).show();
    }
}
