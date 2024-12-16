package com.kmkz.xuuDiamondsRBXSkins.activities;

import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kmkz.xuuDiamondsRBXSkins.MainActivity;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

public class MenuActivity extends AppCompatActivity {

    ImageView closeImg, shareImg, rateImg;
    LinearLayout instructionsBtn, withdrawalBtn, aboutBtn, contactBtn, achievementsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Change system bar color
        setSystemBarColor(MenuActivity.this, R.color.colorPrimary);

        initViews(); // Initialize Views

        closeImg.setOnClickListener(v ->
        {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(closeImg, "imageTransition");

            Intent goHome = new Intent(MenuActivity.this, MainActivity.class);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MenuActivity.this, pairs);

            startActivity(goHome, activityOptions.toBundle());
        });

        instructionsBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), InstructionsActivity.class);
            startActivity(intent);
        });

        achievementsBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), AchievementsActivity.class);
            startActivity(intent);
        });

        withdrawalBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), WithdrawalActivity.class);
            startActivity(intent);
        });

        aboutBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        });

        contactBtn.setOnClickListener(v ->
        {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{Tools.getAppSettingsSharedPreferences(this, "emailAddress")});
            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });

        shareImg.setOnClickListener(v ->
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = getString(R.string.Share_Text);
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            share.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(share, "choose one"));
        });

        rateImg.setOnClickListener(v ->
        {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
    }

    private void initViews()
    {
        closeImg = findViewById(R.id.close_img);
        shareImg = findViewById(R.id.share_img);
        rateImg = findViewById(R.id.rate_img);
        instructionsBtn = findViewById(R.id.Instructions_Btn);
        withdrawalBtn = findViewById(R.id.Withdrawal_Btn);
        aboutBtn = findViewById(R.id.About_Btn);
        contactBtn = findViewById(R.id.Contact_Btn);
        achievementsBtn = findViewById(R.id.Achievements_Btn);
    }
}