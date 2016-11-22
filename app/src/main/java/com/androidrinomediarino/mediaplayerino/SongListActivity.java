package com.androidrinomediarino.mediaplayerino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Intent intent = getIntent();
        String selectedValue = intent.getStringExtra("selectedValue");
        int grouping = intent.getIntExtra("grouping", -1);
    }
}
