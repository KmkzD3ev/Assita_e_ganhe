package com.kmkz.xuuDiamondsRBXSkins.activities;


import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

public class AboutActivity extends AppCompatActivity {

    ImageView closeImg;
    TextView appVersionTxt;
    LinearLayout contactBtn, rateBtn, shareBtn, privacyBtn, websiteBtn;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Change system bar color
        setSystemBarColor(AboutActivity.this, R.color.colorPrimary);
        initViews();

        // App Version Name
        appVersionTxt.setText(getString(R.string.app_version) + " " + Tools.getVersionName(this));

        closeImg.setOnClickListener(v -> finish());

        contactBtn.setOnClickListener(v ->
        {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{Tools.getAppSettingsSharedPreferences(this, "email")});
            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });

        rateBtn.setOnClickListener(v ->
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

        shareBtn.setOnClickListener(v ->
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = getString(R.string.Share_Text);
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            share.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(share, "choose one"));
        });

        privacyBtn.setOnClickListener(v ->
        {
            Intent pp = new Intent(Intent.ACTION_VIEW);
            pp.setData(Uri.parse(Tools.getAppSettingsSharedPreferences(this, "privacy_policy")));
            startActivity(pp);
        });

        websiteBtn.setOnClickListener(v ->
        {
            Intent website = new Intent(Intent.ACTION_VIEW);
            website.setData(Uri.parse(Tools.getAppSettingsSharedPreferences(this, "website")));
            startActivity(website);
        });
    }

    private void initViews()
    {
        closeImg = findViewById(R.id.close_img);
        appVersionTxt = findViewById(R.id.appVersionTxt);
        contactBtn = findViewById(R.id.contactBtn);
        rateBtn = findViewById(R.id.rateBtn);
        shareBtn = findViewById(R.id.shareBtn);
        privacyBtn = findViewById(R.id.privacyBtn);
        websiteBtn = findViewById(R.id.websiteBtn);
    }
}