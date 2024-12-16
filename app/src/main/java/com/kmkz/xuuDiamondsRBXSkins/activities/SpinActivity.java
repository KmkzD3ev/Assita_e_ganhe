package com.kmkz.xuuDiamondsRBXSkins.activities;

import static com.kmkz.xuuDiamondsRBXSkins.Config.SPIN_ROUNDS;
import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.model.LuckyItem;
import com.kmkz.xuuDiamondsRBXSkins.utility.LuckyWheelView;
import com.kmkz.xuuDiamondsRBXSkins.Config;

import com.kmkz.xuuDiamondsRBXSkins.utility.AdsManager;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {

    List<LuckyItem> data = new ArrayList<>();
    LuckyWheelView luckyWheelView;
    Button spinBtn;
    ImageView closeImg;
    private ProgressDialog pDialog;
    int userPoints;
    public int spinReward;
    SharedPreferences prefs;
    AdsManager adsManager;
    TextView resultTv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);

        initViews(); // Initialize Views

        // Change system bar color
        setSystemBarColor(this, R.color.colorPrimary);

        // Initialize AdsManager
        adsManager = new AdsManager();
        if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
        {
            AdsManager.showBannerAdmobAds(this);
            adsManager.setInterAds(this);
            adsManager.setRewardedAd(this);
        } else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
        {
            AdsManager.initApplovinAds(this);
            adsManager.loadMaxInterstitialAd(this);
            adsManager.loadMaxRewardsAd(this);
        }

        // Close Activity
        closeImg.setOnClickListener(v -> finish());

        // Get User Points
        prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);

        // Initialize Spin Wheel
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = Config.spinPrize1;
        luckyItem1.icon = R.drawable.coin_icon;
        luckyItem1.color = 0xffE0ECFF;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = Config.spinPrize2;
        luckyItem2.icon = R.drawable.coin_icon;
        luckyItem2.color = 0xff97B5E6;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = Config.spinPrize3;
        luckyItem3.icon = R.drawable.coin_icon;
        luckyItem3.color = 0xff6186C2;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = Config.spinPrize4;
        luckyItem4.icon = R.drawable.coin_icon;
        luckyItem4.color = 0xffE0ECFF;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = Config.spinPrize5;
        luckyItem5.icon = R.drawable.coin_icon;
        luckyItem5.color = 0xff97B5E6;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = Config.spinPrize6;
        luckyItem6.icon = R.drawable.coin_icon;
        luckyItem6.color = 0xff6186C2;
        data.add(luckyItem6);
        //////////////////

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = Config.spinPrize7;
        luckyItem7.icon = R.drawable.coin_icon;
        luckyItem7.color = 0xffE0ECFF;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = Config.spinPrize8;
        luckyItem8.icon = R.drawable.coin_icon;
        luckyItem8.color = 0xff97B5E6;
        data.add(luckyItem8);


        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.topText = Config.spinPrize9;
        luckyItem9.icon = R.drawable.coin_icon;
        luckyItem9.color = 0xff6186C2;
        data.add(luckyItem9);
        ////////////////////////

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.topText = Config.spinPrize10;
        luckyItem10.icon = R.drawable.coin_icon;
        luckyItem10.color = 0xffE0ECFF;
        data.add(luckyItem10);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.topText = Config.spinPrize11;
        luckyItem11.icon = R.drawable.coin_icon;
        luckyItem11.color = 0xff97B5E6;
        data.add(luckyItem11);

        LuckyItem luckyItem12 = new LuckyItem();
        luckyItem12.topText = Config.spinPrize12;
        luckyItem12.icon = R.drawable.coin_icon;
        luckyItem12.color = 0xff6186C2;
        data.add(luckyItem12);

        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(SPIN_ROUNDS);

        spinBtn.setOnClickListener(v ->
        {
            setSpinStartPlay();
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(index ->
        {
            spinReward = Integer.parseInt(data.get(index).topText);

            resultTv.setText(getString(R.string.you_win) + " " + spinReward + " " + getString(R.string.points));
            resultTv.setVisibility(TextView.VISIBLE);

            //ads
            if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
            {
                adsManager.showRewardsAs(this, 0, false, "spin");
            }
            else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
            {
                adsManager.showMaxVideoAds(this, 0, false, "spin");
            }
        });
    }

    private void initViews() {
        closeImg = findViewById(R.id.close_img);
        luckyWheelView = findViewById(R.id.luckyWheel);
        spinBtn = findViewById(R.id.spinBtn);
        resultTv = findViewById(R.id.resultTv);
    }

    public void setSpinStartPlay()
    {
        int index = getRandomIndex();
        luckyWheelView.startLuckyWheelWithTargetIndex(index);
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1);
    }

}