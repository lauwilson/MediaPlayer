package com.androidrinomediarino.mediaplayerino;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicScanner {

    private File                    musicDirPath;
    private ArrayList<File>         musicFiles;
    private List<File>              sortedMusicFiles;
    private MediaMetadataRetriever  retriever = new MediaMetadataRetriever();

    protected MusicScanner() {

        // Init ArrayList
        musicFiles = new ArrayList<>();

        // Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // Scan for music; musicFiles lists all music
        scanMusic(musicDirPath);
    }

    // Get music files & get music files by folder
    private final void scanMusic(final File file) {
        for(File f : new ArrayList<>(Arrays.asList(file.listFiles()))) {

            if(f.isDirectory())
            {
                // Inside a directory
                if(file == musicDirPath) {  /* Do at root */ }

                // Recursively scan subdirectory
                scanMusic(f);

            } else if (f.getName().endsWith(".mp3")  ||
                       f.getName().endsWith(".flac") ||
                       f.getName().endsWith(".wav"))
            {
                musicFiles.add(f);
            }
        }
    }

    // Check if musicFiles is NULL or EMPTY
    public final boolean hasMusic() {
        if(musicFiles != null && musicFiles.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Get all music
    public final ArrayList<File> getMusicFiles() {
        return musicFiles;
    }

    public List<String> GetSongNameAndArtist() {
        List<String> songs = new ArrayList<>();

        sortedMusicFiles = SortFiles();

        for (File file : sortedMusicFiles) {
            try {
                retriever.setDataSource(file.getAbsolutePath());
                String songName =   retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                if (songName == null) {
                    songName = "";
                }

                String songArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                if (songArtist == null) {
                    songArtist = "";
                }

                songs.add(songName + " - " + songArtist);

            } catch (Exception ex) {
                String[] pathFragments;
                String fileName;

                pathFragments = file.getAbsolutePath().split("/");
                fileName = pathFragments[pathFragments.length - 1];
                songs.add(fileName);
            }
        }

        return songs;
    }

    public final List<File> SortFiles() {
        List<File> sortedFiles = new ArrayList<>();
        sortedFiles = Sort.SortByArtist(musicFiles, Sort.sortOrder.ASC);
        return sortedFiles;
    }

}