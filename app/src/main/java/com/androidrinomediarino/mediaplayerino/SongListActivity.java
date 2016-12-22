package com.androidrinomediarino.mediaplayerino;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Intent intent = getIntent();
        String selectedValue = intent.getStringExtra("selectedValue");
        int grouping = intent.getIntExtra("grouping", -1);
        ListView listViewSongs = (ListView) findViewById(R.id.listView_songs);

        List<Song> songSubList;
        MusicScanner scanner = MusicScanner.getInstance();

        MusicScanner.SongGroupings group = MusicScanner.SongGroupings.fromInt(grouping);
        switch (group) {
            case ALBUM:
                songSubList = scanner.getSongsByAlbum().get(selectedValue);
                break;
            case ARTIST:
                songSubList = scanner.getSongsByArtist().get(selectedValue);
                break;
            case GENRE:
                songSubList = scanner.getSongsByGenre().get(selectedValue);
                break;
            default:
                songSubList = SongList.SongList;
                break;
        }

        SongAdapter adapter = new SongAdapter(this, R.layout.songlist_listview_item_layout, songSubList);
        listViewSongs.setAdapter(adapter);
        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = (Song) parent.getItemAtPosition(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("songPath", selectedSong.filePath);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
