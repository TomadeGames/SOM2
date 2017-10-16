package com.tomade.saufomat.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.SparseArray;

/**
 * Hilfsklasse für das Abspielen von Soundeffekten
 * Created by woors on 16.10.2017.
 */

public class SoundProvider {
    private static SoundProvider instance = new SoundProvider();
    private SparseArray<MediaPlayer> playingSoundeffects = new SparseArray<>();

    private SoundProvider() {

    }

    public static SoundProvider getInstance() {
        return instance;
    }

    /**
     * Spielt einen Soundeffekt ab
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void playSound(final int soundId, Context context) {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        MediaPlayer playingSound = this.playingSoundeffects.get(soundId);
        if (playingSound != null) {
            this.stopSound(soundId);
        }
        this.playingSoundeffects.put(soundId, mediaPlayer);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                SoundProvider.this.playingSoundeffects.remove(soundId);
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }

    /**
     * Spielt einen Soundeffekt in einer dauerschleife ab
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void playSoundloop(final int soundId, Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.setLooping(true);
        this.playingSoundeffects.put(soundId, mediaPlayer);
        mediaPlayer.start();
    }

    /**
     * Stoppt einen Soundeffekt
     *
     * @param soundId die Id des Soundeffekts (Bsp.: R.raw.soundfile.mp3)
     */
    public void stopSound(int soundId) {
        MediaPlayer mediaPlayer = this.playingSoundeffects.get(soundId);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            this.playingSoundeffects.remove(soundId);
        }
    }
}
