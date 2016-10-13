package com.androidrinomediarino.mediaplayerino;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {

    // Storage Permissions variables
    private static final int    REQUEST_CODE_EXTERNAL_STORAGE = 1;
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
        onRequestPermissionsResult(REQUEST_CODE_EXTERNAL_STORAGE, PERMISSIONS_STORAGE, PERMISSION_GRANT_RESULT);
    }

    private void getMusic() {
        //Access MUSIC directory
        musicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        //Array of song paths
        musicList = musicDirPath.list(musicFilter);

        //Has Music?
        if(musicList != null && musicList.length > 0) {
            hasMusic = true;
        } else {
            hasMusic = false;
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
    private void requestStoragePermissions(final Activity activity) {

        final boolean requestPermissionRational = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(!requestPermissionRational) {
            showMessageOKCancel("You need to allow access to Music Storage",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions(
                                    activity,
                                    PERMISSIONS_STORAGE,
                                    REQUEST_CODE_EXTERNAL_STORAGE
                            );
                        }
                    });
        }


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
                    REQUEST_CODE_EXTERNAL_STORAGE
            );
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Verify Permission Granted
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission Granted
                    getMusic();

                    playMusic();

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "READ_WRITE Denied", Toast.LENGTH_SHORT).show();
                }

            } break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
