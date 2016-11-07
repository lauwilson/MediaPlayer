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
    private final IBinder       musicBind = new MusicBinder();

    // The system invokes this method to perform one-time setup procedures
    // when the service is initially created
    // (before it calls either onStartCommand() or onBind())
    @Override
    public void onCreate() {
        //create the service
        super.onCreate();

        // Create MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Init EventListeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);

        //Set MediaPlayer properties
        initMediaPlayer();
    }

    //Set MediaPlayer properties
    private void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setList(ArrayList<File> musicList) {
        this.musicList = musicList;
    }

    public final class MusicBinder extends Binder {
        MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    // Must provide an interface that clients use to communicate with the service by returning an IBinder.
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    // When service instance is unbound; executes when user exits the app
    @Override
    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
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
        if (cycleCounter >= musicList.size()) {
            cycleCounter = 0;
        }
        playMusic(musicList.get(cycleCounter));
        cycleCounter++;
    }

    protected final String getFilePath() {
        return filePath;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // The MediaPlayer has moved to the Error state, must be reset!
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        cycle();
    }

    // TODO: playback rules

    // TODO: LifeCycle

    // TODO: Create Foreground Service - Notifications
}
