package com.androidrinomediarino.mediaplayerino;

import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {

    // MediaPlayer
    private MediaPlayer   mediaPlayer;
    private String        filePath;
    private MusicScanner  musicScanner;
    private int           cycleCounter;

    MusicPlayer(final MusicScanner musicScanner) {
        mediaPlayer = new MediaPlayer();
        this.musicScanner = musicScanner;

        mediaPlayer.setOnCompletionListener(onCompletion);

        if(musicScanner.hasMusic()) {

            cycleCounter = 0;

            playMusic(musicScanner.getMusicFiles().get(0));
        }
    }

    protected final void playMusic(final File musicFile) {

        //Verify prepare
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        //Play Music
        try {
            filePath = musicFile.getAbsolutePath();
            mediaPlayer.setDataSource(filePath);

            //Android requires prepare() before play, calls setOnPreparedListener
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TimerTask for quicker tests...
    }

    //Cycle & Play Music ArrayList
    private final void cycle() {
        mediaPlayer.reset();

        cycleCounter++;
        if (cycleCounter >= musicScanner.getMusicFiles().size()) {
            cycleCounter = 0;
        }

        playMusic(musicScanner.getMusicFiles().get(cycleCounter));

    }

    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            cycle();
        }
    };

    private final void playbackRules() {
        //loop
    }

    protected final String getFilePath() {
        return filePath;
    }

}
