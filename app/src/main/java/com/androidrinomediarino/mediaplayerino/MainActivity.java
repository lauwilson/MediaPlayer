package com.androidrinomediarino.mediaplayerino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean hasMusic = false;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission
        //Location: MainActivity, AndroidManifest.xml
        verifyStoragePermissions(this);

        //Access MUSIC directory
        File musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        //Array of song paths
        String[] musicList = musicDirPath.list(musicFilter);

        //Has Music?
        if(musicList.length > 0) {
            Log.d("Testerino", "No relevant files in Music Directory.");
            hasMusic = true;
        }

        //Verify prepare
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        //Play Music
        if(hasMusic) {
            file = new File(musicDirPath, musicList[0]);                //The music to play

            try {
                mediaPlayer.setDataSource(file.getAbsolutePath());
                mediaPlayer.prepare();                                  //Android requires prepare() before play, calls PreparedListener
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FilenameFilter musicFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String fileName) {
            File file;

            if(     fileName.endsWith(".mp3")   ||
                    fileName.endsWith(".flac")  ||
                    fileName.endsWith(".wav")   ){
                return true;
            }
            file = new File(dir.getAbsolutePath()+"/"+fileName);
            return file.isDirectory();
        }
    };

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}