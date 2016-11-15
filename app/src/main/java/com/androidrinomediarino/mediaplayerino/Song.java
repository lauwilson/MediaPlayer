package com.androidrinomediarino.mediaplayerino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import java.io.File;

public class Song {
    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    public Context context;
    public File file;
    public String filePath;
    public String songName;
    public String artistName;
    public String albumName;
    public Bitmap coverArt;

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
        songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    private void extractArtistName(){
        artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    private void extractAlbumName(){
        albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
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

    public File getFile(){
        return file;
    }

    public void setFile(File file){
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSongName(){
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName(){
        return albumName;
    }

    public Bitmap getCoverArt(){
        return coverArt;
    }

}
