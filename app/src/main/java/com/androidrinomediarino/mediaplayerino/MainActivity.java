package com.androidrinomediarino.mediaplayerino;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongMetadataFragment.OnFragmentInteractionListener,
                                                                CurrentPlaylistFragment.OnFragmentInteractionListener {

    // Storage Permissions variables
    private static final int    REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private int[]               PERMISSION_GRANT_RESULT;
    private static String[]     PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //MusicScanner
    private MusicScanner        musicScanner;
    private ArrayList<File>     musicFiles;

    //MusicPlayer
    private MusicPlayer         musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

        // Initialize the starting fragment
        CurrentPlaylistFragment initialFragment = new CurrentPlaylistFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, initialFragment, "CURRENT_PLAYLIST")
                .commit();

        findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // TODO: Insert onclick logic for the previous button.
            }
        });
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // TODO: Insert onclick logic for the play button.
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // TODO: Insert onclick logic for the next button.
            }
        });

        //Permissions: MainActivity, AndroidManifest.xml
        requestStoragePermissions(this);
        onRequestPermissionsResult(REQUEST_CODE_EXTERNAL_STORAGE, PERMISSIONS_STORAGE, PERMISSION_GRANT_RESULT);
    }

    // Grant Permission
    private void requestStoragePermissions(final Activity activity) {

        PERMISSION_GRANT_RESULT = new int[] {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE),
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        };

        if (PERMISSION_GRANT_RESULT[0] != PackageManager.PERMISSION_GRANTED ||  // Read
            PERMISSION_GRANT_RESULT[1] != PackageManager.PERMISSION_GRANTED) {  // Write

            final boolean requestPermissionRational = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(requestPermissionRational) {

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
                return;

            } else {

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
                    // Permission Granted: dependent code is ready to run

                    //Get list of music files
                    musicScanner = new MusicScanner();

                    //Post files to MediaPlayer
                    musicPlayer = new MusicPlayer(musicScanner);

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "READ_WRITE Denied", Toast.LENGTH_SHORT).show();
                }

            } break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Implementation of OnFragmentInteraction interface method.
     */
    public void btn_fragmentSwitch_onClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment playlistFragment = fragmentManager.findFragmentByTag("CURRENT_PLAYLIST");

        if (playlistFragment != null && playlistFragment.isVisible()) {
            Fragment songMetaDataFragment = new SongMetadataFragment();
            Bundle bundle = new Bundle();
            bundle.putString("filePath", musicPlayer.getFilePath());
            songMetaDataFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, songMetaDataFragment, "SONG_METADATA").commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new CurrentPlaylistFragment(), "CURRENT_PLAYLIST").commit();
        }
    }
}
