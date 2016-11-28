package com.androidrinomediarino.mediaplayerino;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.File;

public class Song {
    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    File file;
    String filePath;
    String songName;
    String artistName;
    String albumName;
    String genre;
    Bitmap coverArt;


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

    private void extractArtistName() {
        String extractedArtistName;

        extractedArtistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        artistName = extractedArtistName == null ? "Unknown Artist" : extractedArtistName;
    }

    private void extractAlbumName() {
        String extractedAlbumName;

        extractedAlbumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        albumName = extractedAlbumName == null ? "Unknown Album" : extractedAlbumName;
    }

    private void extractCoverArt() {
        byte[] coverArtBytes = retriever.getEmbeddedPicture();
        if (coverArtBytes != null) {
            Bitmap bitmap = BitmapFactory.
                    decodeByteArray(coverArtBytes, 0, coverArtBytes.length);
            coverArt = bitmap;
        } else {
            coverArt = BitmapFactory.decodeResource(MainApplication.getContext().getResources(),
                    R.drawable.default_album_art);
        }
    }
}

