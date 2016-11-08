package com.androidrinomediarino.mediaplayerino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AddSongActivity extends AppCompatActivity {

    private MusicScanner scanner = MusicScanner.getInstance();

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

        HashMap<String, ArrayList<File>> songsByArtist = (HashMap) scanner.getSongsByArtist();
        Set<String> artistSet = songsByArtist.keySet();
        ArrayAdapter<String> artistList = new ArrayAdapter<String>(this,
                                                                   R.layout.songlist_listview_item_layout,
                                                                   new ArrayList(artistSet));
        ((ListView)findViewById(R.id.listView_songsByArtist)).setAdapter(artistList);

    }

}
