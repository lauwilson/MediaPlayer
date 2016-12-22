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
        ARTIST(0),
        GENRE(1),
        ALBUM(2);

        private final int value;

        SongGroupings(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        private static final Map<Integer, SongGroupings> groupingMap = new HashMap<>();
        static {
            for (SongGroupings grouping : SongGroupings.values()) {
                groupingMap.put(grouping.value, grouping);
            }
        }

        public static SongGroupings fromInt(int i) {
            SongGroupings grouping = groupingMap.get(Integer.valueOf(i));
            if (grouping == null) {
                throw new IllegalArgumentException();
            }
            return grouping;
        }
    }

    private static MusicScanner     instance;
    private boolean                 _initialized = false;
    private File                    musicDirPath;
    private ArrayList<Song>         musicFiles;
    private List<File>              sortedMusicFiles;
    private MediaMetadataRetriever  retriever = new MediaMetadataRetriever();
    private Map<String, ArrayList<Song>>    _songsByArtist;
    private Map<String, ArrayList<Song>>    _songsByGenre;
    private Map<String, ArrayList<Song>>    _songsByAlbum;
    private Song   _currentSong;

    private MusicScanner() {

        // Init ArrayList
//        musicFiles = new ArrayList<>();
        _songsByArtist = new HashMap<String, ArrayList<Song>>();
        _songsByGenre = new HashMap<String, ArrayList<Song>>();
        _songsByAlbum = new HashMap<String, ArrayList<Song>>();

        // Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // Scan for music; musicFiles lists all music
        scanMusic(musicDirPath);
    }

    public Song getCurrentSong() {
        return _currentSong;
    }

    public void setCurrentSong(Song song) {
        _currentSong = song;
    }

    public boolean isInitialized() { return _initialized; };

    // Thread-safe singleton
    public static MusicScanner getInstance() {
        if (instance == null) {
            instance = new MusicScanner();
            instance._initialized = true;
        }
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
                Song song = new Song(f);
                SongList.addSongToList(song);
                addToDefaultPlaylists(song);
            }
        }
        _initialized = true;
    }

    private void addToDefaultPlaylists(Song song) {
        // Sort By Artist
        if (!_songsByArtist.containsKey(song.artistName)) {
            _songsByArtist.put(song.artistName, new ArrayList<Song>());
        }
        _songsByArtist.get(song.artistName).add(song);

        // Sort By Genre
        if (!_songsByGenre.containsKey(song.genre)) {
            _songsByGenre.put(song.genre, new ArrayList<Song>());
        }
        _songsByGenre.get(song.genre).add(song);

        // Sort by Album
        if (!_songsByAlbum.containsKey(song.albumName)) {
            _songsByAlbum.put(song.albumName, new ArrayList<Song>());
        }
        _songsByAlbum.get(song.albumName).add(song);
    }

    // Check if musicFiles is NULL or EMPTY
    public final boolean hasMusic() {
        if(SongList.SongList != null && SongList.SongList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Get all music
//    public final ArrayList<Song> getMusicFiles() {
//        return musicFiles;
//    }

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

    public Map<String, ArrayList<Song>> getSongsByArtist() { return _songsByArtist; }
    public Map<String, ArrayList<Song>> getSongsByGenre() { return _songsByGenre; }
    public Map<String, ArrayList<Song>> getSongsByAlbum() { return _songsByAlbum; }

    public final List<File> SortFiles() {
        List<File> sortedFiles = new ArrayList<>();
//        sortedFiles = Sort.SortByArtist(musicFiles, Sort.sortOrder.ASC);
        return sortedFiles;
    }

}