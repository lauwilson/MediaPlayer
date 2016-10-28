package com.androidrinomediarino.mediaplayerino;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user.gen.desktop on 2016-10-27.
 */

public class MusicScanner {

    private File    musicDirPath;
    ArrayList<File> musicFiles = new ArrayList<>();

    // Constructor
    public MusicScanner(File file) {
        musicDirPath = file;

        scan(musicDirPath);
    }


    // Get music files & get music files by folder
    private void scan(final File file) {

        for(File f : new ArrayList<>(Arrays.asList(file.listFiles()))) {
            if(f.isDirectory()) {
                Log.d("IS_DIRECTORY", f.getName().toString());

                if(file == musicDirPath) {
                    Log.d("AT_ROOT", f.getName().toString());
                }

                scan(f);

            } else if (f.getName().endsWith(".mp3") || f.getName().endsWith(".flac") || f.getName().endsWith(".wav")) {

                Log.d("IS_MUSIC", f.getName().toString());
                musicFiles.add(f);
            }
        }
    }

    public ArrayList<File> getMusicFiles() {
        return musicFiles;
    }

}
