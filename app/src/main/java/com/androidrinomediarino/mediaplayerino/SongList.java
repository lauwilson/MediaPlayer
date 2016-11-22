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
        Bitmap coverArt;

        public Song(Context context, File file) {
            this.context = context;
            this.file = file;
            filePath = file.getAbsolutePath();
            retriever.setDataSource(filePath);
            extractSongName();
            extractArtistName();
            extractAlbumName();
            extractCoverArt();
        }

        private void extractSongName() {
            String extractedSongName;

            extractedSongName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            songName = extractedSongName == null ? "Unknown Song" : extractedSongName;
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
                coverArt = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.default_album_art);
            }
        }
    }
}

