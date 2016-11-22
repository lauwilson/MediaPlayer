package com.androidrinomediarino.mediaplayerino;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilson on 2016-11-22.
 */

public class SongAdapter extends ArrayAdapter<SongList.Song> {
    Context context;
    int layoutResourceId;
    ArrayList<SongList.Song> songs = null;
    public SongAdapter(Context context, int resource, List<SongList.Song> songs) {
        super(context, resource, songs);
        this.layoutResourceId = resource;
        this.context = context;
        this.songs = (ArrayList) songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = ((Activity)context).getLayoutInflater().inflate(layoutResourceId, parent, false);
        }
        SongList.Song song = songs.get(position);
        ((TextView) row.findViewById(R.id.txtView_songTitle)).setText(song.songName);
        return row;
    }
}
