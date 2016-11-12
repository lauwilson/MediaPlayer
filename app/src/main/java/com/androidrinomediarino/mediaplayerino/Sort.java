package com.androidrinomediarino.mediaplayerino;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Sort {
    public enum sortOrder {
        ASC("ASC", true),
        DESC("DESC", false);

        private String orderString;
        private boolean orderBoolean;

        private sortOrder(String name, boolean order) {
            orderString = name;
            orderBoolean = order;
        }
    };
    public static MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public static List<File> SortByArtist(List<File> music, sortOrder order) {
        Map<File, String> filenamesAndArtist = new HashMap<>();
        Map<File, String> sortedMap;
        List<File> sortedFiles = new ArrayList<>();

        for (File file : music) {
            retriever.setDataSource(file.getAbsolutePath());
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (artist == null) {
                artist = "ZZZZZZ";
            }
            filenamesAndArtist.put(file, artist);
        }

        sortedMap = SortByCompare(filenamesAndArtist, order);

        for (Map.Entry<File, String> entry : sortedMap.entrySet()) {
            File file;

            file = entry.getKey();
            sortedFiles.add(file);
        }

        return sortedFiles;
    }

    public static Map<File, String> SortByCompare(Map<File, String> unsortMap, final sortOrder order)
    {

        List<Map.Entry<File, String>> list = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<File, String>>()
        {
            public int compare(Map.Entry<File, String> o1,
                               Map.Entry<File, String> o2)
            {
                if (order.orderBoolean)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        Map<File, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<File, String> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
