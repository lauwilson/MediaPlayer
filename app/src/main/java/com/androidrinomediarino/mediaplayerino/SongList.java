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
            SongList.add(new Song(context, file));
        }
    }

    public static void sortList(SortOrderEnum order, SortByEnum by) {
        SongCompare comparer = new SongCompare(order, by);

        comparer.sortList();
    }

    public static final class Song {
        private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        private Context context;
        File file;
        String filePath;
        String songName;
        String artistName;
        String albumName;
        String genre;
        Bitmap coverArt;

        // this constructor is probably redundant now, since we can get the application context from the MainApplication class.
        public Song(Context context, File file) {
            this.context = context;
            this.file = file;
            filePath = file.getAbsolutePath();
            retriever.setDataSource(filePath);
            extractSongName();
            extractArtistName();
            extractAlbumName();
            extractCoverArt();
            extractGenre();
        }

        public Song(File file) {
            this.file = file;
            filePath = file.getAbsolutePath();
            retriever.setDataSource(filePath);
            extractSongName();
            extractArtistName();
            extractAlbumName();
            extractCoverArt();
            extractGenre();
        }

        private void extractSongName() {
            String extractedSongName;

            extractedSongName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            songName = extractedSongName == null ? "Unknown Song" : extractedSongName;
        }

        private void extractGenre() {
            String extractedGenre;

            extractedGenre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);

            genre = (extractedGenre == null) ? "Unknown Genre" : extractedGenre;
        }

        private void extractArtistName(){
            String extractedArtistName;

            extractedArtistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            artistName = extractedArtistName == null ? "Unknown Artist" : extractedArtistName;
        }

        private void extractAlbumName(){
            String extractedAlbumName;

            extractedAlbumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

            albumName = extractedAlbumName == null ? "Unknown Album" : extractedAlbumName;
        }

        private void extractCoverArt() {
            byte[] coverArtBytes =  retriever.getEmbeddedPicture();
            if(coverArtBytes!=null)
            {
                Bitmap bitmap = BitmapFactory.
                        decodeByteArray(coverArtBytes, 0, coverArtBytes.length);
                coverArt = bitmap;
            }
            else
            {
                coverArt = BitmapFactory.decodeResource(MainApplication.getContext().getResources(),
                        R.drawable.default_album_art);
            }
        }
    }
}

