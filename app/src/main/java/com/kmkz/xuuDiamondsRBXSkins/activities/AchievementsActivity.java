package com.kmkz.xuuDiamondsRBXSkins.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmkz.xuuDiamondsRBXSkins.Config;
import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import com.ibrahimodeh.ibratoastlib.IbraToast;

public class AchievementsActivity extends AppCompatActivity {

    ImageView closeImg;
    LinearLayout lyt_achieve_one, lyt_achieve_two, lyt_achieve_three;
    TextView txt_ac_one, txt_ac_two, txt_ac_three;
    ProgressBar progress_ac_one, progress_ac_two, progress_ac_three;
    Button btn_ac_one, btn_ac_two, btn_ac_three;
    SharedPreferences prefs;
    int ac_one_count, ac_two_count, ac_three_count;
    boolean is_ac_one_completed, is_ac_two_completed, is_ac_three_completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        initViews();
        setProgressValues();
        checkAchievements();

        // Change system bar color
        Tools.setSystemBarColor(this, R.color.colorPrimary);

        closeImg.setOnClickListener(v -> finish());

        btn_ac_one.setOnClickListener(v -> {
            if (ac_one_count >= Config.AC_ONE_UNLOCK) {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.completed), IbraToast.LENGTH_LONG, IbraToast.SUCCESS).show();
                Tools.addThePoints(this, Config.AC_ONE_POINTS);
                setAchieveCompleted(1);
                checkAchievements();
            } else {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.not_completed), IbraToast.LENGTH_LONG, IbraToast.ERROR).show();
            }
        });

        btn_ac_two.setOnClickListener(v -> {
            if (ac_two_count >= Config.AC_TWO_UNLOCK) {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.completed), IbraToast.LENGTH_LONG, IbraToast.SUCCESS).show();
                Tools.addThePoints(this, Config.AC_TWO_POINTS);
                setAchieveCompleted(2);
                checkAchievements();
            } else {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.not_completed), IbraToast.LENGTH_LONG, IbraToast.ERROR).show();
            }
        });

        btn_ac_three.setOnClickListener(v -> {
            if (ac_three_count >= Config.AC_THREE_UNLOCK) {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.completed), IbraToast.LENGTH_LONG, IbraToast.SUCCESS).show();
                Tools.addThePoints(this, Config.AC_THREE_POINTS);
                setAchieveCompleted(3);
                checkAchievements();
            } else {
                IbraToast.makeText(AchievementsActivity.this, getString(R.string.not_completed), IbraToast.LENGTH_LONG, IbraToast.ERROR).show();
            }
        });


        // Get the Achievements Points Rewards
        txt_ac_one.setText(String.valueOf(Config.AC_ONE_POINTS));
        txt_ac_two.setText(String.valueOf(Config.AC_TWO_POINTS));
        txt_ac_three.setText(String.valueOf(Config.AC_THREE_POINTS));
    }

    private void initViews() {
        closeImg = findViewById(R.id.close_img);
        lyt_achieve_one = findViewById(R.id.lyt_achieve_one);
        lyt_achieve_two = findViewById(R.id.lyt_achieve_two);
        lyt_achieve_three = findViewById(R.id.lyt_achieve_three);
        txt_ac_one = findViewById(R.id.txt_ac_one);
        txt_ac_two = findViewById(R.id.txt_ac_two);
        txt_ac_three = findViewById(R.id.txt_ac_three);
        progress_ac_one = findViewById(R.id.progress_ac_one);
        progress_ac_two = findViewById(R.id.progress_ac_two);
        progress_ac_three = findViewById(R.id.progress_ac_three);
        btn_ac_one = findViewById(R.id.btn_ac_one);
        btn_ac_two = findViewById(R.id.btn_ac_two);
        btn_ac_three = findViewById(R.id.btn_ac_three);
    }

    private void setProgressValues()
    {
        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        ac_one_count = prefs.getInt("ac_one_count", 0);
        ac_two_count = prefs.getInt("ac_two_count", 0);
        ac_three_count = prefs.getInt("ac_three_count", 0);

        progress_ac_one.setProgress(ac_one_count);
        progress_ac_two.setProgress(ac_two_count);
        progress_ac_three.setProgress(ac_three_count);

        // Set the maximum progress values
        progress_ac_one.setMax(Config.AC_ONE_UNLOCK);
        progress_ac_two.setMax(Config.AC_TWO_UNLOCK);
        progress_ac_three.setMax(Config.AC_THREE_UNLOCK);
    }

    private void checkAchievements()
    {
        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        is_ac_one_completed = prefs.getBoolean("ac_one_completed", false);
        is_ac_two_completed = prefs.getBoolean("ac_two_completed", false);
        is_ac_three_completed = prefs.getBoolean("ac_three_completed", false);

        if (is_ac_one_completed) {
            btn_ac_one.setText(getString(R.string.completed));
            btn_ac_one.setEnabled(false);
            btn_ac_one.setAlpha(0.5f);
        }
        if (is_ac_two_completed) {
            btn_ac_two.setText(getString(R.string.completed));
            btn_ac_two.setEnabled(false);
            btn_ac_two.setAlpha(0.5f);
        }
        if (is_ac_three_completed) {
            btn_ac_three.setText(getString(R.string.completed));
            btn_ac_three.setEnabled(false);
            btn_ac_three.setAlpha(0.5f);
        }
    }

    private void setAchieveCompleted(int ac_number)
    {
        SharedPreferences.Editor editor = prefs.edit();
        switch (ac_number) {
            case 1:
                editor.putBoolean("ac_one_completed", true);
                break;
            case 2:
                editor.putBoolean("ac_two_completed", true);
                break;
            case 3:
                editor.putBoolean("ac_three_completed", true);
                break;
        }
        editor.apply();
    }
}