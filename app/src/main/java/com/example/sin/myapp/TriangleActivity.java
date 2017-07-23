package com.example.sin.myapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ui.TriangleView;

public class TriangleActivity extends AppCompatActivity {

    private TriangleView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);

        mTv = (TriangleView) findViewById(R.id.triangle_tv);
        mTv.setTriangleColor(Color.rgb(0xFF,0X00,0X00));
    }
}
