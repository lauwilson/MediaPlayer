package com.androidrinomediarino.mediaplayerino;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MusicScanner {

    public enum SongGroupings {
        ARTIST,
        GENRE,
        ALBUM
    }

    private static MusicScanner     instance = new MusicScanner();
    private File                    musicDirPath;
    private ArrayList<File>         musicFiles;
    private List<File>              sortedMusicFiles;
    private MediaMetadataRetriever  retriever = new MediaMetadataRetriever();
    private Map<String, ArrayList<File>>    _songsByArtist;
    private Map<String, ArrayList<File>>    _songsByGenre;
    private Map<String, ArrayList<File>>    _songsByAlbum;

    private MusicScanner() {

        // Init ArrayList
        musicFiles = new ArrayList<>();
        _songsByArtist = new HashMap<String, ArrayList<File>>();
        _songsByGenre = new HashMap<String, ArrayList<File>>();
        _songsByAlbum = new HashMap<String, ArrayList<File>>();

        // Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // Scan for music; musicFiles lists all music
        scanMusic(musicDirPath);
    }

    // Thread-safe singleton
    public static MusicScanner getInstance() {
        return instance;
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
                addToDefaultPlaylists(f);
            }
        }
    }

    private void addToDefaultPlaylists(File file) {
        retriever.setDataSource(file.getAbsolutePath());

        // Sort By Artist
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        artist = (artist == null) ? "Unknown Artist" : artist;
        if (!_songsByArtist.containsKey(artist)) {
            _songsByArtist.put(artist, new ArrayList<File>());
        }
        _songsByArtist.get(artist).add(file);

        // Sort By Genre
        String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        genre = (genre == null) ? "Unknown Genre" : genre;
        if (!_songsByGenre.containsKey(genre)) {
            _songsByGenre.put(genre, new ArrayList<File>());
        }
        _songsByGenre.get(genre).add(file);

        // Sort by
        String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        album = (album == null) ? "Unknown Album" : album;
        if (!_songsByAlbum.containsKey(album)) {
            _songsByAlbum.put(album, new ArrayList<File>());
        }
        _songsByAlbum.get(album).add(file);
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

    public Map<String, ArrayList<File>> getSongsByArtist() { return _songsByArtist; }
    public Map<String, ArrayList<File>> getSongsByGenre() { return _songsByGenre; }
    public Map<String, ArrayList<File>> getSongsByAlbum() { return _songsByAlbum; }

    public final List<File> SortFiles() {
        List<File> sortedFiles = new ArrayList<>();
        sortedFiles = Sort.SortByArtist(musicFiles, Sort.sortOrder.ASC);
        return sortedFiles;
    }

}