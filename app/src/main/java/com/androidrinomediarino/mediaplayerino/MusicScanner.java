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
    private ArrayList<SongList.Song>         musicFiles;
    private List<File>              sortedMusicFiles;
    private MediaMetadataRetriever  retriever = new MediaMetadataRetriever();
    private Map<String, ArrayList<SongList.Song>>    _songsByArtist;
    private Map<String, ArrayList<SongList.Song>>    _songsByGenre;
    private Map<String, ArrayList<SongList.Song>>    _songsByAlbum;
    private SongList.Song   _currentSong;

    private MusicScanner() {

        // Init ArrayList
        musicFiles = new ArrayList<>();
        _songsByArtist = new HashMap<String, ArrayList<SongList.Song>>();
        _songsByGenre = new HashMap<String, ArrayList<SongList.Song>>();
        _songsByAlbum = new HashMap<String, ArrayList<SongList.Song>>();

        // Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // Scan for music; musicFiles lists all music
        scanMusic(musicDirPath);
    }

    public SongList.Song getCurrentSong() {
        return _currentSong;
    }

    public void setCurrentSong(SongList.Song song) {
        _currentSong = song;
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
                SongList.Song song = new SongList.Song(f);
                musicFiles.add(song);
                addToDefaultPlaylists(song);
            }
        }
    }

    private void addToDefaultPlaylists(SongList.Song song) {
        // Sort By Artist
        if (!_songsByArtist.containsKey(song.artistName)) {
            _songsByArtist.put(song.artistName, new ArrayList<SongList.Song>());
        }
        _songsByArtist.get(song.artistName).add(song);

        // Sort By Genre
        if (!_songsByGenre.containsKey(song.genre)) {
            _songsByGenre.put(song.genre, new ArrayList<SongList.Song>());
        }
        _songsByGenre.get(song.genre).add(song);

        // Sort by Album
        if (!_songsByAlbum.containsKey(song.albumName)) {
            _songsByAlbum.put(song.albumName, new ArrayList<SongList.Song>());
        }
        _songsByAlbum.get(song.albumName).add(song);
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
    public final ArrayList<SongList.Song> getMusicFiles() {
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

    public Map<String, ArrayList<SongList.Song>> getSongsByArtist() { return _songsByArtist; }
    public Map<String, ArrayList<SongList.Song>> getSongsByGenre() { return _songsByGenre; }
    public Map<String, ArrayList<SongList.Song>> getSongsByAlbum() { return _songsByAlbum; }

    public final List<File> SortFiles() {
        List<File> sortedFiles = new ArrayList<>();
//        sortedFiles = Sort.SortByArtist(musicFiles, Sort.sortOrder.ASC);
        return sortedFiles;
    }

}