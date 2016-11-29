package com.androidrinomediarino.mediaplayerino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongList {
    public static List<Song> SongList = new ArrayList<>();

    public static void addSongsToList(Context context, List<File> files) {
        for(File file : files){
            SongList.add(new Song(file));
        }
    }

    public static void addSongToList(File file) {
        SongList.add(new Song(file));
    }

    public static void addSongToList(Song song) {
        SongList.add(song);
    }

    public static void sortList(SortOrderEnum order, SortByEnum by) {
        SongCompare comparer = new SongCompare(order, by);

        comparer.sortList();
    }

}

