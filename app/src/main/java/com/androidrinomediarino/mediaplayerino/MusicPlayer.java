package com.androidrinomediarino.mediaplayerino;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{
    // MediaPlayer
    private MediaPlayer         mediaPlayer;
    private String              filePath;
    private int                 cycleCounter = 0;
    private ArrayList<File>     musicList;
    private final IBinder       musicBind = new MusicBinder();      // interface for clients that bind
    private int                 mStartMode;                         // indicates how to behave if the service is killed
    private boolean             mAllowRebind;                       // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        Log.i("@MusicPlayer", "onCreate() is called!");
        // The service is being created
        super.onCreate();
        initMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("@MusicPlayer", "onStartCommand() is called!");
        // The service is starting, due to a call to startService()
        return START_STICKY;
        //return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("@MusicPlayer", "onBind() is called!");
        // A client is binding to the service with bindService()
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("@MusicPlayer", "onUnbind() is called!");
        // All clients have unbound with unbindService()
        return false;
        //return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("@MusicPlayer", "onRebind() is called!");
        // A client is binding to the service with bindService(),
    }

    @Override
    public void onDestroy() {
        Log.i("@MusicPlayer", "onDestroy() is called!");
        // The service is no longer used and is being destroyed
        super.onDestroy();

        stopSelf();

        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //Set MediaPlayer properties
    private void initMediaPlayer(){
        // Create MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Init
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setLooping(true);
    }

    public void setList(ArrayList<File> musicList) {
        if(this.musicList == null) {
            this.musicList = musicList;
        }
    }

    public final class MusicBinder extends Binder {
        MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    protected final void playPause() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    protected final void nextSong() {
        cycleCounter++;
        if (cycleCounter >= musicList.size()) {
            cycleCounter = 0;
        }
        playMusic(musicList.get(cycleCounter));
    }

    protected final void previousSong() {
        cycleCounter--;
        if (cycleCounter < 0) {
            cycleCounter = musicList.size() - 1;
        }
        playMusic(musicList.get(cycleCounter));
    }

    protected final void playMusic(final File musicFile) {
        mediaPlayer.reset();
        //Play Music
        try {
            filePath = musicFile.getAbsolutePath();

            //Initialized State
            mediaPlayer.setDataSource(filePath);

            //Android requires prepare() before play, calls setOnPreparedListener
            //Prepared State, can call start()
            mediaPlayer.prepareAsync();

        } catch (IOException|IllegalArgumentException e) {
            //setDataSource file might not exist
            e.printStackTrace();
        }
        // TODO: TimerTask for quicker tests
    }

    //Cycle & Play Music ArrayList
    protected final void cycle() {
        if(mediaPlayer.isPlaying()) {
            // keep playing
        } else {
            if (cycleCounter >= musicList.size()) {
                cycleCounter = 0;
            }
            playMusic(musicList.get(cycleCounter));
            cycleCounter++;
        }
    }

    protected final String getFilePath() {
        return filePath;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("@MusicPlayer", "onError() is called!");
        // The MediaPlayer has moved to the Error state, must be reset!
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i("@MusicPlayer", "onPrepared() is called!");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("@MusicPlayer", "onCompletion() is called!");
        nextSong();
    }

    //TODO: Foreground notifications

    //TODO: AudioFocus

    //TODO: AUDIO_BECOMING_NOISY
}
