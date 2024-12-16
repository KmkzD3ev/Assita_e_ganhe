package com.kmkz.xuuDiamondsRBXSkins.activities;

import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.AdsManager;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;
import com.ibrahimodeh.ibratoastlib.IbraToast;



import java.util.Calendar;
import java.util.Objects;

public class DailyGiftActivity extends AppCompatActivity {

    ImageView closeImg;
    SharedPreferences prefs;
    int userPoints;
    Button giftBtn;
    AdsManager adsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_gift);

        // Change system bar color
        setSystemBarColor(DailyGiftActivity.this, R.color.colorPrimary);

        closeImg = findViewById(R.id.close_img);
        giftBtn = findViewById(R.id.Gift_Btn);

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);

        adsManager = new AdsManager(); // Initialize AdsManager

        if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
        {
            AdsManager.showBannerAdmobAds(this);
            adsManager.setInterAds(this);
        } else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
        {
            AdsManager.initApplovinAds(this);
            adsManager.loadMaxInterstitialAd(this);
        }

        closeImg.setOnClickListener(v -> finish());

        giftBtn.setOnClickListener(v ->
        {
            if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
            {
                adsManager.showInterstitial(this, 0, false);
            } else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
            {
                adsManager.showMaxInterstitialAd(this, 0, false);
            }
            getTheGift();
        });

    }

    private void getTheGift()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayString = year + "" + month + "" + day;

        prefs = getSharedPreferences("Gift", Context.MODE_PRIVATE);
        boolean currentDay = prefs.getBoolean(todayString, false);

        if (!currentDay)
        {
            IbraToast.makeText(this, "Success !", Toast.LENGTH_LONG, 1).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(todayString, true);
            editor.apply();
            // Add Points
            addThePoints();

        }else {
            IbraToast.makeText(this, "Gift Today Received !", Toast.LENGTH_LONG, 2).show();
        }
    }

    private void addThePoints()
    {
        userPoints = userPoints + Integer.parseInt(Tools.getAppSettingsSharedPreferences(this, "dailyGiftPoints"));
        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("points", userPoints);
        editor.apply();
    }
}