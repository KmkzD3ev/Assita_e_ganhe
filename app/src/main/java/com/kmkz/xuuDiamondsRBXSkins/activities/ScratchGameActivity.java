package com.kmkz.xuuDiamondsRBXSkins.activities;

import static com.kmkz.xuuDiamondsRBXSkins.Config.MAX_SCRATCH_POINTS;
import static com.kmkz.xuuDiamondsRBXSkins.Config.MIN_SCRATCH_POINTS;
import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.AdsManager;
import com.kmkz.xuuDiamondsRBXSkins.utility.ScratchCard;
import com.kmkz.xuuDiamondsRBXSkins.utility.ScratchTools;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import java.util.Objects;

public class ScratchGameActivity extends AppCompatActivity {

    ScratchCard mScratchCard;
    ImageView closeImg;
    TextView codeTxt;
    SharedPreferences prefs;
    int userPoints;
    int pointsAdded;
    AdsManager adsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_game);

        // Change system bar color
        setSystemBarColor(this, R.color.colorPrimary);
        initViews();

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);

        // Points Range
        codeTxt.setText(ScratchTools.generateNewCode(getApplicationContext(), MIN_SCRATCH_POINTS, MAX_SCRATCH_POINTS));

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

        closeImg.setOnClickListener(v -> finish());

        handleListeners();


        findViewById(R.id.btnScratchAgain).setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }

    private void initViews()
    {
        closeImg = findViewById(R.id.close_img);
        mScratchCard = findViewById(R.id.scratchCard);
        codeTxt = findViewById(R.id.codeTxt);
    }

    private void scratch(boolean isScratched) {
        if (isScratched) {
            mScratchCard.setVisibility(View.INVISIBLE);
        } else {
            mScratchCard.setVisibility(View.VISIBLE);
        }
    }
    private void handleListeners() {
        mScratchCard.setOnScratchListener((scratchCard, visiblePercent) -> {
            if (visiblePercent > 0.4) {
                scratch(true);

                pointsAdded = ScratchTools.getPoint();

                // Add Points
                showDialogPointsAdded();
            }
        });
    }

    public void ScratchImgClick(View view)
    {
        view.setVisibility(View.GONE);
        showDialogWatchVideo();
    }

    @SuppressLint("SetTextI18n")
    private void showDialogPointsAdded()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_scratch_congrats);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView congratsSubText = dialog.findViewById(R.id.txt_congrats_subText);

        congratsSubText.setText(getText(R.string.you_have_won) + " " + pointsAdded + " " + getString(R.string.points));

        dialog.findViewById(R.id.bt_ok).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void showDialogWatchVideo()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_watch_video);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.bt_ok).setOnClickListener(v -> {
            dialog.dismiss();
            pointsAdded = ScratchTools.getPoint();
            //ads
            if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Admob"))
            {
                adsManager.showRewardsAs(this, pointsAdded, false, "scratch");
            }
            else if (Objects.equals(Tools.getAppSettingsSharedPreferences(this, "AdsNetwork"), "Applovin"))
            {
                adsManager.showMaxVideoAds(this, pointsAdded, false, "scratch");
            }
        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}