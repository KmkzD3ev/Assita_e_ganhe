package com.kmkz.xuuDiamondsRBXSkins.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.activities.SpinActivity;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ibrahimodeh.ibratoastlib.IbraToast;

import java.util.concurrent.TimeUnit;

public class AdsManager implements MaxAdListener, MaxRewardedAdListener {

    InterstitialAd mInterstitialAd;
    RewardedAd mRewardedAd;
    MaxRewardedAd rewardedAd;
    MaxInterstitialAd maxInterstitialAd;
    int retryAttempt;

    /**
     * Show Banner Ads from Admob
     */
    public static void showBannerAdmobAds(Activity activity)
    {
        MobileAds.initialize(activity, initializationStatus -> {
        });
        AdView mAdView = activity.findViewById(R.id.adView);
        @SuppressLint("VisibleForTests")
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Initialize Applovin Ads
     */
    public static void initApplovinAds(Activity activity)
    {
        AppLovinSdk.getInstance( activity ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( activity, configuration -> {
            // AppLovin SDK is initialized, start loading ads
            showMaxBannerAd(activity);
        });
    }

    /**
     * Show Applovin Banner Ads
     */
    public static void showMaxBannerAd(Activity activity)
    {
        MaxAdView maxBannerAdView = activity.findViewById(R.id.MaxAdView);
        maxBannerAdView.loadAd();
    }

    /**
     * Load Applovin Interstitial Ads
     */
    public void loadMaxInterstitialAd(Activity activity){
        maxInterstitialAd = new MaxInterstitialAd( activity.getString(R.string.appLovin_interstitial_id), activity );
        maxInterstitialAd.setListener( this );

        maxInterstitialAd.loadAd();
    }

    /**
     * Show Applovin Interstitial Ads
     */
    public void showMaxInterstitialAd(Activity activity, int points, boolean isPointsCounted)
    {
        if ( maxInterstitialAd.isReady() )
        {
            maxInterstitialAd.showAd();
            if (isPointsCounted)
            {
                Tools.addThePoints(activity, points);
            }
        }else{
            IbraToast.makeText(activity, activity.getString(R.string.no_ads), Toast.LENGTH_SHORT, 2).show();
            loadMaxInterstitialAd(activity);
        }
    }

    /**
     * Ad Listener for Applovin Interstitial Ads
     */
    @Override
    public void onAdLoaded(@NonNull final MaxAd maxAd)
    {
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(@NonNull final String adUnitId, @NonNull final MaxError error)
    {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(() -> {
            maxInterstitialAd.loadAd();
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(@NonNull final MaxAd maxAd, @NonNull final MaxError error)
    {
        // Toast.makeText(getApplicationContext(), "onAdLoadFailed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        // ad failed to display. We recommend loading the next ad
        maxInterstitialAd.loadAd();
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayed(@NonNull final MaxAd maxAd) {
    }

    @Override
    public void onAdClicked(@NonNull final MaxAd maxAd) {}

    @Override
    public void onAdHidden(@NonNull final MaxAd maxAd)
    {
        // ad is hidden. Pre-load the next ad
        maxInterstitialAd.loadAd();
        rewardedAd.loadAd();
    }


    /**
     * Load Rewards Ads for Applovin
     */
    public void loadMaxRewardsAd(Activity activity)
    {
        rewardedAd = MaxRewardedAd.getInstance( activity.getString(R.string.appLovin_rewards_id), activity );
        rewardedAd.setListener( this );

        rewardedAd.loadAd();
    }

    /**
     * Rewards Ad Listener for Applovin
     */
    @Override
    public void onUserRewarded(@NonNull final MaxAd maxAd, @NonNull final MaxReward maxReward)
    {
        // Rewarded ad was displayed and user should receive the reward
        Log.e("Applovin", "onUserRewarded");
    }

    public void showMaxVideoAds(Activity activity, int points, boolean isPointsCounted, String type)
    {
        if ( rewardedAd.isReady() )
        {
            Log.e("Applovin", "isReady");
            rewardedAd.showAd();
            if (isPointsCounted)
            {
                Tools.addThePoints(activity, points);
                Tools.addAchieveProgress(activity, 1);
                Tools.addAchieveProgress(activity, 2);
            }
            if (type.equals("spin"))
            {
                SpinActivity spinActivity = (SpinActivity) activity;
                Tools.addThePoints(activity, spinActivity.spinReward);
                Tools.addAchieveProgress(activity, 3);
            }

            if (type.equals("scratch"))
            {
                Tools.addThePoints(activity, points);
            }
        }else{
            Log.e("Applovin", "Error: No Ads Now!");
            IbraToast.makeText(activity, activity.getString(R.string.no_ads), Toast.LENGTH_SHORT, 2).show();
        }
    }



    /**
     * Load Interstitial Ads for Admob
     */
    public void setInterAds(Activity activity)
    {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, activity.getString(R.string.admob_interstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Admob", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("Admob", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("Admob", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                                Log.d("Admob", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("Admob", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }


    /**
     * Show Interstitial Ads for Admob
     */
    public void showInterstitial(Activity activity, int points, boolean isPointsCounted) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
            if (isPointsCounted)
            {
                Tools.addThePoints(activity, points);
            }
            setInterAds(activity);
        } else {
            IbraToast.makeText(activity, activity.getString(R.string.no_ads), Toast.LENGTH_SHORT, 2).show();
        }
    }


    /**
     * Load Rewarded Ads for Admob
     */
    public void setRewardedAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(activity, activity.getString(R.string.admob_reward_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("Admob", loadAdError.getMessage());
                        mRewardedAd = null;
                        IbraToast.makeText(activity, activity.getString(R.string.ads_failed_load), Toast.LENGTH_SHORT, 2).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d("Admob", "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("Admob", "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                Log.d("Admob", "Ad failed to show.");
                                IbraToast.makeText(activity, activity.getString(R.string.ads_failed), Toast.LENGTH_SHORT, 2).show();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("Admob", "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                    }
                });
    }


    /**
     * Show Rewarded Ads for Admob
     */
    public void showRewardsAs(Activity activity, int points, boolean isPointsCounted, String type)
    {
        if (mRewardedAd != null) {
            mRewardedAd.show(activity, rewardItem -> {
                // Handle the reward.
                Log.d("Admob", "The user earned the reward.");
                setRewardedAd(activity);

                if (isPointsCounted)
                {
                    Tools.addThePoints(activity, points);
                    Tools.addAchieveProgress(activity, 1);
                    Tools.addAchieveProgress(activity, 2);
                }

                if (type.equals("spin"))
                {
                    SpinActivity spinActivity = (SpinActivity) activity;
                    Tools.addThePoints(activity, spinActivity.spinReward);
                    Tools.addAchieveProgress(activity, 3);
                }
                if (type.equals("scratch"))
                {
                    Tools.addThePoints(activity, points);
                }
            });
        } else {
            Log.d("Admob", "The rewarded ad wasn't ready yet.");
            IbraToast.makeText(activity, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT, 2).show();
            setRewardedAd(activity);
        }
    }

}
