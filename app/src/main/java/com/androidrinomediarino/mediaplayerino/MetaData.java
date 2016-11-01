package com.androidrinomediarino.mediaplayerino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public final class MetaData {
    private static MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private static ImageView coverImage;
    private static TextView songInfo;

    public static void GetSongMetaData(Context context, String path, ViewGroup root) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_song_metadata, root);
        coverImage = (ImageView)layout.findViewById(R.id.imageView);

        retriever.setDataSource(path);

        byte[] coverArtBytes =  retriever.getEmbeddedPicture();
        if(coverArtBytes!=null)
        {
            Bitmap bitmap = BitmapFactory.
                    decodeByteArray(coverArtBytes, 0, coverArtBytes.length);
            coverImage.setImageBitmap(bitmap);
        }
        else
        {
            coverImage.setImageDrawable(coverImage.getResources().
                    getDrawable(R.drawable.default_album_art));
        }

        songInfo = (TextView)layout.findViewById(R.id.textView);
        String songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        songInfo.setText(songName + " - " + artistName);
    }
}
