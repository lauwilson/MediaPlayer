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

    // Storage Permissions variables
    private static final int    REQUEST_EXTERNAL_STORAGE = 1;
    private int[]               PERMISSION_GRANT_RESULT;
    private static String[]     PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    // MediaPlayer
    private boolean             hasMusic = false;
    private final MediaPlayer   mediaPlayer = new MediaPlayer();
    private File                musicDirPath;
    private String[]            musicList;
    private File                file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permissions: MainActivity, AndroidManifest.xml
        requestStoragePermissions(this);
        onRequestPermissionsResult(REQUEST_EXTERNAL_STORAGE, PERMISSIONS_STORAGE, PERMISSION_GRANT_RESULT);
    }

    private void getMusic() {
        //Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        //Array of song paths
        musicList = musicDirPath.list(musicFilter);

        //Has Music?
        if(musicList.length > 0) {
            Log.d("Testerino", "No relevant files in Music Directory.");
            hasMusic = true;
        }
    }

    private void playMusic() {
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


    // Filter Files by Extension
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

    // Grant Permission
    private void requestStoragePermissions(Activity activity) {

        PERMISSION_GRANT_RESULT = new int[] {
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE),
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        };

        if (    PERMISSION_GRANT_RESULT[0] != PackageManager.PERMISSION_GRANTED ||  // Read
                PERMISSION_GRANT_RESULT[1] != PackageManager.PERMISSION_GRANTED) {  // Write

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // Verify Permission Granted
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {

        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission Granted
                    getMusic();

                    playMusic();

                } else {
                    // Permission Denied
                }

            } break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}