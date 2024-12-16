package com.kmkz.xuuDiamondsRBXSkins.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kmkz.xuuDiamondsRBXSkins.MainActivity;
import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.game.FlyingFishView;
import com.kmkz.xuuDiamondsRBXSkins.utility.Constants;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import java.util.Timer;

public class DuckGameActivity extends AppCompatActivity {

    FlyingFishView gameView;
    Handler handler = new Handler();
    final static long Interval = 30;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_duck_game);

        gameView = new FlyingFishView(this);
        setContentView(gameView);

        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    gameView.invalidate();
                });
            }
        }, 0, Interval);

        // Start the background sound
        startBackgroundSound();
    }

    private void startBackgroundSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.underwater_sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tools.startNewActivity(this, MainActivity.class);
        finish();
    }
}