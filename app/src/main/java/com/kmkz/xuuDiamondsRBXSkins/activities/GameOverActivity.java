package com.kmkz.xuuDiamondsRBXSkins.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kmkz.xuuDiamondsRBXSkins.MainActivity;
import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.AdsManager;
import com.kmkz.xuuDiamondsRBXSkins.utility.Constants;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import java.util.Objects;

public class GameOverActivity extends AppCompatActivity {

    Button playAgain, goHome;
    TextView txtScoreOver, txtBestScore;
    int bestScore, score;
    SharedPreferences prefs;
    AdsManager adsManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_game_over);

        adsManager = new AdsManager(); // Initialize AdsManager
        if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
        {
            AdsManager.showBannerAdmobAds(this);
            adsManager.setRewardedAd(this);
        } else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
        {
            AdsManager.initApplovinAds(this);
            adsManager.loadMaxRewardsAd(this);
        }

        playAgain = findViewById(R.id.playAgain);
        goHome = findViewById(R.id.goHome);
        txtScoreOver = findViewById(R.id.txt_score_over);
        txtBestScore = findViewById(R.id.txt_best_score);

        Bundle bundle = getIntent().getExtras();
        score = bundle.getInt("score");

        Tools.addThePoints(this,score);

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        bestScore = prefs.getInt("bestScore", 0);

        if (score > bestScore)
        {
            bestScore = score;
            SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("bestScore", bestScore);
            editor.apply();
        }

        txtScoreOver.setText(getString(R.string.score) + ": " + score);
        txtBestScore.setText(getString(R.string.best_score) + ": " + bestScore);

        playAgain.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, DuckGameActivity.class);
            startActivity(intent);
            finish();

            //ads
            if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
            {
                adsManager.showRewardsAs(this, 0, false, "scratch");
            }
            else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
            {
                adsManager.showMaxVideoAds(this, 0, false, "scratch");
            }

        });

        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            //ads
            if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
            {
                adsManager.showRewardsAs(this, 0, false, "scratch");
            }
            else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
            {
                adsManager.showMaxVideoAds(this, 0, false, "scratch");
            }
        });

    }
}