package com.androidrinomediarino.mediaplayerino;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class AddSongActivity extends AppCompatActivity {

    private MusicScanner scanner = MusicScanner.getInstance();
    private final static int SONG_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();
        TabSpec artistSpec = host.newTabSpec("Artist");
        artistSpec.setContent(R.id.tab_artist);
        artistSpec.setIndicator("Artist");
        host.addTab(artistSpec);

        TabSpec genreSpec = host.newTabSpec("Genre");
        genreSpec.setContent(R.id.tab_genre);
        genreSpec.setIndicator("Genre");
        host.addTab(genreSpec);

        TabSpec albumSpec = host.newTabSpec("Album");
        albumSpec.setContent(R.id.tab_album);
        albumSpec.setIndicator("Album");
        host.addTab(albumSpec);

        HashMap<String, ArrayList<Song>> songsByArtist = (HashMap) scanner.getSongsByArtist();
        String[] artistList = songsByArtist.keySet().toArray(new String[songsByArtist.size()]);
        Arrays.sort(artistList);
        ArrayAdapter<String> artistAdapter = new ArrayAdapter<String>(this,
                                                                   R.layout.songlist_listview_item_layout,
                                                                   artistList);
        ((ListView)findViewById(R.id.listView_songsByArtist)).setAdapter(artistAdapter);

        HashMap<String, ArrayList<Song>> songsByGenre = (HashMap) scanner.getSongsByGenre();
        String[] genreList = songsByGenre.keySet().toArray(new String[songsByGenre.size()]);
        Arrays.sort(genreList);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this,
                R.layout.songlist_listview_item_layout,
                genreList);
        // fix for genre tab
        ((ListView)findViewById(R.id.listView_songsByGenre)).setAdapter(genreAdapter);

        HashMap<String, ArrayList<Song>> songsByAlbum = (HashMap) scanner.getSongsByAlbum();
        String[] albumList = songsByAlbum.keySet().toArray(new String[songsByAlbum.size()]);
        Arrays.sort(albumList);
        ArrayAdapter<String> albumAdapter = new ArrayAdapter<String>(this,
                R.layout.songlist_listview_item_layout,
                albumList);
        ((ListView)findViewById(R.id.listView_songsByAlbum)).setAdapter(albumAdapter);

        // Set list items onclick listener for Artist
        ((ListView)findViewById(R.id.listView_songsByArtist)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedArtist = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
                intent.putExtra("grouping", MusicScanner.SongGroupings.ARTIST.getValue());
                intent.putExtra("selectedValue", selectedArtist);
                startActivityForResult(intent, SONG_SELECT);
            }
        });

        // Set list items onclick listener for Genre
        ((ListView)findViewById(R.id.listView_songsByGenre)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
                intent.putExtra("grouping", MusicScanner.SongGroupings.GENRE.getValue());
                intent.putExtra("selectedValue", selectedGenre);
                startActivityForResult(intent, SONG_SELECT);
            }
        });

        // Set list items onclick listener for Album
        ((ListView)findViewById(R.id.listView_songsByAlbum)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAlbum = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
                intent.putExtra("grouping", MusicScanner.SongGroupings.ALBUM.getValue());
                intent.putExtra("selectedValue", selectedAlbum);
                startActivityForResult(intent, SONG_SELECT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SONG_SELECT:
                if (resultCode == Activity.RESULT_OK) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("songPath", data.getStringExtra("songPath"));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;
        }
    }

}
