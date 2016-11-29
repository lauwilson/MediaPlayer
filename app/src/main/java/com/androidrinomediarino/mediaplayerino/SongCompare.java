package com.androidrinomediarino.mediaplayerino;

import java.util.Collections;
import java.util.Comparator;

public class SongCompare {
    private int sortOrder_mode = 0;
    private int sortBy_mode = 3;

    public SongCompare(SortOrderEnum order, SortByEnum by) {
        sortOrder_mode = order.getValue();
        sortBy_mode = by.getValue();
    }

    public void sortList() {
        final String sortBy = SortByEnum.fromInt(sortBy_mode).
                toString().
                toLowerCase();

        Collections.sort(SongList.SongList, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2){
                switch (sortBy) {
                    case "artistname":
                        if (sortOrder_mode == 0) {
                            return s1.artistName.compareTo(s2.artistName);
                        }
                        return s2.artistName.compareTo(s1.artistName);
                    case "albumname":
                        if (sortOrder_mode == 0) {
                            return s1.albumName.compareTo(s2.albumName);
                        }
                        return s2.albumName.compareTo(s1.albumName);
                    case "songname":
                        if (sortOrder_mode == 0) {
                            return s1.songName.compareTo(s2.songName);
                        }
                        return s2.songName.compareTo(s1.songName);
                    default:
                        return 0;
                }
            }
        });
    }
}
