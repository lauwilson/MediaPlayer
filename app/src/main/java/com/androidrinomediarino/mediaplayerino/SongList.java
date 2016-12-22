package com.androidrinomediarino.mediaplayerino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongList {
    public static List<Song> SongList = new ArrayList<>();
    public static HashMap<String, Song> SongMap = new HashMap<>();

    public static void addSongsToList(Context context, List<File> files) {
        for(File file : files){
            Song song = new Song(file);
            SongList.add(song);
            SongMap.put(song.filePath, song);
        }
    }

    public static void addSongToList(File file) {
        Song song = new Song(file);
        SongList.add(song);
        SongMap.put(song.filePath, song);
    }

    public static void addSongToList(Song song) {
        SongList.add(song);
        SongMap.put(song.filePath, song);
    }

    public static Song getSongFromPath(String songPath) { return SongMap.get(songPath); }

    public static void sortList(SortOrderEnum order, SortByEnum by) {
        SongCompare comparer = new SongCompare(order, by);

        comparer.sortList();
    }

}

