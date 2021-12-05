package com.pritampachal.mymusicplayer2021;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private TextView textViewSong, textViewDurationStart, textViewDurationEnd;
    private SeekBar seekBar;
    private ImageView imageViewPrev, imageViewPlayPause, imageViewNext;
    private ArrayList<File> arrayListAllSongs;
    private String songName;
    private int currentSongPosition, a1, a2, a3, x1, x2, x3;
    private Uri uri;
    private MediaPlayer mediaPlayer;
    private Handler handler; //os.Handler
    private Runnable runnable; //java.lang

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textViewSong = findViewById(R.id.textViewSongName);
        textViewDurationStart = findViewById(R.id.textViewDurationStart);
        textViewDurationEnd = findViewById(R.id.textViewDurationEnd);
        seekBar = findViewById(R.id.seekBar);
        imageViewPrev = findViewById(R.id.imageViewPrev);
        imageViewPlayPause = findViewById(R.id.imageViewPlayPause);
        imageViewNext = findViewById(R.id.imageViewNext);
        arrayListAllSongs = (ArrayList) getIntent().getExtras().getParcelableArrayList("allSongs");
        currentSongPosition = getIntent().getIntExtra("currentSongPosition", 0);
        songName = getIntent().getStringExtra("currentSong");
        textViewSong.setText(songName);
        textViewSong.setSelected(true);
        uri = Uri.parse(arrayListAllSongs.get(currentSongPosition).toString());
        mediaPlayer = MediaPlayer.create(MainActivity2.this, uri);
        x1 = -1;
        a1 = (mediaPlayer.getDuration()) / 1000; //total-second
        a2 = a1 / 60; //main-minute
        a3 = a1 % 60; //main-second
        textViewDurationEnd.setText(a2 + ":" + a3);
        imageViewPlayPause.setImageResource(R.drawable.pausered);
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        //start, continue-running-in-background, this is called main-Thread
        handler = new Handler(); //os.Handler
        runnable = new Runnable() { //java.lang
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                x1 = (mediaPlayer.getCurrentPosition()) / 1000; //total-second
                x2 = x1 / 60; //main-minute
                x3 = x1 % 60; //main-second
                textViewDurationStart.setText(x2 + ":" + x3);
                if ((a1-1) <= x1) { //code for, auto-play-next-song
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (currentSongPosition == arrayListAllSongs.size() - 1) {
                        currentSongPosition = 0;
                    } else {
                        currentSongPosition++;
                    }
                    textViewSong.setText(arrayListAllSongs.get(currentSongPosition).getName().replace(".mp3", ""));
                    textViewSong.setSelected(true);
                    uri = Uri.parse(arrayListAllSongs.get(currentSongPosition).toString());
                    mediaPlayer = MediaPlayer.create(MainActivity2.this, uri);
                    imageViewPlayPause.setImageResource(R.drawable.pausered);
                    seekBar.setMax(mediaPlayer.getDuration());
                    x1 = -1;
                    a1 = (mediaPlayer.getDuration()) / 1000; //total-second
                    a2 = a1 / 60; //integer-minute
                    a3 = a1 % 60; //main-second
                    textViewDurationEnd.setText(a2 + ":" + a3);
                    mediaPlayer.start();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
        //end, continue-running-in-background, this is called main-Thread
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    x1 = (mediaPlayer.getCurrentPosition()) / 1000; //total-second
                    x2 = x1 / 60; //main-minute
                    x3 = x1 % 60; //main-second
                    textViewDurationStart.setText(x2 + ":" + x3);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //none
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //none
            }
        });
        imageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imageViewPlayPause.setImageResource(R.drawable.playgreen);
                } else {
                    mediaPlayer.start();
                    imageViewPlayPause.setImageResource(R.drawable.pausered);
                }
            }
        });
        imageViewPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (currentSongPosition == 0) {
                    currentSongPosition = arrayListAllSongs.size() - 1;
                } else {
                    currentSongPosition--;
                }
                textViewSong.setText(arrayListAllSongs.get(currentSongPosition).getName().replace(".mp3", ""));
                textViewSong.setSelected(true);
                uri = Uri.parse(arrayListAllSongs.get(currentSongPosition).toString());
                mediaPlayer = MediaPlayer.create(MainActivity2.this, uri);
                imageViewPlayPause.setImageResource(R.drawable.pausered);
                seekBar.setMax(mediaPlayer.getDuration());
                x1 = -1;
                a1 = (mediaPlayer.getDuration()) / 1000; //total-second
                a2 = a1 / 60; //integer-minute
                a3 = a1 % 60; //main-second
                textViewDurationEnd.setText(a2 + ":" + a3);
                mediaPlayer.start();
            }
        });
        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (currentSongPosition == arrayListAllSongs.size() - 1) {
                    currentSongPosition = 0;
                } else {
                    currentSongPosition++;
                }
                textViewSong.setText(arrayListAllSongs.get(currentSongPosition).getName().replace(".mp3", ""));
                textViewSong.setSelected(true);
                uri = Uri.parse(arrayListAllSongs.get(currentSongPosition).toString());
                mediaPlayer = MediaPlayer.create(MainActivity2.this, uri);
                imageViewPlayPause.setImageResource(R.drawable.pausered);
                seekBar.setMax(mediaPlayer.getDuration());
                x1 = -1;
                a1 = (mediaPlayer.getDuration()) / 1000; //total-second
                a2 = a1 / 60; //integer-minute
                a3 = a1 % 60; //main-second
                textViewDurationEnd.setText(a2 + ":" + a3);
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        handler.removeCallbacks(runnable);
    }
}
