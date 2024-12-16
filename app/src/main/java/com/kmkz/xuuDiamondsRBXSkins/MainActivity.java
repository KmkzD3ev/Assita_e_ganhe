package com.kmkz.xuuDiamondsRBXSkins;

import static com.kmkz.xuuDiamondsRBXSkins.utility.Tools.setSystemBarColor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kmkz.xuuDiamondsRBXSkins.activities.DailyGiftActivity;
import com.kmkz.xuuDiamondsRBXSkins.activities.DuckGameActivity;
import com.kmkz.xuuDiamondsRBXSkins.activities.MenuActivity;
import com.kmkz.xuuDiamondsRBXSkins.activities.ScratchGameActivity;
import com.kmkz.xuuDiamondsRBXSkins.activities.SpinActivity;
import com.kmkz.xuuDiamondsRBXSkins.utility.AdsManager;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageView menuImg;
    TextView dailyGiftTxt, watchVideoTxt, smallAdsPointsTxt;
    public static TextView pointsTxt;
    LinearLayout dailyGiftBtn, spinBtn, watchVideoBtn, watchSmallBtn;
    SharedPreferences prefs;
    int userPoints;
    AdsManager adsManager;
    String topic = "SpinApp"; // Topic for Firebase Cloud Messaging

    @Override
    protected void onResume() {
        super.onResume();

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);
        pointsTxt.setText(String.valueOf(userPoints));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic(topic); // Subscribe to topic
        FirebaseMessaging.getInstance().setAutoInitEnabled(true); // Enable auto init


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission(); // Ask for notification permission
        }

        // Change system bar color
        setSystemBarColor(MainActivity.this, R.color.colorPrimary);

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);

        adsManager = new AdsManager(); // Initialize AdsManager

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


        menuImg = findViewById(R.id.menu_img);
        pointsTxt = findViewById(R.id.Points_Txt);
        dailyGiftBtn = findViewById(R.id.DailyGift_Btn);
        dailyGiftTxt = findViewById(R.id.DailyGiftTxt);
        spinBtn = findViewById(R.id.Spin_Btn);
        watchVideoBtn = findViewById(R.id.Watch_Btn);
        watchVideoTxt = findViewById(R.id.WatchVideoTxt);
        watchSmallBtn = findViewById(R.id.WatchSmall_Btn);
        smallAdsPointsTxt = findViewById(R.id.SmallAdsPointsTxt);

        pointsTxt.setText(String.valueOf(userPoints));
        dailyGiftTxt.setText(String.valueOf(Tools.getAppSettingsSharedPreferences(this, "dailyGiftPoints")));
        watchVideoTxt.setText(String.valueOf(Tools.getAppSettingsSharedPreferences(this, "watchVideoPoints")));
        smallAdsPointsTxt.setText(String.valueOf(Tools.getAppSettingsSharedPreferences(this, "smallAdsPoints")));


        menuImg.setOnClickListener(v ->
        {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(menuImg, "imageTransition");
            Intent goMenu = new Intent(MainActivity.this, MenuActivity.class);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(goMenu, activityOptions.toBundle());
        });

        dailyGiftBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), DailyGiftActivity.class);
            startActivity(intent);
        });

        spinBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), SpinActivity.class);
            startActivity(intent);
        });

        watchVideoBtn.setOnClickListener(v->
        {
            Intent intent = new Intent(getApplicationContext(), DuckGameActivity.class);
            startActivity(intent);
        });
        watchSmallBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), ScratchGameActivity.class);
            startActivity(intent);
        });
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Log.d("NotificationPermission", "onActivityResult: Permission Granted");
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Log.d("NotificationPermission", "onActivityResult: Permission Denied");
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                Log.d("NotificationPermission", "askNotificationPermission: Permission Granted");
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                Log.d("NotificationPermission", "askNotificationPermission: Permission Denied");
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}