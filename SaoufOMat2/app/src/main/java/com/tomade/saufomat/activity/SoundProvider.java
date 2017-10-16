package com.tomade.saufomat.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.SparseArray;

/**
 * Hilfsklasse für das Abspielen von Soundeffekten
 * Created by woors on 16.10.2017.
 */

public class SoundProvider {
    private Context context;
    private SparseArray<MediaPlayer> playingSoundeffekts;

    public SoundProvider(Context context) {
        this.context = context;
        this.playingSoundeffekts = new SparseArray<>();
    }

    /**
     * Spielt einen Soundeffekt ab
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void playSound(final int soundId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this.context, soundId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                SoundProvider.this.playingSoundeffekts.remove(soundId);
            }
        });
        mediaPlayer.start();
    }

    /**
     * Spielt einen Soundeffekt in einer dauerschleife ab
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void playSoundloop(final int soundId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this.context, soundId);
        mediaPlayer.setLooping(true);
        this.playingSoundeffekts.put(soundId, mediaPlayer);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                SoundProvider.this.playingSoundeffekts.remove(soundId);
            }
        });
        mediaPlayer.start();
    }

    /**
     * Stoppt einen Soundeffekt
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void stopSound(int soundId) {
        MediaPlayer mediaPlayer = this.playingSoundeffekts.get(soundId);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
